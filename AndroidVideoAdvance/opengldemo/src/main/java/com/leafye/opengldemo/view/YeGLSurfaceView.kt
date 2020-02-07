package com.leafye.opengldemo.view

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Created by leafye on 2020-01-27.
 */
class YeGLSurfaceView : GLSurfaceView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var isRender: Boolean = false

    fun setVersion2() {
        if (checkOpenGl2(context)) {
            setEGLContextClientVersion(2)
        } else {
            throw IllegalStateException("device no support gl version 2 !!!")
        }
    }

    override fun setRenderer(renderer: Renderer?) {
        super.setRenderer(renderer)
        isRender = renderer != null
    }

    fun isRender(): Boolean {
        return isRender
    }

    companion object {
        fun checkOpenGl2(context: Context): Boolean {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val configurationInfo = activityManager.deviceConfigurationInfo
            return configurationInfo.reqGlEsVersion >= 0x20000
//                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
//                && (Build.FINGERPRINT.startsWith("generic")
//                || Build.FINGERPRINT.startsWith("unknown")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.MODEL.contains("Android SDK built for x86")))//支持模拟器
        }
    }

}