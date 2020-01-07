package com.leafye.opengldemo

import android.opengl.EGL14
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startGl()
    }

    private fun startGl() {
        val eglGetDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)

    }
}
