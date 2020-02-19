package com.leafye.opengldemo.recordCamera.camera.cameraImp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.util.Size
import android.view.Surface
import androidx.annotation.RequiresApi
import com.leafye.opengldemo.utils.LogObj
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

/**
 * Created by leafye on 2020-02-08.
 */
class Camera2Imp(private val activity: Activity) : ICamera {

    companion object {
        private const val TAG = "Camera2Imp"

        private const val EVENT_START_PREVIEW = 1001
    }

    private var previewSize: Size? = null

    private var cameraDevice: CameraDevice? = null

    private var surfaceTexture: SurfaceTexture? = null

    private var cameraThread: HandlerThread? = null
    private var cameraHandler: Handler? = null

    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var captureRequest: CaptureRequest? = null
    private var cameraCaptureSession: CameraCaptureSession? = null

    private fun startCameraThread() {
        cameraThread = HandlerThread("camera_thread").apply {
            start()
            cameraHandler = object : Handler(looper) {
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    when (msg.what) {
                        EVENT_START_PREVIEW -> {
                            startPreview()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(21)
    private fun getCameraId(id: ICamera.CameraId, width: Int, height: Int): String {
        var curCameraId = ""
        val cameraService = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraService.cameraIdList.forEach { cameraId ->
                cameraService.getCameraCharacteristics(cameraId).takeIf {
                    if (id == ICamera.CameraId.FRONT) {
                        it.get(CameraCharacteristics.LENS_FACING) != CameraCharacteristics.LENS_FACING_BACK
                    } else {
                        it.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
                    }
                }?.let {
                    val map = it.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    previewSize = getOptimalSize(
                        map!!.getOutputSizes(SurfaceTexture::class.java),
                        width,
                        height
                    )
                    curCameraId = cameraId
                }
            }
        } catch (e: CameraAccessException) {
            LogObj.logException(TAG, e)
        }
        return curCameraId
    }

    @RequiresApi(21)
    private fun getOptimalSize(sizeMap: Array<Size>, width: Int, height: Int): Size {
        val list = ArrayList<Size>()
        sizeMap.forEach { option ->
            if (width > height) {
                if (option.width > width && option.height > height) {
                    list.add(option)
                }
            } else {
                if (option.width > height && option.height > width) {
                    list.add(option)
                }
            }
        }
        if (list.size > 0) {
            val comparator = Comparator<Size> { lhs, rhs ->
                java.lang.Long.signum(lhs.width * lhs.height.toLong() - rhs.width * rhs.height)
            }
            return Collections.min(list, comparator)
        }
        return sizeMap[0]
    }

    @RequiresApi(21)
    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(p0: CameraDevice) {
            cameraDevice = p0
        }

        override fun onDisconnected(p0: CameraDevice) {
            cameraDevice = null
        }

        override fun onError(p0: CameraDevice, p1: Int) {
            p0.close()
            cameraDevice = null
        }

    }

    @RequiresApi(21)
    override fun getCameraId(id: ICamera.CameraId): String {
        return getCameraId(id, 1080, 1920)
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(21)
    override fun openCamera(cameraId: String): Boolean {
        val cameraService = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (TextUtils.isEmpty(cameraId)) {
                Log.e(TAG, "camera id is not init!!!")
                return false
            }
            if (cameraHandler == null) {
                startCameraThread()
            }
            if (cameraHandler == null) {
                Log.e(TAG, "camera handler not init!!!")
                return false
            }
            cameraService.openCamera(cameraId, stateCallback, cameraHandler)
        } catch (e: CameraAccessException) {
            LogObj.logException(TAG, e)
        }
        return true
    }

    @RequiresApi(21)
    override fun startPreview() {
        check(this.surfaceTexture != null) { "surfaceTexture is null,please init !!!" }
        check(previewSize != null) { "previewSize is null,please init !!!" }
        this.surfaceTexture?.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
        val surface = Surface(this.surfaceTexture!!)
        try {
            cameraDevice?.let {
                captureRequestBuilder =
                    it.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                        addTarget(surface)
                    }
                it.createCaptureSession(
                    listOf(surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigureFailed(p0: CameraCaptureSession) {

                        }

                        override fun onConfigured(p0: CameraCaptureSession) {
                            try {
                                captureRequest = captureRequestBuilder?.build()?.apply request@{
                                    cameraCaptureSession = p0.apply {
                                        setRepeatingRequest(this@request, null, cameraHandler)
                                    }
                                }
                            } catch (e: Exception) {
                                LogObj.logException(TAG, e)
                            }
                        }
                    },
                    cameraHandler
                )
            }
        } catch (e: Exception) {
            LogObj.logException(TAG, e)
        }
    }

    override fun stopPreview() {
        cameraDevice?.close()
    }

    override fun setPreviewTexture(texture: SurfaceTexture) {
        this.surfaceTexture = texture
        cameraHandler?.sendEmptyMessage(EVENT_START_PREVIEW)
    }

    override fun release() {
        cameraCaptureSession?.close()
        cameraCaptureSession = null
        cameraDevice?.close()
        cameraDevice = null
    }
}