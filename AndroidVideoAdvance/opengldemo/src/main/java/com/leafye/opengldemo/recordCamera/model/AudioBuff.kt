package com.leafye.opengldemo.recordCamera.model

class AudioBuff(val audioFormat: Int,size: Int) {
    var buff: ByteArray? = null
        private set

    var isReadyToFill: Boolean = true

    init {
        buff = ByteArray(size)
    }
}