package com.leafye.ffmpegapp.utils

import android.os.Environment
import java.io.File

/**
 * Created by leafye on 2020/3/8.
 */
object FFmpegTestFileManager {

    fun basePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + "/yeffmpeg"
    }


    fun audioFileSrc(): String {
        return basePath() + "/input.mp3"
    }

    fun checkFile(path: String? = null): Boolean {
        if (path != null) {
            return File(path).exists()
        }
        return false
    }

}