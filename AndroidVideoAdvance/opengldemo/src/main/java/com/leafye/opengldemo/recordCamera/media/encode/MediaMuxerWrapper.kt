package com.leafye.opengldemo.recordCamera.media.encode

import android.media.MediaCodec
import android.media.MediaFormat
import android.media.MediaMuxer
import android.util.Log
import java.lang.IllegalStateException
import java.nio.ByteBuffer

/**
 * leafye 2020/1/21
 *
 */
class MediaMuxerWrapper(private val outputPath: String) {

    companion object {
        private const val TAG = "MediaMuxerWrapper"
    }

    var encodeCount = 0

    var startCount = 0
        private set

    var isStart: Boolean = false
        private set

    private val muxer: MediaMuxer by lazy {
        MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
    }

    /**
     * request start recording from encoder
     * @return true when muxer is ready to write
     */
    @Synchronized
    fun start(): Boolean {
        startCount++
        if (encodeCount > 0 && startCount == encodeCount) {
            muxer.start()
            isStart = true
            //notifyAll()
        }
        return isStart
    }

    /**
     * request stop recording from encoder when encoder received EOS
     */
    @Synchronized
    fun stop() {
        if (!isStart) return
        if (encodeCount > 0 && startCount <= 0) {
            muxer.stop()
            muxer.release()
            isStart = false
        }
    }

    @Synchronized
    @Throws(IllegalStateException::class)
    fun addTrack(format: MediaFormat): Int {
        check(!isStart) { "muxer already started" }
        return muxer.addTrack(format).also {
            Log.i(TAG, "addTrack:trackNum=$encodeCount,trackIx=$it,format=$format")
        }
    }

    fun writeSampleData(
        trackIndex: Int,
        byteBuffer: ByteBuffer,
        bufferInfo: MediaCodec.BufferInfo
    ) {
        if (isStart) return
        if (startCount > 0)
            muxer.writeSampleData(trackIndex, byteBuffer, bufferInfo)

    }


}