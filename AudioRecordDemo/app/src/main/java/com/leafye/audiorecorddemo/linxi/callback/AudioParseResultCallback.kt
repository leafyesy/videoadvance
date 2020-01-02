package com.leafye.audiorecorddemo.linxi.callback

interface AudioParseResultCallback<T> {

    //返回的数据结果
    fun parse(result: T)
}