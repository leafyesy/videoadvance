package com.leafye.ffmpegapp.vm

import android.text.TextUtils
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.model.VideoPlayModel
import java.io.File

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.vm
 * @ClassName:      VideoPlayViewModel
 * @Description:    视频播放
 * @Author:         leafye
 * @CreateDate:     2020/3/16 10:16
 * @UpdateUser:
 * @UpdateDate:     2020/3/16 10:16
 * @UpdateRemark:
 */
class VideoPlayViewModel(model: VideoPlayModel) : BaseViewModel<VideoPlayModel>(model) {

    var pathStr = ""
    var enableSelect: Boolean = false
    var enablePlay = false

    fun uiPrepareSuccess() {
        pathStr = "界面加载完成,请选择播放文件"
        enableSelect = true
    }

    fun setSelectVideoPath(path: String) {
        pathStr = path
        enablePlay = TextUtils.isEmpty(path) && File(path).exists()
    }
}

class VideoPlayViewModelProduct(private val model: VideoPlayModel) :
    VMProduct<VideoPlayViewModel>() {
    override fun isThis(classNew: Class<*>) = with(classNew) {
        if (isAssignableFrom(VideoPlayViewModel::class.java)) {
            VideoPlayViewModel(model)
        } else null
    }
}