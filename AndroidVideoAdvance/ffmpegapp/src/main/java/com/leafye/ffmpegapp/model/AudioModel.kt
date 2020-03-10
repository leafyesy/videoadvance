package com.leafye.ffmpegapp.model

import com.leafye.base.BaseModel
import com.leafye.ffmpegapp.utils.FFmpegTestFileManager

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.model
 * @ClassName:      AudioModel
 * @Description:     java类作用描述
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
    fun getTransformAudioPath(): String {
        return FFmpegTestFileManager.basePath() + "/transformAudio.mp3"
    }

    /**
     * 音频剪切用的文件路径
     */
    fun getCutPath(): String {
        return FFmpegTestFileManager.basePath() + "/cutAudio.mp3"
    }

    fun srcAudioFile(): String {
        return FFmpegTestFileManager.audioFileSrc()
    }


}