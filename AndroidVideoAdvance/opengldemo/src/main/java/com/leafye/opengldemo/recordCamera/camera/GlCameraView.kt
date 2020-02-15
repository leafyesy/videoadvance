package com.leafye.opengldemo.recordCamera.camera

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.leafye.opengldemo.R
import com.leafye.opengldemo.recordCamera.camera.cameraImp.Camera2Imp
import com.leafye.opengldemo.recordCamera.camera.cameraImp.ICamera
import kotlinx.android.synthetic.main.view_gl_camera.view.*

/**
 * Created by leafye on 2020-01-27.
 */
class GlCameraView(context: Context, set: AttributeSet? = null) :
    ConstraintLayout(context, set) {

    companion object {
        private const val TAG = "GlCameraView"
    }

    private var glSurfaceView: GLSurfaceView? = null

    private var glSurfaceViewCamera: CameraV1GLSurfaceView? = null

    private var surfaceViewHolder: SurfaceHolder? = null

    private var camera: ICamera? = null

    init {
        View.inflate(context, R.layout.view_gl_camera, this)
    }

    /**
     * 初始化设备数据
     * 相机
     * opengl
     * 屏幕属性
     */
    fun initDevice() {

    }

    /**
     * 准备工作,开启camera数据获取
     */
    @Synchronized
    fun prepare() {
        if (camera != null && glSurfaceView != null) {
            Log.w(TAG, "camera已经初始化!!!")
            return
        }
        camera = Camera2Imp(context as Activity).apply camera@{
            val cameraId = getCameraId(ICamera.CameraId.BACK)
            if (!openCamera(cameraId)) {
                Log.e(TAG, "相机启动失败")
                return
            }
            glSurfaceViewCamera = CameraV1GLSurfaceView(
                context
            ).apply glView@{
                this@GlCameraView.glSurfaceViewContainer.removeAllViews()
                this@GlCameraView.glSurfaceViewContainer.addView(this@glView)
                init(this@camera, true, context)
            }
        }
    }

    /**
     * 开始
     */
    fun start() {
        checkDevice()
        camera?.startPreview()
    }

    fun release() {
        camera?.stopPreview()
        camera?.release()
        glSurfaceViewCamera?.deinit()
    }

    @Throws(IllegalStateException::class)
    fun checkDevice() {
        check(camera == null) { "camera is null!!" }
        check(glSurfaceView == null) { "gl surface view is null!!" }
        check(surfaceViewHolder == null) { "surfaceView holder is null!!" }
    }

}