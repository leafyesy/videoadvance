package com.leafye.opengldemo.recordCamera.camera.cameraImp

import android.app.Activity
import android.graphics.PixelFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import com.leafye.opengldemo.recordCamera.camera.ICamera
import com.leafye.opengldemo.recordCamera.camera.IHolder
import com.leafye.opengldemo.utils.CameraUtils
import com.leafye.opengldemo.utils.LogObj


/**
 * Created by leafye on 2020-01-27.
 */
class Camera1Imp(private val activity: Activity) : ICamera {

    companion object {
        private const val TAG = "Camera1Imp"

        private val SIZE_WIDTH = 1920
        private val SIZE_HEIGHT = 1080
    }

    // 设置缓存帧数据容器，避免重复创建
    private val mPreviewData = ByteArray(SIZE_WIDTH * SIZE_HEIGHT * 3 / 2)

    private var holder: IHolder? = null
    private var camera: Camera? = null

    private var cameraId = Camera.CameraInfo.CAMERA_FACING_BACK

    override fun setHolder(holder: IHolder) {
        this.holder = holder
    }

    override fun openCamera(cameraId: Int): Boolean {
        try {
            this.cameraId = cameraId
            camera = Camera.open(cameraId).apply camera@{
                val tempParameters = this@camera.parameters.apply {
                    set("orientation", "portrait")
                    focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                    pictureFormat = PixelFormat.JPEG
                    setPreviewSize(1920, 1080)
                    val calculateCameraPreviewOrientation =
                        CameraUtils.calculateCameraPreviewOrientation(activity, cameraId)
                    setDisplayOrientation(calculateCameraPreviewOrientation)
                }
                this@camera.parameters = tempParameters
                this@camera.setPreviewCallbackWithBuffer(previewCallback)
                this@camera.addCallbackBuffer(mPreviewData)
                this@camera.startPreview()
                Log.i(TAG, "open camera")
            }
        } catch (e: Exception) {
            LogObj.logException(TAG, e)
            return false
        }
        return true
    }

    private val previewCallback = object : Camera.PreviewCallback {
        override fun onPreviewFrame(p0: ByteArray?, p1: Camera?) {
            Log.d(TAG, "p0:$p0")
        }
    }

    override fun startPreview() {
        check(camera != null) { "camera is null,please openCamera first" }
        stopPreview()
        release()
        camera?.startPreview()
    }

    override fun stopPreview() {
        camera?.stopPreview()
    }

    override fun setPreviewTexture(texture: SurfaceTexture) {
        check(camera != null) { "camera is null,please openCamera first" }
        camera?.let {
            try {
                it.setPreviewTexture(texture)
            } catch (e: java.lang.Exception) {
                LogObj.logException(TAG, e)
            }
        }
    }

    override fun release() {
        camera?.release()
        camera = null
    }

    override fun getCameraId(id: ICamera.CameraId): Int {
        return when (id) {
            ICamera.CameraId.FRONT -> Camera.CameraInfo.CAMERA_FACING_FRONT
            else -> Camera.CameraInfo.CAMERA_FACING_BACK
        }
    }

    fun setCameraDisplayOrientation(
        activity: Activity,
        cameraId: Int, camera: Camera
    ) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay
            .rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360
        }
        camera.setDisplayOrientation(result)
    }


}