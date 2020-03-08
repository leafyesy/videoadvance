package com.leafye.ffmpegapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.leafye.base.BaseActivity
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.databinding.ActivityAudioHandleBinding
import com.leafye.ffmpegapp.vm.AudioHandleViewModel

/**
 * Created by leafye on 2020-03-04.
 */
class AudioHandleActivity : BaseActivity<AudioHandleViewModel,ActivityAudioHandleBinding>() {
    override fun initVariableId(): Int = BR.vm

    override fun initContentView(savedInstanceState: Bundle?): Int = R.layout.activity_audio_handle

    override fun setupObservers() {

    }

    override fun activityPrepared() {

    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AudioHandleActivity::class.java))
        }
    }


}