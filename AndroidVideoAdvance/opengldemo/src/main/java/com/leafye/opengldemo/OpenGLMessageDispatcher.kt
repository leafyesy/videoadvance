package com.leafye.opengldemo

import android.app.Activity
import android.opengl.GLSurfaceView

/**
 * OpenGl线程调度
 */
class OpenGLMessageDispatcher {

    fun postMessageToUiThread(glSv: GLSurfaceView, message: OpenGLMessage) {
        (glSv.context as Activity).runOnUiThread(object : Runnable {
            override fun run() {
                message
            }
        })
    }

    fun postMessageToGlSurfaceView(glSv: GLSurfaceView, message: OpenGLMessage) {
        glSv.queueEvent(object : Runnable {
            override fun run() {
                message
            }
        })
    }


}

