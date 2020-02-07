package com.leafye.opengldemo.recordCamera.camera.cameraImp

import android.content.Context
import android.opengl.GLSurfaceView
import com.leafye.opengldemo.recordCamera.camera.CameraV1GLRenderer
import com.leafye.opengldemo.recordCamera.camera.ICamera


/**
 * Created by leafye on 2020-02-05.
 */
class CameraV1GLSurfaceView(context: Context) : GLSurfaceView(context) {
    private var mRenderer: CameraV1GLRenderer? = null
    private var textureId = -1

    fun init(camera: ICamera, isPreviewStarted: Boolean, context: Context) {
        setEGLContextClientVersion(2)
        mRenderer = CameraV1GLRenderer(context).also {
            it.init(this, camera, isPreviewStarted)
            setRenderer(it)
        }
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun deinit() {
        mRenderer?.deinit()
        mRenderer = null
        textureId = -1
    }

}