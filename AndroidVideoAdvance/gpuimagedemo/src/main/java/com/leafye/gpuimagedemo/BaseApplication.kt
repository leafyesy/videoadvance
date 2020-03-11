package com.leafye.gpuimagedemo

import android.app.Application
import androidx.lifecycle.ViewModel
import com.leafye.base.VMProduct
import com.leafye.base.ViewModelFactory
import com.leafye.base.utils.Utils
import com.leafye.gpuimagedemo.model.MainModel
import com.leafye.gpuimagedemo.vm.MainViewModelProduct

/**
 * Created by leafye on 2020/3/11.
 */
class BaseApplication : Application() {

    private val vmProductList =
        mutableListOf<VMProduct<out ViewModel>>().apply {
            add(MainViewModelProduct(MainModel()))
        }

    override fun onCreate() {
        super.onCreate()
        //先初始化Context
        Utils.initConfig(this)
        ViewModelFactory.instance().addAll(vmProductList)
    }

}