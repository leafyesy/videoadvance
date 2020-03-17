package com.leafye.ffmpegapp.vm

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.*
import com.leafye.ffmpegapp.ffmpeg.FFmpegCmd
import com.leafye.ffmpegapp.ffmpeg.Result
import com.leafye.ffmpegapp.ffmpeg.ResultCallback
import com.leafye.ffmpegapp.model.VideoHandleModel
import org.jetbrains.anko.runOnUiThread

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
        private const val TAG = "VideoHandleViewModel"
    }

    val resultLiveData = MutableLiveData<Result>()

    fun btnClick(view: View) {
        if (!checkStatus()) return
        when (view.id) {
            R.id.btnProtocol -> btnProtocolClick()
            R.id.btnFormat -> btnFormatClick()
            R.id.btnCodec -> btnCodecClick()
            R.id.btnFilter -> btnFilterClick()
            R.id.transform -> btnTransformClick()
            R.id.btnVideoCut -> btnVideoCutClick()
            R.id.btnScreenShot -> btnScreenShotClick()
            R.id.addWater -> addWaterClick()
            R.id.generateGif -> generateGifClick()
            R.id.recordScreen -> recordScreenClick()
            R.id.combineVideo -> combineVideoClick()
            R.id.multiVideo -> multiVideoClick()
            R.id.reverseVideo -> reverseVideoClick()
            R.id.denoiseVideo -> denoiseVideoClick()
            R.id.toImageVideo -> toImageVideoClick()
            R.id.pipVideo -> pipVideoClick()
            R.id.movVideo -> movVideoClick()
        }
    }

    private fun btnCodecClick() {
        checkPath(model.srcVideoPath())?.let {
            YeFFmpeg.instance().decode(it[0], model.getCodecPath())
        }
    }

    private fun btnFilterClick() {
        val avfilterinfo = YeFFmpeg.instance().avfilterinfo()
        Log.i(TAG, "filter:$avfilterinfo")
    }

    private fun btnFormatClick() {
        val avformatinfo = YeFFmpeg.instance().avformatinfo()
        Log.i(TAG, "format:$avformatinfo")
    }

    private fun btnProtocolClick() {
        val urlprotocolinfo = YeFFmpeg.instance().urlprotocolinfo()
        Log.i(TAG, "protocol:$urlprotocolinfo")
    }

    private fun btnTransformClick() {
        if (!checkStatus()) return
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
            FFmpegCmd.excute(FFmpegCmd.CmdTask(transformVideoCmdArr, excuteCallback))
        }
    }

    private fun btnVideoCutClick() {
        checkPath(model.srcVideoPath())?.let {
            val cutVideoCmdArr = FFmpegUtils.cutVideo(
                srcFile = it[0],
                startTime = 20,
                duration = 10,
                targetFile = model.getCutVideoPath()
            )
            FFmpegUtils.logCmd(cutVideoCmdArr)
            FFmpegCmd.excute(FFmpegCmd.CmdTask(cutVideoCmdArr, excuteCallback))
        }
    }

    private fun btnScreenShotClick() {
        checkPath(model.srcVideoPath())?.let {
            val screenShotCmdArr = FFmpegUtils.screenShot(
                it[0],
                10,
                model.getScreenShotPath()
            )
            FFmpegCmd.excute(FFmpegCmd.CmdTask(screenShotCmdArr, excuteCallback))
        }
    }

    private fun addWaterClick() {
        checkPath(model.srcVideoPath())?.let {
            val addWaterMarkCmdArr = FFmpegUtils.addWaterMark(
                srcFile = it[0],
                waterMark = model.getWaterPath(),
                resolution = "720x1280",
                bitRate = 1024,
                targetFile = model.getWaterPathTarget()
            )
            FFmpegUtils.logCmd(addWaterMarkCmdArr)
            FFmpegCmd.excute(FFmpegCmd.CmdTask(addWaterMarkCmdArr, excuteCallback))
        }
    }

    private fun generateGifClick() {
        checkPath(model.srcVideoPath())?.let {
            val generateGifCmdArr =
                FFmpegUtils.generateGif(it[0], 20, 10, "720X1280", 20, model.getGitPathTarget())
            FFmpegCmd.excute(FFmpegCmd.CmdTask(generateGifCmdArr, excuteCallback))
        }
    }

    private fun recordScreenClick() {
        val screenRecordCmdArr = FFmpegUtils.screenRecord("720x1280", 20, model.getScreenRecord())
        FFmpegCmd.excute(FFmpegCmd.CmdTask(screenRecordCmdArr, excuteCallback))
    }

    private fun combineVideoClick() {
        FFmpegUtils
    }

    private fun multiVideoClick() {
        checkPath(model.srcVideoPath(), model.srcVideoPath2())?.let {
            val multiVideoCmdArr = FFmpegUtils.multiVideo(
                input1 = it[0],
                input2 = it[1],
                targetFile = model.multiPath(),
                videoLayout = VideoLayout.LAYOUT_HORIZONTAL
            )
            FFmpegCmd.excute(FFmpegCmd.CmdTask(multiVideoCmdArr, excuteCallback))
        }
    }

    private fun reverseVideoClick() {
        checkPath(model.srcVideoPath())?.let {
            val reverseVideoCmdArr = FFmpegUtils.reverseVideo(it[0], model.reversePath())
            FFmpegCmd.excute(FFmpegCmd.CmdTask(reverseVideoCmdArr, excuteCallback))
        }
    }

    private fun denoiseVideoClick() {
        checkPath(model.srcVideoPath())?.let {
            val denoiseVideoCmdArr = FFmpegUtils.denoiseVideo(it[0], model.denoisePath())
            FFmpegCmd.excute(FFmpegCmd.CmdTask(denoiseVideoCmdArr, excuteCallback))
        }
    }

    private fun toImageVideoClick() {
        checkPath(model.srcVideoPath())?.let {
            val videoToImageCmdArr =
                FFmpegUtils.videoToImage(
                    inputFile = it[0],
                    startTime = 10,
                    duration = 1,
                    frameRate = 5,
                    targetFile = model.toImagePathPrefix()
                )
            FFmpegCmd.excute(FFmpegCmd.CmdTask(videoToImageCmdArr, excuteCallback))
        }
    }

    private fun pipVideoClick() {
        checkPath(model.srcVideoPath(), model.srcVideoPath2())?.let {
            //x、y坐标点需要根据全屏视频与小视频大小，进行计算
            //比如：全屏视频为320x240，小视频为120x90，那么x=200 y=150
            val x = 200
            val y = 150
            val picInPicVideoCmdArr =
                FFmpegUtils.picInPicVideo(it[0], it[1], x, y, model.pipPath())
            FFmpegCmd.excute(FFmpegCmd.CmdTask(picInPicVideoCmdArr, excuteCallback))
        }
    }

    private fun movVideoClick() {

    }

    fun changeResult(result: Result) {
        getApplication<BaseApplication>().runOnUiThread {
            resultLiveData.value = result
        }
    }

    private fun checkStatus(): Boolean {
        resultLiveData.value?.type?.let { type ->
            return (type != Result.Type.BEGIN && type != Result.Type.PROGRESS).also {
                if (!it) Toast.makeText(getApplication(), "excuting", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private val excuteCallback = object : ResultCallback {
        override fun begin(result: Result) {
            changeResult(result)
        }

        override fun progress(result: Result) {
            changeResult(result)
        }

        override fun end(result: Result) {
            changeResult(result)
        }

        override fun error(result: Result) {
            changeResult(result)
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