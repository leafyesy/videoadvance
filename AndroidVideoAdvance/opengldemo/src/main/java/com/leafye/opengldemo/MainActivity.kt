package com.leafye.opengldemo

import android.app.ActivityManager
import android.content.Context
import android.opengl.EGL14
import android.opengl.GLES10.GL_COLOR_BUFFER_BIT
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity() {

    private var isRenderer: Boolean = false

    private val glSurfaceView: GLSurfaceView by lazy {
        GLSurfaceView(this).also {
            glContainer.addView(it)
        }
    }

    private val renderer by lazy {
        object : GLSurfaceView.Renderer {
            override fun onDrawFrame(gl: GL10?) {
                gl?.glClear(GL_COLOR_BUFFER_BIT)
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                gl?.glViewport(0, 0, width, height)
            }

            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                //红色表示
                gl?.glClearColor(1.0F, 0.0F, 0.0F, 0.0F)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkOpenGl2()) {
            startGl()
        } else {
            Toast.makeText(this, "不支持OpenGL2", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkOpenGl2(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
//                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
//                && (Build.FINGERPRINT.startsWith("generic")
//                || Build.FINGERPRINT.startsWith("unknown")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.MODEL.contains("Android SDK built for x86")))//支持模拟器
    }

    private fun startGl() {
<<<<<<< HEAD
        val eglGetDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val majorVersion = IntArray(1)
        val minorVersion = IntArray(1)
        EGL14.eglInitialize(eglGetDisplay, majorVersion, 1, minorVersion, 1)
=======
        //设置客户端为opengl 2 版本
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(renderer)
        //---mode start-----------------------------------------------------------
        //连续不断的刷新
        //glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        //按请求刷新
        //glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        //---mode end----------------------------------------------------------



        isRenderer = true
    }

    //start---生命周期处理-----------------
    override fun onPause() {
        super.onPause()
        if (isRenderer) {
            glSurfaceView.onPause()
        }
>>>>>>> f92d2cc1906ed7a9559edeb08691ed5562261236
    }

    override fun onResume() {
        super.onResume()
        if (isRenderer) {
            glSurfaceView.onResume()
        }
    }
    //---生命周期处理-----------------end

}
