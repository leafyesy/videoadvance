package com.leafye.ffmpegapp.ffmpeg.exception

/**
 * Created by leafye on 2020-03-08.
 */
open class FFmpegException(message: String?=null, cause: Throwable?=null) :
    Throwable(message, cause) {

    companion object{

        const val ERROR_PARAME = 1

    }


}