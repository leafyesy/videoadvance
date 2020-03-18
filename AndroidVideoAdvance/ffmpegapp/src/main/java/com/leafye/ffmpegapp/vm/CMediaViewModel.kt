package com.leafye.ffmpegapp.vm

import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.base.livedata.CallLiveData
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.model.CMediaModel
import kotlin.concurrent.thread

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.vm
 * @ClassName:      CMediaViewModel
 * @Description:
 * @Author:         leafye
 * @CreateDate:     2020/3/18 16:40
 * @UpdateUser:
 * @UpdateDate:     2020/3/18 16:40
 * @UpdateRemark:
 */
class CMediaViewModel(model: CMediaModel) : BaseViewModel<CMediaModel>(model) {

    val selectCall = CallLiveData()

    val setUpCall = CallLiveData()

    val pathLiveData = MutableLiveData<String>()

    val status = MutableLiveData<String>()

    fun selectClick() {
        selectCall.call()
    }

    fun setSelectVideoPath(path: String) {
        pathLiveData.value = path
    }

    fun setUpClick() {
        setUpCall.call()
    }

    fun playClick() {
        thread {
            YeFFmpeg.instance().playMedia()
        }
    }

    fun releaseClick() {
        thread {
            YeFFmpeg.instance().releaseMedia()
        }
    }

}

class CMediaViewModelProduct(private val model: CMediaModel) : VMProduct<CMediaViewModel>() {
    override fun isThis(classNew: Class<*>): CMediaViewModel? = with(classNew) {
        if (isAssignableFrom(CMediaViewModel::class.java))
            CMediaViewModel(model)
        else null
    }

}