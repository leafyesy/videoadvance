package com.leafye.ffmpegapp.vm

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.model.AudioModel

/**
 * Created by leafye on 2020-03-04.
 */
class AudioHandleViewModel(model: AudioModel) : BaseViewModel<AudioModel>(model) {

    companion object {
        private const val TAG = "AudioHandleViewModel"
    }

    val useFFmpeg: MutableLiveData<Boolean> = MutableLiveData(true)

    fun audioTransformClick(view: View) {
        Log.d(TAG, "audio transform.")
    }

    fun audioCutClick(view: View){

    }

    fun audioConcatClick(view:View){

    }

}

class AudiohandleVmProduct(private val model:AudioModel) :
    VMProduct<AudioHandleViewModel>() {
    override fun isThis(classNew: Class<*>): AudioHandleViewModel? {
        return with(classNew) {
            if (isAssignableFrom(AudioHandleViewModel::class.java)) {
                AudioHandleViewModel(model)
            } else null
        }
    }

}