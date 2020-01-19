package com.leafye.audiorecorddemo.audio

import android.annotation.SuppressLint
import android.media.AudioRecord
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * 音频读取线程
 * 用于读取AudioRecord写入到缓存区的数据
 *
 */
class AudioWrite(
    private val path: String,
    private val record: AudioRecord,
    private val bufferSizeInBytes: Int
) : Runnable {

    private val file by lazy {
        File(path)
            .also {
                if (it.exists()) {
                    it.delete()
                }
                it.createNewFile()
            }
    }
    private val data by lazy { ByteArray(bufferSizeInBytes) }

    //private val byteBuff = ByteBuffer.wrap(ByteArray(bufferSizeInBytes))

    private val dataOutputStream: BufferedOutputStream by lazy {
        BufferedOutputStream(FileOutputStream(file))
    }

    private var isRecording: Boolean = false


    init {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO)
    }

    override fun run() {
        isRecording = true
        while (isRecording
            && record.recordingState == AudioRecord.RECORDSTATE_RECORDING
        ) {
            read()
        }
    }

    @SuppressLint("NewApi")
    private fun read() {
        val ret = record.read(data, 0, bufferSizeInBytes)
        if (AudioRecord.ERROR_INVALID_OPERATION != ret
            && AudioRecord.ERROR_BAD_VALUE != ret
        ) {
            dataOutputStream.write(data, 0, ret)
        }
    }

    @Throws(AudioException::class)
    fun stopRecord() {
        isRecording = false
        record.let {
            if (it.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                it.stop()
            }
            if (it.state == AudioRecord.STATE_INITIALIZED) {
                it.release()
            }
        }
    }
}