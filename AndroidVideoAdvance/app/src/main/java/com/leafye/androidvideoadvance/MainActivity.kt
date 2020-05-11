package com.leafye.androidvideoadvance

import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.leafye.annotation.AViewBinder
import com.leafye.apilib.BindView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by leafye on 2020/4/19.
 */
open class MainActivity : AppCompatActivity() {
    @BindView(R.id.helloWorld)
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AViewBinder.bind(this)
        tv.setOnClickListener {
            ivShow.setImageResource(R.mipmap.ic_launcher)
        }
        Handler
    }

    override fun onDestroy() {
        super.onDestroy()
        AViewBinder.unBind(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

    }


}