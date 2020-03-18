package com.leafye.ffmpegapp.vm

import android.app.Application
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.base.livedata.CallLiveData
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.model.VideoPlayModel

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

    companion object {
        private const val DEF_STR = "界面加载完成,请选择播放文件"
    }

    var pathStr = MutableLiveData<String>()

    val playEvent = CallLiveData()

    fun setSelectVideoPath(path: String) {
        pathStr.value = path
    }

    fun playClick() {
        if (TextUtils.isEmpty(pathStr.value) || pathStr.value == DEF_STR) {
            Toast.makeText(getApplication(), "请选择播放文件", Toast.LENGTH_SHORT).show()
            return
        }
        playEvent.call()
    }

    fun pauseClick() {
        YeFFmpeg.instance().videoPause()
    }

    fun resumeClick() {
        YeFFmpeg.instance().videoResume()
    }

    fun stopClick() {
        YeFFmpeg.instance().videoStop()
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