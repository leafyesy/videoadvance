package com.leafye.ffmpegapp.utils

import android.os.Environment

/**
 * Created by leafye on 2020/3/8.
 */
object FFmpegTestFileManager {

    fun basePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + "/yeffmpeg"
    }


    fun audioFileSrc(): String {
        return basePath() + "/test.mp3"
    }

}