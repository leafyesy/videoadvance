package com.leafye.audiorecorddemo

import android.app.Application
import com.lingxi.voicesupport.speech.CloudSpeech

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CloudSpeech.getInstance().initSDK(this)
    }
}