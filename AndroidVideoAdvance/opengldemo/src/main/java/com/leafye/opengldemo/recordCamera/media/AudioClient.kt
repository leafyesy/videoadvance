package com.leafye.opengldemo.recordCamera.media

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.leafye.opengldemo.recordCamera.config.MediaMakerConfig
import com.leafye.opengldemo.recordCamera.config.RecordConfig
import com.leafye.opengldemo.recordCamera.filter.softaudiofilter.BaseSoftAudioFilter
import com.leafye.opengldemo.recordCamera.media.encode.MediaMuxerWrapper
import com.leafye.opengldemo.recordCamera.media.imp.audio.AudioCore
import com.leafye.opengldemo.utils.LogObj
import com.leafye.opengldemo.utils.logNull

class AudioClient(private val mediaMakerConfig: MediaMakerConfig) {

    companion object {
        private const val TAG = "AudioClient"
    }

    private var audioRecord: AudioRecord? = null
    private var audioBuffer: ByteArray? = null
    private var softAudioCore: AudioCore? = null
    private var audioRecordThread: AudioRecordThread? = null

    @Synchronized
    fun prepare(recordConfig: RecordConfig): Boolean {
        mediaMakerConfig.audioBufferQueueNum = 5
        softAudioCore = AudioCore(mediaMakerConfig).apply {
            if (!this.prepare()) {
                Log.e("", "AudioClient,prepare")
                return false
            }
        }
        mediaMakerConfig.audioRecoderFormat = AudioFormat.ENCODING_PCM_16BIT
        mediaMakerConfig.audioRecoderChannelConfig = AudioFormat.CHANNEL_IN_MONO
        mediaMakerConfig.audioRecoderSliceSize = mediaMakerConfig.mediacodecAACSampleRate / 10
        mediaMakerConfig.audioRecoderBufferSize = mediaMakerConfig.audioRecoderSliceSize * 2
        mediaMakerConfig.audioRecoderSource = MediaRecorder.AudioSource.DEFAULT
        mediaMakerConfig.audioRecoderSampleRate = mediaMakerConfig.mediacodecAACSampleRate
        prepareAudio()
        return true
    }

    @Synchronized
    fun startRecording(muxer: MediaMuxerWrapper): Boolean {
        softAudioCore?.startRecording(muxer)
        audioRecord?.startRecording()
        audioRecordThread = AudioRecordThread().also {
            it.start()
        }
        Log.d("", "AudioClient,start()")
        return true
    }

    @Synchronized
    fun stopRecording(): Boolean {
        audioRecordThread?.quit()
        try {
            audioRecordThread?.join()
        } catch (ignored: InterruptedException) {
            LogObj.logException(TAG, ignored)
        }
        audioRecordThread = null
        softAudioCore?.stop()
        audioRecord?.stop()
        return true
    }

    @Synchronized
    fun destroy(): Boolean {
        audioRecord?.release()
        return true
    }

    fun setSoftAudioFilter(baseSoftAudioFilter: BaseSoftAudioFilter) {
        softAudioCore?.setAudioFilter(baseSoftAudioFilter)
    }

    fun acquireSoftAudioFilter(): BaseSoftAudioFilter? {
        return softAudioCore?.acquireAudioFilter()
    }

    fun releaseSoftAudioFilter() {
        softAudioCore?.releaseAudioFilter()
    }

    private fun prepareAudio(): Boolean {
        val minBufferSize = AudioRecord.getMinBufferSize(
            mediaMakerConfig.audioRecoderSampleRate,
            mediaMakerConfig.audioRecoderChannelConfig,
            mediaMakerConfig.audioRecoderFormat
        )
        audioRecord = AudioRecord(
            mediaMakerConfig.audioRecoderSource,
            mediaMakerConfig.audioRecoderSampleRate,
            mediaMakerConfig.audioRecoderChannelConfig,
            mediaMakerConfig.audioRecoderFormat,
            minBufferSize * 5
        ).apply {
            audioBuffer = ByteArray(mediaMakerConfig.audioRecoderBufferSize)
            if (AudioRecord.STATE_INITIALIZED != this.state) {
                Log.e("", "audioRecord.getState()!=AudioRecord.STATE_INITIALIZED!")
                return false
            }
            if (AudioRecord.SUCCESS != this.setPositionNotificationPeriod(mediaMakerConfig.audioRecoderSliceSize)) {
                Log.e(
                    "",
                    "AudioRecord.SUCCESS != audioRecord.setPositionNotificationPeriod(" + mediaMakerConfig.audioRecoderSliceSize + ")"
                )
                return false
            }
        }
        return true
    }

    internal inner class AudioRecordThread : Thread() {
        private var isRunning = true

        init {
            isRunning = true
        }

        fun quit() {
            isRunning = false
        }

        override fun run() {
            audioBuffer.logNull("AudioRecordThread") ?: return
            Log.d("", "AudioRecordThread,tid=" + Thread.currentThread().id)
            while (isRunning) {
                val size = audioRecord?.read(audioBuffer!!, 0, audioBuffer!!.size) ?: 0
                if (isRunning && softAudioCore != null && size > 0) {
                    softAudioCore?.queueAudio(audioBuffer!!)
                }
            }
        }
    }

}