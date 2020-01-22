package com.leafye.opengldemo.utils

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.Log
import java.nio.*

class OpenGLUtils {


    companion object {

        private const val TAG = "OpenGLUtils"

        fun intBufferUtil(arr: IntArray): IntBuffer {
            //初始化ByteBuffer,长度为arr数组的长度*4,因为一个int为4个字节
            val qbb = ByteBuffer.allocateDirect(arr.size * 4)
            //数组排列用nativeOrder
            qbb.order(ByteOrder.nativeOrder())
            return qbb.asIntBuffer().apply {
                put(arr)
                position(0)
            }
        }

        fun floatBufferUtil(arr: FloatArray): FloatBuffer {
            val qbb = ByteBuffer.allocateDirect(arr.size * 4)
            qbb.order(ByteOrder.nativeOrder())
            return qbb.asFloatBuffer().apply {
                put(arr)
                position(0)
            }
        }

        fun shortBufferUtil(arr: ShortArray): ShortBuffer {
            val qbb = ByteBuffer.allocateDirect(arr.size * 4)
            qbb.order(ByteOrder.nativeOrder())
            return qbb.asShortBuffer().apply {
                put(arr)
                position(0)
            }
        }

        fun byteBufferUtil(arr: ByteArray): ByteBuffer {
            val qbb = ByteBuffer.allocateDirect(arr.size * 4)
            qbb.order(ByteOrder.nativeOrder())
            return qbb.apply {
                put(arr)
                position(0)
            }
        }

        /**
         * @param type
         * 顶点着色器:GLES20.GL_VERTEX_SHADER
         * 片段着色器:GLES20.GL_FRAGMENT_SHADER
         */
        fun loadShader(type: Int, shaderCode: String): Int {
            val shader = GLES20.glCreateShader(type)
            //添加着色器代码 并 编译
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            return shader
        }

        fun changeMvpMatrixInside(
            viewWidth: Float,
            viewHeight: Float,
            textureWidth: Float,
            textureHeight: Float
        ): FloatArray {
            val scale = viewWidth * textureHeight / viewHeight / textureWidth
            val mvp = FloatArray(16)
            Matrix.setIdentityM(mvp, 0)
            Matrix.scaleM(
                mvp,
                0,
                if (scale > 1) 1f / scale else 1f,
                if (scale > 1) 1f else scale,
                1f
            )
            return mvp
        }

        fun changeMvpMatrixCrop(
            viewWidth: Float,
            viewHeight: Float,
            textureWidth: Float,
            textureHeight: Float
        ): FloatArray {
            val scale = viewWidth * textureHeight / viewHeight / textureWidth
            val mvp = FloatArray(16)
            Matrix.setIdentityM(mvp, 0)
            Matrix.scaleM(
                mvp,
                0,
                if (scale > 1) 1f else 1f / scale,
                if (scale > 1) scale else 1f,
                1f
            )
            return mvp
        }

        /**
         * Creates a texture from bitmap.
         *
         * @param bmp bitmap data
         * @return Handle to texture.
         */
        fun createImageTexture(bmp: Bitmap?): Int {
            if (bmp == null) {
                return 0
            }
            val textureHandles = IntArray(1)
            GLES20.glGenTextures(1, textureHandles, 0)
            checkGlError("glGenTextures")
            if (textureHandles[0] == 0) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.")
                return 0
            }
            // Bind the texture handle to the 2D texture target.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0])
            // Configure min/mag filtering, i.e. what scaling method do we use if what we're rendering
            // is smaller or larger than the source image.
            // 设置缩小过滤为三线性过滤
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR_MIPMAP_LINEAR
            )
            // 设置放大过滤为双线性过滤
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            // 加载纹理到 OpenGL，读入 Bitmap 定义的位图数据，并把它复制到当前绑定的纹理对象
            // Load the data from the buffer into the texture handle.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0)
            // 生成 MIP 贴图
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            return textureHandles[0]
        }

        private fun checkGlError(op: String) {
            val error = GLES20.glGetError()
            if (error != GLES20.GL_NO_ERROR) {
                val msg = op + ": glError 0x" + Integer.toHexString(error)
                Log.e(TAG, "checkGlError: $msg")
            }
        }
    }

}