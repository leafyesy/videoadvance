package com.leafye.opengldemo.recordCamera.camera

import android.graphics.SurfaceTexture

/**
 * Created by leafye on 2020-01-27.
 */
interface ICamera {

    fun setHolder(holder: IHolder)

    fun getCameraId(id: CameraId): Int

    fun openCamera(cameraId: Int): Boolean

    fun startPreview()

    fun stopPreview()

    fun setPreviewTexture(texture: SurfaceTexture)

    fun release()

    enum class CameraId {
        FRONT, BACK
    }

}