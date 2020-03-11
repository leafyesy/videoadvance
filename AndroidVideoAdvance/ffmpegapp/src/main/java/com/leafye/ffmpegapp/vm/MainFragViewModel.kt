package com.leafye.ffmpegapp.vm

import android.view.View
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.base.livedata.CallLiveData
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

    val enterAudioHandleEvent = CallLiveData()

    val enterVideoHandleEvent = CallLiveData()

    fun btnAudioHandle(view:View) {
        enterAudioHandleEvent.call()
    }

    fun btnVideoHandle(view:View) {
        enterVideoHandleEvent.call()
    }
}

class MainFragViewModelProduct(private val model: MainFragModel) : VMProduct<MainFragViewModel>() {
    override fun isThis(classNew: Class<*>): MainFragViewModel? = with(classNew) {
        if (isAssignableFrom(MainFragViewModel::class.java)) {
            MainFragViewModel(model)
        } else null
    }

}