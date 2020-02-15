package com.leafye.opengldemo.recordCamera.camera.cameraImp

import android.graphics.SurfaceTexture

/**
 * Created by leafye on 2020-01-27.
 */
interface ICamera {

    fun getCameraId(id: CameraId): String

    fun openCamera(cameraId: String): Boolean

    fun startPreview()

    fun stopPreview()

    fun setPreviewTexture(texture: SurfaceTexture)

    fun release()

    enum class CameraId {
        FRONT, BACK
    }

}