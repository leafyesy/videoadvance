package com.leafye.audiorecorddemo.linxi.callback

interface WebSocketCallback {

    fun onOpen()

    fun onFailure(t: Throwable)

    fun onClosing(code: Int, reason: String)

    fun onClosed(code: Int, reason: String)

}