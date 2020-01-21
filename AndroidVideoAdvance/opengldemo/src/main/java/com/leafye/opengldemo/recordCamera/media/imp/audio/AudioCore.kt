package com.leafye.opengldemo.recordCamera.media.imp.audio

import android.media.AudioFormat
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.os.*
import android.util.Log
import com.leafye.opengldemo.recordCamera.config.MediaMakerConfig
import com.leafye.opengldemo.recordCamera.filter.softaudiofilter.BaseSoftAudioFilter
import com.leafye.opengldemo.recordCamera.media.MediaCodecHelper
import com.leafye.opengldemo.recordCamera.media.encode.MediaMuxerWrapper
import com.leafye.opengldemo.recordCamera.model.AudioBuff
import com.leafye.opengldemo.utils.LogObj
import com.leafye.opengldemo.utils.logNull
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

class AudioCore(private val mediaMakerConfig: MediaMakerConfig) {
    companion object {
        private const val TAG = "AudioCore"
    }

    private var dstAudioEncoder: MediaCodec? = null
    private var lockAudioFilter: Lock? = null
    private var dstAudioFormat: MediaFormat? = null

    private var audioFilter: BaseSoftAudioFilter? = null
    //AudioBuffs
    //buffers to handle buff from queueAudio
    private var originAudioBuffs: Array<AudioBuff?>? = null
    private var lastAudioQueueBuffIndex: Int = 0
    //buffer to handle buff from originAudioBuffs
    private var originAudioBuff: AudioBuff? = null
    private var filteredAudioBuff: AudioBuff? = null
    private var audioFilterHandler: AudioFilterHandler? = null
    private var audioFilterHandlerThread: HandlerThread? = null
    private var audioSenderThread: AudioSenderThread? = null

    fun queueAudio(rawAudioFrame: ByteArray) {
        originAudioBuffs.logNull(TAG) ?: return
        val targetIndex = (lastAudioQueueBuffIndex + 1) % originAudioBuffs!!.size
        if (originAudioBuffs!![targetIndex]!!.isReadyToFill) {
            Log.d("", "queueAudio,accept ,targetIndex$targetIndex")
            System.arraycopy(
                rawAudioFrame,
                0,
                originAudioBuffs!![targetIndex]!!.buff!!,
                0,
                mediaMakerConfig.audioRecoderBufferSize
            )
            originAudioBuffs!![targetIndex]!!.isReadyToFill = false
            lastAudioQueueBuffIndex = targetIndex

            audioFilterHandler?.obtainMessage(
                AudioFilterHandler.WHAT_INCOMING_BUFF,
                targetIndex,
                0
            )?.let {
                audioFilterHandler?.sendMessage(it)
            }

        } else {
            Log.d("", "queueAudio,abandon,targetIndex$targetIndex")
        }
    }

    @Synchronized
    fun prepare(): Boolean {
        mediaMakerConfig.apply {
            mediacodecAACProfile = MediaCodecInfo.CodecProfileLevel.AACObjectLC
            mediacodecAACSampleRate = 44100
            mediacodecAACChannelCount = 1
            mediacodecAACBitRate = 32 * 1024
            mediacodecAACMaxInputSize = 8820
        }
        dstAudioFormat = MediaFormat()
        dstAudioEncoder = MediaCodecHelper.createAudioMediaCodec(mediaMakerConfig, dstAudioFormat!!)
        if (dstAudioEncoder == null) {
            Log.e(TAG, "create Audio MediaCodec failed")
            return false
        }
        val bufferQueueNum = mediaMakerConfig.audioBufferQueueNum
        val originAudioBuffSize = mediaMakerConfig.mediacodecAACSampleRate / 5
        originAudioBuffs = arrayOfNulls(bufferQueueNum)

        for (i in 0 until bufferQueueNum) {
            originAudioBuffs?.let {
                it[i] = AudioBuff(AudioFormat.ENCODING_PCM_16BIT, originAudioBuffSize)
            }
        }
        originAudioBuff = AudioBuff(AudioFormat.ENCODING_PCM_16BIT, originAudioBuffSize)
        filteredAudioBuff = AudioBuff(AudioFormat.ENCODING_PCM_16BIT, originAudioBuffSize)
        return true
    }

