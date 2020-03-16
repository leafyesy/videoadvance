package com.leafye.ffmpegapp.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.view
 * @ClassName:      VideoSurfaceView
 * @Description:    视频播放SurfaceView
 * @Author:         leafye
 * @CreateDate:     2020/3/16 10:23
 * @UpdateUser:
 * @UpdateDate:     2020/3/16 10:23
 * @UpdateRemark:
 */
class VideoSurfaceView : SurfaceView, SurfaceHolder.Callback2 {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        holder.addCallback(this)
    }

    companion object {
        private const val TAG = "VideoSurfaceView"
    }

    var surface: Surface? = null
        private set

    var callback: Callback? = null

    override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
        Log.d(TAG, "surfaceRedrawNeeded")
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surfaceChanged format:${format} width:${width} height:${height}")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.d(TAG, "surfaceDestroyed")
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.d(TAG, "surfaceCreated")
        surface = holder?.surface?.also {
            callback?.surfaceCreated(it)
        }
    }

    interface Callback {
        fun surfaceCreated(surface: Surface)
    }

}