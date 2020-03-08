package com.leafye.ffmpegapp.ffmpeg

/**
 * Created by leafye on 2020-03-08.
 * 事件处理结果回调
 */
interface ResultCallback {

    fun begin(result: Result)

    fun progress(result: Result)

    fun end(result: Result)

    fun error(result: Result)

}