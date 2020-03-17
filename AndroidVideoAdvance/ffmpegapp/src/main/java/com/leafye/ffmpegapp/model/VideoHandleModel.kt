package com.leafye.ffmpegapp.model

import android.util.Log
import com.leafye.base.BaseModel
import com.leafye.ffmpegapp.utils.FFmpegTestFileManager

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.model
 * @ClassName:      VideoHandleModel
 * @Description:    为ViewHandle提供数据
 * @Author:         leafye
 * @CreateDate:     2020/3/11 13:32
 * @UpdateUser:
 * @UpdateDate:     2020/3/11 13:32
 * @UpdateRemark:
 */
class VideoHandleModel : BaseModel {

    companion object {
        private const val TAG = "VideoHandleModel"
    }

    fun srcVideoPath() =
        (FFmpegTestFileManager.basePath() + "/input.mp4").also {
            Log.i(TAG, "srcVideoPath:$it")
        }

    fun getCodecPath() =
        FFmpegTestFileManager.basePath() + "/codec.mp4"

    fun srcVideoPath2() =
        FFmpegTestFileManager.basePath() + "/input2.mp4"

    fun getTransformPath() =
        FFmpegTestFileManager.basePath() + "/input_tran.flv"

    fun getCutVideoPath() =
        FFmpegTestFileManager.basePath() + "/cut.mp4"

    fun getScreenShotPath() =
        FFmpegTestFileManager.basePath() + "/screen_shot.jpeg"

    fun getWaterPath() =
        FFmpegTestFileManager.basePath() + "/water.jpg"

    fun getWaterPathTarget() =
        FFmpegTestFileManager.basePath() + "/water.mp4"

    fun getGitPathTarget() =
        FFmpegTestFileManager.basePath() + "/gif.gif"

    fun getScreenRecord() =
        FFmpegTestFileManager.basePath() + "/record.mp4"

    fun multiPath() =
        FFmpegTestFileManager.basePath() + "/multi.mp4"

    fun reversePath() =
        FFmpegTestFileManager.basePath() + "/reverse.mp4"

    fun denoisePath() =
        FFmpegTestFileManager.basePath() + "/denoise.mp4"

    fun toImagePathPrefix() =
        FFmpegTestFileManager.basePath() + "/toImage"

    fun pipPath() =
        FFmpegTestFileManager.basePath() + "/pip.mp4"
}