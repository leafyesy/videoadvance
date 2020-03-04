package com.leafye.ffmpegapp.vm

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel

/**
 * Created by leafye on 2020-03-04.
 */
class AudioHandleViewModel(application: Application):AndroidViewModel(application) {

    companion object {
        private const val TAG = "AudioHandleViewModel"
    }

    val audioTransformClick = View.OnClickListener {
        Log.d(TAG, "audio transform.")

    }


}