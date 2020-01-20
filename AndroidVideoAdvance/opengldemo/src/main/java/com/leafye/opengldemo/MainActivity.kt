package com.leafye.opengldemo

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leafye.opengldemo.glproxy.*
import com.leafye.opengldemo.glproxy.shape.TriangleColorTest
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

    private val glTest: GLTest by lazy { TriangleColorTest() }

    private val renderer by lazy {
        object : GLSurfaceView.Renderer {

            override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                glTest.onSurfaceCreatedTest(gl, config)
            }

            override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                glTest.onSurfaceChangedTest(gl, width, height)
            }

            override fun onDrawFrame(gl: GL10?) {
                glTest.onDrawFrameTest(gl)
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
        //设置客户端为opengl 2 版本
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(renderer)
        //---mode start-----------------------------------------------------------
        //连续不断的刷新
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
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
    }

    override fun onResume() {
        super.onResume()
        if (isRenderer) {
            glSurfaceView.onResume()
        }
    }
    //---生命周期处理-----------------end

}
