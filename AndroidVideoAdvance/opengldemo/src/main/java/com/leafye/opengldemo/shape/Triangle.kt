package com.leafye.opengldemo.shape

import android.opengl.GLES20
import com.leafye.opengldemo.utils.OpenGLUtils
import java.nio.FloatBuffer

class Triangle : BaseShape() {


    private val vertexBuffer: FloatBuffer by lazy{
        OpenGLUtils.floatBufferUtil(triangleCoords)
    }


    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4

    private var positionHandle: Int = 0
    private var colorHandle: Int = 0


    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3

        const val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main(){" +
                    " gl_Position = vPosition;" +
                    "}"

        const val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main(){" +
                    " gl_FragColor = vColor;" +
                    "}"

        @JvmStatic
        var triangleCoords = floatArrayOf(// in counterclockwise order:
            0.0f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
        )
    }

    // Set color with red, green, blue and alpha (opacity) values
    private val colorArr = floatArrayOf(255F, 0F, 0F, 1.0F)

    override fun initOpenGLParam(): MutableList<Int> {
        //把要执行的代码片段放入OpenGL中,并获取int(key)
        val vertexShader = OpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = OpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        return mutableListOf<Int>(vertexShader, fragmentShader)
    }


    fun draw() {
        //将程序添加到OpenGL ES环境
        GLES20.glUseProgram(program)
        //获取顶点着色器的位置的句柄
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        //获取片段着色器的颜色句柄
        colorHandle = GLES20.glGetUniformLocation(program, "vColor")

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

        //设置绘制三角形的颜色
        GLES20.glUniform4fv(colorHandle, 1, colorArr, 0)
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        //禁用顶点数组
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

}