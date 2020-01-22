package com.leafye.opengldemo.shape

import android.opengl.GLES20
import android.util.Log
import com.leafye.opengldemo.R
import com.leafye.opengldemo.utils.OpenGLUtils
import com.leafye.opengldemo.utils.ResUtils

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.opengldemo.shape
 * @ClassName:      TriangleColor2
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/1/22 14:10
 * @UpdateUser:
 * @UpdateDate:     2020/1/22 14:10
 * @UpdateRemark:
 */
class TriangleColor2 : BaseShape() {

    private var positionHandle: Int = 0
    private var colorHandle: Int = 0

    companion object {

        const val TAG = "TriangleColor2"

        private val colorArr = floatArrayOf(
            0.0F, 1.0F, 0.0F, 1.0F,//r
            1.0F, 0.0F, 0.0F, 1.0F,//g
            0.0F, 0.0F, 1.0F, 1.0F//b
        )

        private val vertexPoints = floatArrayOf(
            0.0F, 0.5F, 0.0F,
            -0.5F, -0.5F, 0.0F,
            0.5F, -0.5F, 0.0F
        )

        private const val POSITION_COMPONENT_COUNT = 3
    }

    override fun initOpenGLParam(): MutableList<Int> {
        val vertexShader = OpenGLUtils.loadShader(
            GLES20.GL_VERTEX_SHADER,
            ResUtils.rawRes(R.raw.triangle_color2_vertex_shader).also {
                Log.d(TAG, "vertexShader:$it")
            }
        )
        val fragmentShader = OpenGLUtils.loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            ResUtils.rawRes(R.raw.triangle_color2_fragment_shader).also {
                Log.d(TAG, "fragmentShader:$it")
            }
        )
        return mutableListOf(vertexShader, fragmentShader)
    }

    private val colorFloatBuffer by lazy {
        OpenGLUtils.floatBufferUtil(colorArr)
    }

    private val vertexBuffer by lazy {
        OpenGLUtils.floatBufferUtil(vertexPoints)
    }

    fun draw() {
        GLES20.glUseProgram(program)
        colorHandle = GLES20.glGetAttribLocation(program, "aColor")
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(colorHandle)
        GLES20.glVertexAttribPointer(
            positionHandle,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            12,
            vertexBuffer
        )

        GLES20.glVertexAttribPointer(
            colorHandle,
            4,
            GLES20.GL_FLOAT,
            false,
            0,
            colorFloatBuffer
        )
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
        GLES20.glDisableVertexAttribArray(colorHandle)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }


}