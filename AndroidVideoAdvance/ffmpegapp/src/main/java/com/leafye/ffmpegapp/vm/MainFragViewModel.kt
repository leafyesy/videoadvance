package com.leafye.ffmpegapp.vm

import android.os.Environment
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.model.MainFragModel

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.vm
 * @ClassName:      MainFragViewModel
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/9 15:39
 * @UpdateUser:
 * @UpdateDate:     2020/3/9 15:39
 * @UpdateRemark:
 */
class MainFragViewModel(model: MainFragModel) : BaseViewModel<MainFragModel>(model) {

    companion object {
        private val TAG = "MainFragViewModel"
    }

    val enterAudioHandleEvent = MutableLiveData<Int>()

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

    val btnAudioHandle = View.OnClickListener {
        enterAudioHandleEvent.value = ((enterAudioHandleEvent.value ?: 0) + 1)
    }
}

class MainFragViewModelProduct(private val model:MainFragModel): VMProduct<MainFragViewModel>(){
    override fun isThis(classNew: Class<*>): MainFragViewModel? {
        return with(classNew) {
            if (isAssignableFrom(MainFragViewModel::class.java)) {
                MainFragViewModel(model)
            } else null
        }
    }

}