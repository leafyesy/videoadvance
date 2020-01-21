package com.leafye.opengldemo.glproxy.shape

import android.content.Context
import android.opengl.GLES20
import com.leafye.opengldemo.glproxy.GLTest
import com.leafye.opengldemo.shape.GLBitmap
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.opengldemo.glproxy.shape
 * @ClassName:      GLBitmapTest
 * @Description:    java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/1/21 16:48
 * @UpdateUser:
 * @UpdateDate:     2020/1/21 16:48
 * @UpdateRemark:
 */
class GLBitmapTest(private val context: Context) : GLTest {

    private val glBitmap: GLBitmap by lazy {
        GLBitmap()
    }

    private var frameSeq: Int = 0
    private var cWidth: Int = 0
    private var cHeight: Int = 0
    private var viewportOffset = 0
    private var maxOffset = 400


    override fun onSurfaceCreatedTest(gl: GL10?, config: EGLConfig?) {
        gl?.let {
            gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f) // Black Background
            glBitmap.loadGLTexture(context)
        }

    }

    override fun onSurfaceChangedTest(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        glBitmap.changeMvpMatrixInside(width, height)
    }

    override fun onDrawFrameTest(gl: GL10?) {
        gl?.let {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            glBitmap.draw()
        }
    }

    /**
     * 通过改变gl的视角获取
     *
     * @param gl
     */
    private fun changeGLViewport(gl: GL10) {
        frameSeq++
        viewportOffset++
        if (frameSeq % 100 == 0) {// 每隔100帧，重置
            gl.glViewport(0, 0, cWidth, cHeight)
            viewportOffset = 0
        } else {
            val k = 4
            gl.glViewport(
                -maxOffset + viewportOffset * k,
                -maxOffset + viewportOffset * k,
                this.cWidth - viewportOffset * 2 * k + maxOffset * 2,
                this.cHeight - viewportOffset * 2 * k + maxOffset * 2
            )
        }
    }
}