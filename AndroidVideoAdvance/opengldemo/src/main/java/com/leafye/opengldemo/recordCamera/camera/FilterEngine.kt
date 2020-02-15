package com.leafye.opengldemo.recordCamera.camera

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLES20.*
import com.leafye.opengldemo.R
import com.leafye.opengldemo.utils.OpenGLUtils
import java.nio.FloatBuffer


/**
 * Created by leafye on 2020-02-05.
 */
class FilterEngine(
    OESTextureId: Int,
    context: Context
) {
    val buffer: FloatBuffer
    var oesTextureId = -1
    private var vertexShader = -1
    private var fragmentShader = -1

    var shaderProgram = -1

    private var aPositionLocation = -1
    private var aTextureCoordLocation = -1
    private var uTextureMatrixLocation = -1
    private var uTextureSamplerLocation = -1

    init {
        oesTextureId = OESTextureId
        buffer = OpenGLUtils.floatBufferUtil(vertexData)
        vertexShader = OpenGLUtils.loadShader(
            GL_VERTEX_SHADER,
            OpenGLUtils.readShaderFromResource(context, R.raw.camera1_vertex_shader)
        )
        fragmentShader = OpenGLUtils.loadShader(
            GL_FRAGMENT_SHADER,
            OpenGLUtils.readShaderFromResource(context, R.raw.camera1_fragment_shader)
        )
        shaderProgram = linkProgram(vertexShader, fragmentShader)
    }

    fun linkProgram(verShader: Int, fragShader: Int): Int {
        val program = glCreateProgram()
        if (program == 0) {
            throw RuntimeException("Create Program Failed!" + glGetError())
        }
        glAttachShader(program, verShader)
        glAttachShader(program, fragShader)
        glLinkProgram(program)
        glUseProgram(program)
        return program
    }

    fun drawTexture(transformMatrix: FloatArray) {
        aPositionLocation = glGetAttribLocation(shaderProgram, POSITION_ATTRIBUTE)
        aTextureCoordLocation =
            glGetAttribLocation(shaderProgram, TEXTURE_COORD_ATTRIBUTE)
        uTextureMatrixLocation =
            glGetUniformLocation(shaderProgram, TEXTURE_MATRIX_UNIFORM)
        uTextureSamplerLocation =
            glGetUniformLocation(shaderProgram, TEXTURE_SAMPLER_UNIFORM)

        glActiveTexture(GLES20.GL_TEXTURE0)
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, oesTextureId)
        glUniform1i(uTextureSamplerLocation, 0)
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)

        buffer.position(0)
        glEnableVertexAttribArray(aPositionLocation)
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, buffer)

        buffer.position(2)
        glEnableVertexAttribArray(aTextureCoordLocation)
        glVertexAttribPointer(aTextureCoordLocation, 2, GL_FLOAT, false, 16, buffer)

        glDrawArrays(GL_TRIANGLES, 0, 6)
    }

    companion object {

        private val vertexData = floatArrayOf(
            1f, 1f, 1f, 1f,
            -1f, 1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
            1f, 1f, 1f, 1f,
            -1f, -1f, 0f, 0f,
            1f, -1f, 1f, 0f
        )

        const val POSITION_ATTRIBUTE = "aPosition"
        const val TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate"
        const val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
        const val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"
    }
}