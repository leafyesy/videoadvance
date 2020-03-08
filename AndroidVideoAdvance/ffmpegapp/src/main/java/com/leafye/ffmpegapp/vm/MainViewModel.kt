package com.leafye.ffmpegapp.vm

import android.app.Application
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.model.MainModel


/**
 * Created by leafye on 2020-03-04.
 */
class MainViewModel(model: MainModel) : BaseViewModel<MainModel>(model) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    val enterAudioHandleEvent = MutableLiveData<Int>()

    val btnCodecClick = View.OnClickListener {
        val storagePath = Environment.getExternalStorageDirectory().absolutePath
        val path1 = "$storagePath/input.mp4"
        val path2 = "$storagePath/2.mp4"
        Log.i(TAG, "input path:$path1    output path:$path2")
        YeFFmpeg.instance().decode(path1, path2)
    }
    val btnFilterClick = View.OnClickListener {
        val avfilterinfo = YeFFmpeg.instance().avfilterinfo()
        Log.i(TAG, "filter:$avfilterinfo")
    }
    val btnFormatClick = View.OnClickListener {
        val avformatinfo = YeFFmpeg.instance().avformatinfo()
        Log.i(TAG, "format:$avformatinfo")
    }
    val btnProtocolClick = View.OnClickListener {
        val urlprotocolinfo = YeFFmpeg.instance().urlprotocolinfo()
        Log.i(TAG, "protocol:$urlprotocolinfo")
    }

    val btnAudioHandle = View.OnClickListener {
        enterAudioHandleEvent.value = ((enterAudioHandleEvent.value ?: 0) + 1)
    }

}

class MainViewModelProduct(private val model: MainModel) :
    VMProduct<MainViewModel>() {
    override fun isThis(classNew: Class<*>): MainViewModel? =
        with(classNew) {
            if (isAssignableFrom(MainViewModel::class.java)) {
                MainViewModel(model)
            } else null
        }
}

class MainViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass
                .getConstructor(Application::class.java)
            return constructor.newInstance(application)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.create(modelClass)
    }
}