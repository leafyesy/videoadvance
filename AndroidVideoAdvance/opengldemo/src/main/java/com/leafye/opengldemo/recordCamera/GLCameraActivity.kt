package com.leafye.opengldemo.recordCamera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.leafye.opengldemo.R
import com.leafye.opengldemo.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_gl_camera.*

/**
 * Created by leafye on 2020-02-04.
 */
class GLCameraActivity : AppCompatActivity() {

    companion object {

        private const val PERMISSION_REQUEST_CODE = 101

        fun start(context: Context) {
            context.startActivity(Intent(context, GLCameraActivity::class.java))
        }
    }

    private var isPermissionRequested: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gl_camera)
        glCameraView.initDevice()
        clickToPreview.setOnClickListener {
            checkPermissionAndOpenCamera()
        }
    }

    private fun checkPermissionAndOpenCamera() {
        PermissionUtils.checkPermissionM(
            this,
            PERMISSION_REQUEST_CODE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            isRequest = !isPermissionRequested
        ).takeIf {
            isPermissionRequested = true
            it
        }?.let {
            glCameraView.prepare()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkPermissionAndOpenCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        glCameraView.release()
    }

}