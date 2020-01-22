package com.leafye.opengldemo.glproxy

import android.opengl.GLES10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class EmptyRedTest : GLTest {

    override fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?) {
        gl?.glClearColor(1.0F, 0.0F, 0.0F, 0.0F)
    }

    override fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int) {
        gl?.glViewport(0, 0, width, height)
    }

    override fun onDrawFrameTest(gl: GL10?) {
        gl?.glClear(GLES10.GL_COLOR_BUFFER_BIT)
    }

}