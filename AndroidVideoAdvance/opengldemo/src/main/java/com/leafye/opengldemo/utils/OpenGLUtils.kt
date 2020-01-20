package com.leafye.opengldemo.utils

import android.opengl.GLES20
import java.nio.*

class OpenGLUtils {


    companion object {
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
    }

}