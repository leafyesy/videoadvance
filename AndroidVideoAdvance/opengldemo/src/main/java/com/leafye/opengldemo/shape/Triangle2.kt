package com.leafye.opengldemo.shape

import android.opengl.GLES20.*
import com.leafye.opengldemo.utils.OpenGLUtils
import java.nio.FloatBuffer

class Triangle2 :BaseShape(){

    private val vertexBuffer: FloatBuffer by lazy {
        OpenGLUtils.floatBufferUtil(triangleCoords)
    }
    private val vertexBuffer2: FloatBuffer by lazy{
        OpenGLUtils.floatBufferUtil(triangleCoords2)
    }

    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4

    private var positionHandle: Int = 0
    private var colorHandle: Int = 0
    private var mvpMatrixHandle: Int = 0


    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3

        const val vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "gl_Position = uMVPMatrix * vPosition;" +
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

        @JvmStatic
        var triangleCoords2 = floatArrayOf(// in counterclockwise order:
            0.0f, 0.3f, 0.3f, // top
            -0.3f, -0.3f, 0.0f, // bottom left
            0.3f, -0.3f, -0.3f  // bottom right
        )
    }

    // Set color with red, green, blue and alpha (opacity) values
    private val colorArr = floatArrayOf(255F, 0F, 0F, 1.0F)
    private val colorArr2 = floatArrayOf(0F, 225F, 0F, 1.0F)

    override fun initOpenGLParam(): MutableList<Int> {
        //把要执行的代码片段放入OpenGL中,并获取int(key)
        val vertexShader = OpenGLUtils.loadShader(GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = OpenGLUtils.loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode)
        return mutableListOf(vertexShader,fragmentShader)
    }


    fun draw(mvpMatrix: FloatArray) {
        //将程序添加到OpenGL ES环境
        glUseProgram(program)
        //获取顶点着色器的位置的句柄
        positionHandle = glGetAttribLocation(program, "vPosition")
        mvpMatrixHandle = glGetUniformLocation(program, "uMVPMatrix")
        //获取片段着色器的颜色句柄
        colorHandle = glGetUniformLocation(program, "vColor")

        //-------------------------------------------------------------准备工作
        glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        //启用三角形顶点位置的句柄
        glEnableVertexAttribArray(positionHandle)
        //准备三角形坐标数据
        glVertexAttribPointer(
            positionHandle,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )
        //设置绘制三角形的颜色
        glUniform4fv(colorHandle, 1, colorArr, 0)
        //-------------------------------------------------------------准备工作
        //-------------------------------------------------------------开始绘制
        //绘制三角形
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)


        glVertexAttribPointer(
            positionHandle,
            COORDS_PER_VERTEX,
            GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer2
        )
        glUniform4fv(colorHandle, 1, colorArr2, 0)
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)

        //禁用顶点数组
        glDisableVertexAttribArray(positionHandle)
        //-------------------------------------------------------------开始绘制
    }

}