    @Synchronized
    fun startRecording(muxer: MediaMuxerWrapper) {
        if (originAudioBuff == null
            || filteredAudioBuff == null
            || dstAudioFormat == null
            || audioFilterHandlerThread == null
        ) {
            Log.e(TAG, "data not init!! please invoke method \"prepare()\" first")
            return
        }
        try {
            //originAudioBuffs?.forEach { it.isReadyToFill = true }
            if (dstAudioEncoder == null) {
                dstAudioEncoder =
                    MediaCodec.createEncoderByType(dstAudioFormat!!.getString(MediaFormat.KEY_MIME))
            }
            dstAudioEncoder?.configure(
                dstAudioFormat,
                null,
                null,
                MediaCodec.CONFIGURE_FLAG_ENCODE
            )
            dstAudioEncoder?.start()
            lastAudioQueueBuffIndex = 0
            audioFilterHandlerThread = HandlerThread("audioFilterHandlerThread")
            audioSenderThread = AudioSenderThread("AudioSenderThread", dstAudioEncoder!!, muxer)
            audioFilterHandlerThread?.start()
            audioSenderThread?.start()
            audioFilterHandler = AudioFilterHandler(this, audioFilterHandlerThread!!.looper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        audioFilterHandler?.removeCallbacksAndMessages(null)
        audioFilterHandlerThread?.quit()
        try {
            audioFilterHandlerThread?.join()
            audioSenderThread?.quit()
            audioSenderThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        audioFilterHandlerThread = null
        audioSenderThread = null
        dstAudioEncoder?.stop()
        dstAudioEncoder?.release()
        dstAudioEncoder = null
    }

    fun acquireAudioFilter(): BaseSoftAudioFilter? {
        lockAudioFilter?.lock()
        return audioFilter
    }

    fun releaseAudioFilter() {
        lockAudioFilter?.unlock()
    }

    fun setAudioFilter(baseSoftAudioFilter: BaseSoftAudioFilter) {
        lockAudioFilter?.lock()
        audioFilter?.onDestroy()
        audioFilter = baseSoftAudioFilter
        audioFilter?.onInit(mediaMakerConfig.mediacodecAACSampleRate / 5)
        lockAudioFilter?.unlock()
    }

    fun destroy() {
        lockAudioFilter?.lock()
        audioFilter?.onDestroy()
        lockAudioFilter?.unlock()
    }

    fun copyOriginAudioBuffs(targetIndex: Int): Unit {
        originAudioBuffs?.let { arr ->
            originAudioBuff?.let { buff ->
                System.arraycopy(
                    arr[targetIndex]!!.buff!!, 0,
                    buff.buff!!, 0, buff.buff!!.size
                )
            }
            arr[targetIndex]?.isReadyToFill = true
        }
    }

    private fun audioFilterOnFrame(time: Long, sequenceNum: Int): Boolean {
        originAudioBuff?.logNull(TAG) ?: return false
        audioFilter?.logNull(TAG) ?: return false
        return audioFilter!!.onFrame(
            originAudioBuff!!.buff!!,
            originAudioBuff!!.buff!!,
            time,
            sequenceNum
        )
    }

    private fun queueInputBuffer(filtered: Boolean, time: Long) {
        dstAudioEncoder.logNull(TAG) ?: return
        filteredAudioBuff.logNull(TAG) ?: return
        originAudioBuff.logNull(TAG) ?: return

        val eibIndex = dstAudioEncoder!!.dequeueInputBuffer(-1)
        if (eibIndex >= 0) {
            val dstAudioEncoderIBuffer = dstAudioEncoder!!.inputBuffers[eibIndex]
            dstAudioEncoderIBuffer.position(0)
            dstAudioEncoderIBuffer.put(
                if (filtered) filteredAudioBuff!!.buff else originAudioBuff!!.buff,
                0,
                originAudioBuff?.buff?.size ?: 0
            )
            dstAudioEncoder!!.queueInputBuffer(
                eibIndex,
                0,
                originAudioBuff?.buff?.size ?: 0,
                time * 1000,
                0
            )
        } else {
            Log.d("", "dstAudioEncoder.dequeueInputBuffer(-1)<0")
        }
    }

    class AudioFilterHandler(core: AudioCore, looper: Looper) : Handler(looper) {

        private val coreWeakReference: WeakReference<AudioCore> = WeakReference<AudioCore>(core)

        companion object {
            private const val FILTER_LOCK_TOLERATION = 3
            const val WHAT_INCOMING_BUFF = 1
        }

        private var sequenceNum = 0
        override fun handleMessage(msg: Message) {
            if (WHAT_INCOMING_BUFF != msg.what) return
            sequenceNum++
            val targetIndex = msg.arg1
            val nowTimeMs = SystemClock.uptimeMillis()
            coreWeakReference.get()?.let {
                it.copyOriginAudioBuffs(targetIndex)
                val lockAudioFilter = lockAudioFilter()
                var filtered = false
                if (lockAudioFilter) {
                    filtered = it.audioFilterOnFrame(nowTimeMs, sequenceNum)
                    unlockAudioFilter()
                } else {
                    it.copyOriginAudioBuffs(targetIndex)
                }
                it.queueInputBuffer(filtered, nowTimeMs)
            }
        }

        private fun lockAudioFilter(): Boolean {
            val lockAudioFilter = coreWeakReference.get()?.lockAudioFilter ?: return false
            try {
                val locked =
                    lockAudioFilter.tryLock(FILTER_LOCK_TOLERATION.toLong(), TimeUnit.MILLISECONDS)
                if (locked) {
                    coreWeakReference.get()?.audioFilter ?: return false.also {
                        lockAudioFilter.unlock()
                    }
                    return true
                } else {
                    return false
                }
            } catch (e: InterruptedException) {
                LogObj.logException(TAG, e)
            }

            return false
        }

        private fun unlockAudioFilter() {
            coreWeakReference.get()?.lockAudioFilter?.unlock()
        }
    }
}