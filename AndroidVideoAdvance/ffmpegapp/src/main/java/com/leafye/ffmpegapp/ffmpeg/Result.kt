package com.leafye.ffmpegapp.ffmpeg

/**
 * Created by leafye on 2020-03-08.
 * 事件处理结果
 */
class Result(val type: Type) {

    var code: Int = 0
    var msg: String? = null
    var progresss: Int = 0

    enum class Type {
        BEGIN, PROGRESS, SUCCESS, ERROR
    }


}