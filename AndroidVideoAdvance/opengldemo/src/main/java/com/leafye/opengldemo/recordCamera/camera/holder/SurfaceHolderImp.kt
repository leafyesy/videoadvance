package com.leafye.opengldemo.recordCamera.camera.holder

import android.view.SurfaceHolder
import com.leafye.opengldemo.recordCamera.camera.IHolder

/**
 * Created by leafye on 2020-01-27.
 */
class SurfaceHolderImp : IHolder, SurfaceHolder.Callback2 {
    override fun surfaceRedrawNeeded(p0: SurfaceHolder?) {

    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {

    }

    override fun create(holder: IHolder) {

    }

    override fun change(holder: IHolder) {
    }

    override fun destroy(holder: IHolder) {
    }
}