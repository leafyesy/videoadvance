package com.leafye.ffmpegapp

import android.app.Application
import androidx.lifecycle.ViewModel
import com.leafye.base.VMProduct
import com.leafye.base.ViewModelFactory
import com.leafye.base.utils.Utils
import com.leafye.ffmpegapp.model.*
import com.leafye.ffmpegapp.vm.*

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp
 * @ClassName:      BaseApplication
 * @Description:
 * @Author:         leafye
 * @CreateDate:     2020/3/5 15:19
 * @UpdateUser:
 * @UpdateDate:     2020/3/5 15:19
 * @UpdateRemark:
 */
class BaseApplication : Application() {

    private val vmProductList =
        mutableListOf<VMProduct<out ViewModel>>().apply {
            add(MainViewModelProduct(MainModel()))
            add(AudiohandleVmProduct(AudioModel()))
            add(MainFragViewModelProduct(MainFragModel()))
            add(ViewHandleViewModelProduct(VideoHandleModel()))
            add(VideoPlayViewModelProduct(VideoPlayModel()))
        }


    override fun onCreate() {
        super.onCreate()
        //先初始化Context
        Utils.initConfig(this)
        //初始化所有的VM product
        ViewModelFactory.instance().addAll(vmProductList)
    }

}