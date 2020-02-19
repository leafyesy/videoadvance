package com.leafye.opengldemo

import android.app.Application

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.opengldemo
 * @ClassName:      BaseApplication
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/1/22 14:34
 * @UpdateUser:
 * @UpdateDate:     2020/1/22 14:34
 * @UpdateRemark:
 */
class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}