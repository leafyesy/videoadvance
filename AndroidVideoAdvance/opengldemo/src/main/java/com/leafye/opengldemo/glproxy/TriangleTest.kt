package com.leafye.opengldemo.glproxy

import android.opengl.GLES20
import com.leafye.opengldemo.shape.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleTest : GLTest {
    private var triangle: Triangle? = null

    override fun onDrawFrameTest(gl: GL10?) {
        gl?.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        triangle?.draw()
    }

    override fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?) {
        gl?.glClearColor(.0F, 1.0F, .0F, .0F)
        triangle = Triangle()
    }
}