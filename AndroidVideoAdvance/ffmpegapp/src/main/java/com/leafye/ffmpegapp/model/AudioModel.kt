package com.leafye.ffmpegapp.model

import com.leafye.base.BaseModel
import com.leafye.ffmpegapp.utils.FFmpegTestFileManager

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.model
 * @ClassName:      AudioModel
 * @Description:    音频处理Model
 * @Author:         leafye
 * @CreateDate:     2020/3/5 15:45
 * @UpdateUser:
 * @UpdateDate:     2020/3/5 15:45
 * @UpdateRemark:
 */
class AudioModel : BaseModel {
    /**
     * 音频转码用的文件路径
     */
    fun getTransformAudioPath() = FFmpegTestFileManager.basePath() + "/transformAudio.mp3"

    /**
     * 音频剪切用的文件路径
     */
    fun getCutPath() = FFmpegTestFileManager.basePath() + "/cutAudio.mp3"

    /**
     * 测试用第二个音频文件
     */
    fun getAppendAudioPath(): String = FFmpegTestFileManager.basePath() + "/append.mp3"

    /**
     * 追加文件生成文件路径
     */
    fun getConcatPath(): String = FFmpegTestFileManager.basePath() + "/concat.mp3"

    /**
     * 混音文件
     */
    fun getMixPath(): String = FFmpegTestFileManager.basePath() + "/mix.mp3"

    /**
     * 测试用pcm数据
     */
    fun getTestPcmPath(): String = FFmpegTestFileManager.basePath() + "/test.pcm"

    fun encodeWavPath() = FFmpegTestFileManager.basePath() + "/new.wav"

    fun srcAudioFile(): String {
        return FFmpegTestFileManager.audioFileSrc()
    }


}