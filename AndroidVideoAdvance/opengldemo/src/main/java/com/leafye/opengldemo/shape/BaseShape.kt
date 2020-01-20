package com.leafye.opengldemo.shape

import android.opengl.GLES20

abstract class BaseShape {
    protected val program: Int

    init {
        //把要执行的代码片段放入OpenGL中,并获取int(key)
        val shaderList = this.initOpenGLParam()

        //创建空的OpenGL ES程序
        program = GLES20.glCreateProgram()
        shaderList.forEach {
            GLES20.glAttachShader(program, it)
        }
        //创建OpenGL ES程序可执行文件
        GLES20.glLinkProgram(program)
    }

    abstract fun initOpenGLParam():MutableList<Int>


}