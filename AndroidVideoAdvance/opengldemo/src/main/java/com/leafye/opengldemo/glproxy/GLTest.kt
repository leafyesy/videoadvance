package com.leafye.opengldemo.glproxy

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

interface GLTest {

    fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?)

    fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int)

    fun onDrawFrameTest(gl: GL10?)

}