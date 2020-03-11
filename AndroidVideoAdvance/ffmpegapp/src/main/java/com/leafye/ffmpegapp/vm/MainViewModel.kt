package com.leafye.ffmpegapp.vm

import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.model.MainModel


/**
 * Created by leafye on 2020-03-04.
 */
class MainViewModel(model: MainModel) : BaseViewModel<MainModel>(model) {

    companion object {
        private const val TAG = "MainViewModel"
    }

}

class MainViewModelProduct(private val model: MainModel) :
    VMProduct<MainViewModel>() {
    override fun isThis(classNew: Class<*>): MainViewModel? =
        with(classNew) {
            if (isAssignableFrom(MainViewModel::class.java)) {
                MainViewModel(model)
            } else null
        }
}