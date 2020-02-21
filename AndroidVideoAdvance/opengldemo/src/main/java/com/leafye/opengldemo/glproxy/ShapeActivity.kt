package com.leafye.opengldemo.glproxy

import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leafye.opengldemo.R
import com.leafye.opengldemo.glproxy.shape.GLBitmapTest
import com.leafye.opengldemo.view.YeGLSurfaceView
import com.leafye.opengldemo.glproxy.shape.TriangleColor2Test
import kotlinx.android.synthetic.main.activity_shape.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ShapeActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ShapeActivity::class.java))
        }
    }

    private var isRenderer: Boolean = false

    private val glSurfaceView: YeGLSurfaceView by lazy {
        YeGLSurfaceView(this).also {
            glContainer.addView(it)
        }
    }

    private val glTest: GLTest by lazy { TriangleColor2Test() }

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
        setContentView(R.layout.activity_shape)
        if (YeGLSurfaceView.checkOpenGl2(this)) {
            startGl()
        } else {
            Toast.makeText(this, "不支持OpenGL2", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGl() {
        //设置客户端为opengl 2 版本
        glSurfaceView.setVersion2()
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.alpha
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