package com.leafye.audiorecorddemo.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment

/**
 * 音频采集类
 * 准备工作:
 * need permission
 * 1.android.permission.RECORD_AUDIO
 * 2.android.permission.WRITE_EXTERNAL_STORAGE
 *
 *
 */
class AudioRecorder {

    companion object {

        private const val AUDIO_RESOURCE = MediaRecorder.AudioSource.MIC //麦克风
        private const val AUDIO_SAMPLE_RATE = 16000//采样率
        //CHANNEL_IN_MONO单通道   CHANNEL_IN_STEREO双声道
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT//pcm编码数据,数据位宽为16bit
        private val BUFFER_SIZE = AudioRecord.getMinBufferSize(
            AUDIO_SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT
        )
    }

    private var recorder: AudioRecord? = null
    private var audioReadTask: AudioWrite? = null

    init {
        createRecord()
    }

    /**
     * 开始录制
     * state->RECORDSTATE_RECORDING
     *  1.ERROR_INVALID_OPERATION if the object wasn't properly initialized
     *  2.ERROR_BAD_VALUE if the parameters don't resolve to valid data and indexes.
     *
     * exception:
     */
    fun startRecording() {
        createRecord()
        checkState()
        recorder?.startRecording()
        audioReadTask?.let {
            Thread(it).start()
        }
    }

    /**
     * 停止采集
     * state->RECORDSTATE_STOPPED
     */
    fun stop() {
        audioReadTask?.stopRecord()
        audioReadTask = null
        recorder = null
    }

    /**
     * Returns the state of the AudioRecord instance. This is useful after the
     * AudioRecord instance has been created to check if it was initialized
     * properly. This ensures that the appropriate hardware resources have been
     * acquired.
     * @see AudioRecord#STATE_INITIALIZED
     * @see AudioRecord#STATE_UNINITIALIZED
     */
    fun getRecordState(): Int {
        return recorder?.state ?: AudioRecord.STATE_UNINITIALIZED
    }

    /**
     * Returns the recording state of the AudioRecord instance.
     * @see AudioRecord#RECORDSTATE_STOPPED
     * @see AudioRecord#RECORDSTATE_RECORDING
     */
    fun getRecordingState(): Int {
        return recorder?.recordingState ?: AudioRecord.RECORDSTATE_STOPPED
    }


    private fun newRecord(): AudioRecord {
        return AudioRecord(
            AUDIO_RESOURCE,
            AUDIO_SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            BUFFER_SIZE
        ).also {
            recorder = it
        }
    }

    private fun createRecord() {
        if (recorder == null) {
            newRecord()
        }
        if (audioReadTask == null) {
            val savePath = getSavePath()
            audioReadTask = AudioWrite(savePath, recorder!!, BUFFER_SIZE)
        }
    }

    fun getSavePath() =
        Environment.getExternalStorageDirectory().absolutePath + "/audio_tmp.pcm"

    private fun checkState() {
        val recordState = getRecordState()
        if (AudioRecord.STATE_UNINITIALIZED == recordState) {
            throw AudioException("AudioRecorder is not init")
        }
    }


}