package com.leafye.opengldemo.recordCamera

import android.content.Context
import android.content.Intent
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.os.Environment
import android.view.TextureView
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.leafye.opengldemo.R
import kotlinx.android.synthetic.main.activity_record_camera.*

class RecordCameraActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, RecordCameraActivity::class.java))
        }
    }

    private val savePath by lazy {
        Environment.getExternalStorageDirectory().absolutePath + "/leaf/" + System.currentTimeMillis() + ".mp4"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tranStatusBar()
        setContentView(R.layout.activity_shape)
        initData()
        initView()
    }

    private fun initData() {

    }

    private fun initView() {
        textureView.keepScreenOn = true
        textureView.surfaceTextureListener = this
    }

    private fun tranStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//状态栏半透明
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
    }


}