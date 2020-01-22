package com.leafye.opengldemo.recordCamera.media.imp.audio

import android.media.MediaCodec
import android.util.Log
import com.leafye.opengldemo.recordCamera.media.encode.MediaMuxerWrapper
import java.lang.Exception
import java.lang.ref.WeakReference

class AudioSenderThread(
    name: String,
    private val dstAudioEncoder: MediaCodec,
    private val muxer: MediaMuxerWrapper
) : Thread(name) {

    companion object {
        private const val WAIT_TIME = 5000L
        private const val TAG = "AudioSenderThread"
    }

    private val eInfo: MediaCodec.BufferInfo by lazy {
        MediaCodec.BufferInfo()
    }
    private var startTime: Long = 0L
    private var shouldQuit = false
    /**
     * previous presentationTimeUs for writing
     */
    private var prevOutputPTSUs: Long = 0

    protected var muxerStarted: Boolean = false
    protected var trackIndex: Int = 0
    protected val muxerWeakReference: WeakReference<MediaMuxerWrapper> by lazy {
        WeakReference<MediaMuxerWrapper>(muxer)
    }

    fun quit() {
        shouldQuit = true
        this.interrupt()
        if (muxerStarted) {
            muxerWeakReference.get()?.let {
                try {
                    it.stop()
                } catch (e: Exception) {
                    Log.w(TAG, e.toString())
                }
            }
        }
    }

    override fun run() {
        val muxerWrapper = muxerWeakReference.get()
        while (!shouldQuit) {
            when (val eobIndex = dstAudioEncoder.dequeueOutputBuffer(eInfo, WAIT_TIME)) {
                MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED ->
                    Log.w(TAG, ">>INFO_OUTPUT_FORMAT_CHANGED")
                MediaCodec.INFO_TRY_AGAIN_LATER ->
                    Log.d(TAG, ">>INFO_TRY_AGAIN_LATER")
                MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                    Log.d(TAG, ">>INFO_OUTPUT_FORMAT_CHANGED")
                    muxerWrapper?.let {
                        val format = dstAudioEncoder.outputFormat
                        it.addTrack(format)
                        it.start()
                        muxerStarted = true
                    }
                }
                else -> {
                    Log.d(TAG, "other eobIndex:$eobIndex")
                    if (startTime == 0L) {
                        startTime = eInfo.presentationTimeUs / 1000
                    }
                    if (eInfo.flags != MediaCodec.BUFFER_FLAG_CODEC_CONFIG && eInfo.size != 0) {
                        val realData = dstAudioEncoder.outputBuffers[eobIndex]
                        realData.position(eInfo.offset)
                        realData.limit(eInfo.offset + eInfo.size)
                        muxerWrapper?.let {
                            if (muxerStarted) {
                                eInfo.presentationTimeUs = getPTSUs()
                                it.writeSampleData(trackIndex, realData, eInfo)
                                prevOutputPTSUs = eInfo.presentationTimeUs
                            }
                        }
                    }
                    dstAudioEncoder.releaseOutputBuffer(eobIndex, false)
                }
            }
        }
    }


    /**
     * get next encoding presentationTimeUs
     * @return
     */
    protected fun getPTSUs(): Long {
        var result = System.nanoTime() / 1000L
        // presentationTimeUs should be monotonic
        // otherwise muxer fail to write
        if (result < prevOutputPTSUs)
            result += prevOutputPTSUs - result
        return result
    }
}