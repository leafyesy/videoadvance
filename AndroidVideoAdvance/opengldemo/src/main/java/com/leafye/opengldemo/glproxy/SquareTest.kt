package com.leafye.opengldemo.glproxy

import android.opengl.GLES20
import com.leafye.opengldemo.shape.Square
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SquareTest : GLTest {

    private var square: Square? = null

    override fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?) {
        gl?.glClearColor(.0F, .0F, 1.0F, 1.0F)
        square = Square()
    }

    override fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int) {

    }

    override fun onDrawFrameTest(gl: GL10?) {
        gl?.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        square?.draw()
    }
}