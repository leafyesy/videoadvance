package com.leafye.ffmpegapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.databinding.ActivityAudioHandleBinding
import com.leafye.ffmpegapp.vm.AudioHandleViewModel

/**
 * Created by leafye on 2020-03-04.
 */
class AudioHandleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioHandleBinding

    private val vm by lazy {
        ViewModelProviders.of(this).get(AudioHandleViewModel::class.java)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AudioHandleActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityAudioHandleBinding>(
            this,
            R.layout.activity_audio_handle
        ).apply {
            vm = this@AudioHandleActivity.vm
        }
    }


}