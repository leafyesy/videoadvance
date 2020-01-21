package com.leafye.opengldemo.shape

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle {

    private var vertexBuffer: FloatBuffer? = null


    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3

        @JvmStatic
        var triangleCoords = floatArrayOf(// in counterclockwise order:
            0.0f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
        )
    }

    // Set color with red, green, blue and alpha (opacity) values
    val colorArr = floatArrayOf(255F, 0F, 0F, 1.0F)

    init {
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
        val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        // 数组排列用nativeOrder
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer().also {
            it.put(triangleCoords)
            it.position(0)
        }
    }

}