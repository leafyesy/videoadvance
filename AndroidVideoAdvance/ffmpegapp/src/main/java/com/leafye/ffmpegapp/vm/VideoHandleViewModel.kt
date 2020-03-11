package com.leafye.ffmpegapp.vm

import android.os.Environment
import android.util.Log
import android.view.View
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.YeFFmpeg
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



}

class ViewHandleViewModelProduct(private val model: VideoHandleModel) :
    VMProduct<VideoHandleViewModel>() {
    override fun isThis(classNew: Class<*>): VideoHandleViewModel? = with(classNew) {
        if (isAssignableFrom(VideoHandleViewModel::class.java)) {
            return VideoHandleViewModel(model)
        } else null
    }
}