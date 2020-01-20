package com.leafye.opengldemo.glproxy.shape

import android.opengl.GLES20
import com.leafye.opengldemo.glproxy.GLTest
import com.leafye.opengldemo.shape.TriangleColor
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleColorTest : GLTest {

    private val triangleColor by lazy { TriangleColor() }
    override fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?) {
        gl?.glClearColor(1F, 1F, 1F, 1F)
    }

    override fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
    }

    override fun onDrawFrameTest(gl: GL10?) {
        gl?.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        triangleColor.draw()
    }
}