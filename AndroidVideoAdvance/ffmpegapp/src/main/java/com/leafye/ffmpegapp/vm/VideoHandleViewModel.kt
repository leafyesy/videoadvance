package com.leafye.ffmpegapp.vm

import android.os.Environment
import android.util.Log
import android.view.View
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.FFmpegUtils
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.checkPath
import com.leafye.ffmpegapp.ffmpeg.FFmpegCmd
import com.leafye.ffmpegapp.model.VideoHandleModel

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.vm
 * @ClassName:      VideoHandleViewModel
 * @Description:    VideoHandleViewModel
 * @Author:         leafye
 * @CreateDate:     2020/3/11 13:32
 * @UpdateUser:
 * @UpdateDate:     2020/3/11 13:32
 * @UpdateRemark:
 */
class VideoHandleViewModel(model: VideoHandleModel) : BaseViewModel<VideoHandleModel>(model) {

    companion object {
        private val TAG = "VideoHandleViewModel"
    }

    val btnCodecClick = View.OnClickListener {
        val storagePath = Environment.getExternalStorageDirectory().absolutePath
        val path1 = "$storagePath/input.mp4"
        val path2 = "$storagePath/2.mp4"
        Log.i(TAG, "input path:$path1    output path:$path2")
        YeFFmpeg.instance().decode(path1, path2)
    }
    val btnFilterClick = View.OnClickListener {
        val avfilterinfo = YeFFmpeg.instance().avfilterinfo()
        Log.i(TAG, "filter:$avfilterinfo")
    }
    val btnFormatClick = View.OnClickListener {
        val avformatinfo = YeFFmpeg.instance().avformatinfo()
        Log.i(TAG, "format:$avformatinfo")
    }
    val btnProtocolClick = View.OnClickListener {
        val urlprotocolinfo = YeFFmpeg.instance().urlprotocolinfo()
        Log.i(TAG, "protocol:$urlprotocolinfo")
    }

    val btnTransformClick = View.OnClickListener {
        checkPath(model.srcVideoPath())?.let {
            val transformVideoCmdArr =
                FFmpegUtils.transformVideo(it[0], model.getTransformPath())
            StringBuilder().apply {
                transformVideoCmdArr.forEach { str ->
                    append(str).append(" ")
                }
            }.also { sb ->
                Log.i(TAG, sb.toString())
            }
            FFmpegCmd.excute(FFmpegCmd.CmdTask(transformVideoCmdArr))
        }
    }

    val btnVideoCutClick = View.OnClickListener {
        checkPath(model.srcVideoPath())?.let {
            val cutVideoCmdArr = FFmpegUtils.cutVideo(
                srcFile = it[0],
                startTime = 20,
                duration = 10,
                targetFile = model.getCutVideoPath()
            )
            FFmpegCmd.excute(FFmpegCmd.CmdTask(cutVideoCmdArr))
        }
    }

    val btnScreenShotClick = View.OnClickListener {
        checkPath(model.srcVideoPath())?.let {
            val screenShotCmdArr = FFmpegUtils.screenShot(
                it[0],
                10,
                model.getScreenShotPath()
            )
            FFmpegCmd.excute(FFmpegCmd.CmdTask(screenShotCmdArr))
        }
    }

    val addWaterClick = View.OnClickListener {
        checkPath(model.srcVideoPath())?.let {
            val addWaterMarkCmdArr = FFmpegUtils.addWaterMark(
                srcFile = it[0],
                waterMark = model.getWaterPath(),
                resolution = "720x1280",
                bitRate = 1024,
                targetFile = model.getWaterPathTarget()
            )
            FFmpegUtils.logCmd(addWaterMarkCmdArr)
            FFmpegCmd.excute(FFmpegCmd.CmdTask(addWaterMarkCmdArr))
        }
    }


}

class ViewHandleViewModelProduct(private val model: VideoHandleModel) :
    VMProduct<VideoHandleViewModel>() {
    override fun isThis(classNew: Class<*>): VideoHandleViewModel? = with(classNew) {
        if (isAssignableFrom(VideoHandleViewModel::class.java)) {
            return VideoHandleViewModel(model)
        } else null
    }
}