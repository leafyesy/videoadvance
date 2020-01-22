package com.leafye.opengldemo.shape

import com.leafye.opengldemo.utils.OpenGLUtils

abstract class BaseShape {
    protected val program: Int

    init {
        //把要执行的代码片段放入OpenGL中,并获取int(key)
        val shaderList = this.initOpenGLParam()
        program = OpenGLUtils.linkProgram(shaderList)
        //创建空的OpenGL ES程序
        initData()
    }

    open fun initData() {

    }

    abstract fun initOpenGLParam(): MutableList<Int>


}