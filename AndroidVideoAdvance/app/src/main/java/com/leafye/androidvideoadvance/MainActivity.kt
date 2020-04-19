package com.leafye.androidvideoadvance

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leafye.annotation.AViewBinder
import com.leafye.apilib.BindView

/**
 * Created by leafye on 2020/4/19.
 */
open class MainActivity: AppCompatActivity() {
    @BindView(R.id.helloWorld)
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AViewBinder.bind(this)
        tv.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "hhh",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AViewBinder.unBind(this)
    }

}