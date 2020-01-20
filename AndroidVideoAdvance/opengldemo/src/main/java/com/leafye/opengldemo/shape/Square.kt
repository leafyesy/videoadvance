package com.leafye.opengldemo.shape

import android.opengl.GLES20
import com.leafye.opengldemo.utils.OpenGLUtils
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class Square {

    private var vertexBuffer: FloatBuffer? = null
    private var drawListByteBuffer: ByteBuffer? = null
    private val program: Int

    private var positionHandle: Int = 0
    private var colorHandle: Int = 0

    private val vertexStride = COORDS_PER_VERTEX * 4

    companion object {
        //表示每个顶点的个数
        const val COORDS_PER_VERTEX = 3

        val squareCoords = floatArrayOf(
            -.5F, .5F, .0F,
            -.5F, -.5F, .0F,
            .5F, -.5F, .0F,
            .5F, .5F, .0F
        )

        val drawOrderByte = byteArrayOf(0, 1, 2, 0, 2, 3)

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

        private val colorArr = floatArrayOf(255F, 0F, 0F, 1.0F)
    }


    init {
        vertexBuffer = OpenGLUtils.floatBufferUtil(squareCoords)
        drawListByteBuffer = OpenGLUtils.byteBufferUtil(drawOrderByte)
        //----------------这里的代码基本相同
        val vertexShader = OpenGLUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = OpenGLUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        //创建空的OpenGL ES程序
        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        //创建OpenGL ES程序可执行文件
        GLES20.glLinkProgram(program)
        //-----------------------------------------end
    }

    fun draw() {
        GLES20.glUseProgram(program)
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )
        colorHandle = GLES20.glGetUniformLocation(program, "vColor")
        GLES20.glUniform4fv(colorHandle, 1, colorArr, 0)
        //使用Element来绘制正方形 使用的两个三角形拼凑起来
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrderByte.size,
            GLES20.GL_UNSIGNED_BYTE,
            drawListByteBuffer
        )
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

}