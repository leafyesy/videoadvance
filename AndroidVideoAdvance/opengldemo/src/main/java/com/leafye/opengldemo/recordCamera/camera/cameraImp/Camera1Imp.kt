package com.leafye.opengldemo.recordCamera.camera.cameraImp

import android.app.Activity
import android.graphics.PixelFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Log
import com.leafye.opengldemo.utils.CameraUtils
import com.leafye.opengldemo.utils.LogObj


/**
 * Created by leafye on 2020-01-27.
 */
class Camera1Imp(private val activity: Activity) :
    ICamera {

    companion object {
        private const val TAG = "Camera1Imp"

        private val SIZE_WIDTH = 1920
        private val SIZE_HEIGHT = 1080
    }

    // 设置缓存帧数据容器，避免重复创建
    private val mPreviewData = ByteArray(SIZE_WIDTH * SIZE_HEIGHT * 3 / 2)

    private var camera: Camera? = null

    private var cameraId = Camera.CameraInfo.CAMERA_FACING_BACK

    override fun openCamera(cameraId: String): Boolean {
        try {
            this.cameraId = cameraId.toInt()
            camera = Camera.open(cameraId.toInt()).apply camera@{
                val tempParameters = this@camera.parameters.apply {
                    set("orientation", "portrait")
                    focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                    pictureFormat = PixelFormat.JPEG
                    setPreviewSize(1920, 1080)
                    val calculateCameraPreviewOrientation =
                        CameraUtils.calculateCameraPreviewOrientation(activity, cameraId.toInt())
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

    private val previewCallback = Camera.PreviewCallback { p0, p1 -> Log.d(TAG, "p0:$p0") }

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

    override fun getCameraId(id: ICamera.CameraId): String {
        return when (id) {
            ICamera.CameraId.FRONT -> Camera.CameraInfo.CAMERA_FACING_FRONT.toString()
            else -> Camera.CameraInfo.CAMERA_FACING_BACK.toString()
        }
    }


}