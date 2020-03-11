package com.leafye.ffmpegapp.vm

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.FFmpegUtils
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.ffmpeg.FFmpegCmd
import com.leafye.ffmpegapp.model.AudioModel
import com.leafye.ffmpegapp.utils.FFmpegTestFileManager
import kotlin.concurrent.thread

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
            checkPath(model.srcAudioFile())?.let {
                val transformAudioCmdArr =
                    FFmpegUtils.transformAudio(it[0], model.getTransformAudioPath())
                val cmdTask = FFmpegCmd.CmdTask(transformAudioCmdArr)
                FFmpegCmd.excute(cmdTask)
            }
        } else {
            //使用MediaCodec与mp3lame转mp3
        }
    }

    val audioCutClick = View.OnClickListener {
        checkPath(model.srcAudioFile())?.let {
            val cutAudioCmdArr = FFmpegUtils.cutAudio(
                srcFile = it[0],
                startTime = 10,
                duration = 10,
                targetFile = model.getCutPath()
            )
            FFmpegCmd.excute(FFmpegCmd.CmdTask(cutAudioCmdArr))
        }
    }

    val audioConcatClick = View.OnClickListener {
        checkPath(model.srcAudioFile(), model.getAppendAudioPath())
            ?.let {
                val concatAudioCmdArr =
                    FFmpegUtils.concatAudio(it[0], it[1], model.getConcatPath())
                FFmpegCmd.excute(FFmpegCmd.CmdTask(concatAudioCmdArr))
            }
    }

    val audioMixClick = View.OnClickListener {
        checkPath(model.srcAudioFile(), model.getAppendAudioPath())
            ?.let {
                val mixAudioCmdArr = FFmpegUtils.mixAudio(it[0], it[1], model.getMixPath())
                FFmpegCmd.excute(FFmpegCmd.CmdTask(mixAudioCmdArr))
            }
    }

    val audioEncodeClick = View.OnClickListener {
        checkPath(model.getTestPcmPath())?.let {
            val sampleRate = 8000//pcm数据的采样率，一般采样率为8000、16000、44100
            val channel = 1//pcm数据的声道，单声道为1，立体声道为2
            val encodeAudioCmdArr =
                FFmpegUtils.encodeAudio(
                    srcFile = it[0],
                    targetFile = model.encodeWavPath(),
                    sampleRate = sampleRate,
                    channel = channel
                )
            FFmpegCmd.excute(FFmpegCmd.CmdTask(encodeAudioCmdArr))
        }
    }

    val audioPlayTrackClick = View.OnClickListener {
        checkPath(model.srcAudioFile())?.let {
            thread {
                YeFFmpeg.instance().ffmpegPlay(it[0])
            }
        }
    }

    val audioPlayByOpenSLClick = View.OnClickListener {
        checkPath(model.srcAudioFile())?.let {
            thread {
                YeFFmpeg.instance().openslPlayer(it[0])
            }
        }
    }

    val stopAudioPlayByOpenSLClick = View.OnClickListener {
        YeFFmpeg.instance().stopOpenslPlayer()
    }


    private fun checkPath(vararg path: String?): MutableList<String>? =
        mutableListOf<String>().apply {
            path.forEach {
                if (!FFmpegTestFileManager.checkFile(it)) {
                    Log.e(TAG, "文件不存在:$it")
                    return null
                } else {
                    add(it!!)
                }
            }
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