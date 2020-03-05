package com.leafye.ffmpegapp.vm

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Created by leafye on 2020-03-04.
 */
class AudioHandleViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "AudioHandleViewModel"
    }

    val useFFmpeg: MutableLiveData<Boolean> = MutableLiveData(true)

    val audioTransformClick = View.OnClickListener {
        Log.d(TAG, "audio transform.")

    }


}