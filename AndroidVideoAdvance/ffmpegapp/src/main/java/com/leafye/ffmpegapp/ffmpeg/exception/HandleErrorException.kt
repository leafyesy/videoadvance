package com.leafye.ffmpegapp.ffmpeg.exception

/**
 * Created by leafye on 2020-03-08.
 */
class HandleErrorException(val code: Int, message: String? = null, cause: Throwable? = null) :
    FFmpegException(message, cause) {
}