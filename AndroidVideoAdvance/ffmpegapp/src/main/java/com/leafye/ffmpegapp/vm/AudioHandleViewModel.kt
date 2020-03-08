package com.leafye.ffmpegapp.vm

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.FFmpegUtils
import com.leafye.ffmpegapp.ffmpeg.FFmpegCmd
import com.leafye.ffmpegapp.model.AudioModel

/**
 * Created by leafye on 2020-03-04.
 */
class AudioHandleViewModel(model: AudioModel) : BaseViewModel<AudioModel>(model) {

    companion object {
        private const val TAG = "AudioHandleViewModel"
    }

    val useFFmpeg: MutableLiveData<Boolean> = MutableLiveData(true)

    val audioTransformClick = View.OnClickListener {
        Log.d(TAG, "audio transform.")
        if (useFFmpeg.value == true) {
            val transformAudioCmdArr =
                FFmpegUtils.transformAudio(model.srcAudioFile(), model.getTransformAudioPath())
            val cmdTask = FFmpegCmd.CmdTask(transformAudioCmdArr)
            FFmpegCmd.excute(cmdTask)
        } else {
            //使用MediaCodec与mp3lame转mp3

        }
    }

    val audioCutClick = View.OnClickListener {

    }

    val audioConcatClick = View.OnClickListener {

    }

}

class AudiohandleVmProduct(private val model: AudioModel) :
    VMProduct<AudioHandleViewModel>() {
    override fun isThis(classNew: Class<*>): AudioHandleViewModel? {
        return with(classNew) {
            if (isAssignableFrom(AudioHandleViewModel::class.java)) {
                AudioHandleViewModel(model)
            } else null
        }
    }

}