package com.leafye.opengldemo.shape

import android.opengl.GLES20
import com.leafye.opengldemo.utils.OpenGLUtils
import java.nio.FloatBuffer

class TriangleColor : BaseShape() {

    private val vertexBuffer: FloatBuffer by lazy{
        OpenGLUtils.floatBufferUtil(triangleCoords)
    }
    private val colorBuffer: FloatBuffer by lazy{
        OpenGLUtils.floatBufferUtil(colorArr)
    }

    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4

    private var positionHandle: Int = 0
    private var colorHandleA: Int = 0

    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3

        const val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "varying vec4 vColor;" +
                    "attribute vec4 aColor;" +
                    "void main(){" +
                    " gl_Position = vPosition;" +
                    " vColor=aColor;" +
                    "}"

        const val fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main(){" +
                    " gl_FragColor = vColor;" +
                    "}"

        @JvmStatic
        var triangleCoords = floatArrayOf(// in counterclockwise order:
            0.0f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
        )

        // Set color with red, green, blue and alpha (opacity) values
        @JvmStatic
        private val colorArr = floatArrayOf(
            1F, 0F, 0F, 1F,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f
        )
    }

    override fun initOpenGLParam(): MutableList<Int> {
        val vertexShader = OpenGLUtils.loadShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode
        )
        val fragmentShader = OpenGLUtils.loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode
        )
        return mutableListOf(vertexShader, fragmentShader)
    }

    fun draw() {
        //将程序添加到OpenGL ES环境
        GLES20.glUseProgram(program)
        //获取顶点着色器的位置的句柄
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        //启用三角形顶点位置的句柄
        GLES20.glEnableVertexAttribArray(positionHandle)
        //准备三角形坐标数据
        GLES20.glVertexAttribPointer(
            positionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        //获取片段着色器的颜色句柄
        colorHandleA = GLES20.glGetAttribLocation(program, "aColor")
        //设置绘制三角形的颜色
        GLES20.glEnableVertexAttribArray(colorHandleA)
        GLES20.glVertexAttribPointer(
            colorHandleA,
            4,
            GLES20.GL_FLOAT,
            false,
            0,
            colorBuffer
        )
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        //禁用顶点数组
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}