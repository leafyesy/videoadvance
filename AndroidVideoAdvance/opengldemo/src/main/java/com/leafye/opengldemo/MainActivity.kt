package com.leafye.opengldemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leafye.opengldemo.glproxy.ShapeActivity
import com.leafye.opengldemo.recordCamera.GLCameraActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shapeBtn.setOnClickListener {
            ShapeActivity.startActivity(this)
        }
        recordBtn.setOnClickListener {

        }
        glCameraBtn.setOnClickListener {
            GLCameraActivity.start(this)
        }
    }
}