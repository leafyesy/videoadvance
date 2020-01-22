package com.leafye.opengldemo.glproxy.shape

import android.opengl.GLES20
import com.leafye.opengldemo.glproxy.GLTest
import com.leafye.opengldemo.shape.TriangleColor2
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.opengldemo.glproxy.shape
 * @ClassName:      TriangleColor2
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/1/22 15:07
 * @UpdateUser:
 * @UpdateDate:     2020/1/22 15:07
 * @UpdateRemark:
 */
class TriangleColor2Test : GLTest {

    private val glTriangleColor2 by lazy {
        TriangleColor2()
    }

    override fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.5F, 0.5F, 0.5F, 0.5F)
    }

    override fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrameTest(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        glTriangleColor2.draw()
    }
}