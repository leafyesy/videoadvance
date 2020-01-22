package com.leafye.opengldemo.recordCamera.filter.softaudiofilter

class BaseSoftAudioFilter {

    protected var SIZE: Int = 0
    protected var SIZE_HALF: Int = 0

    fun onInit(size: Int) {
        SIZE = size
        SIZE_HALF = size / 2
    }

    /**
     *
     * @param orignBuff
     * @param targetBuff
     * @param presentationTimeMs
     * @param sequenceNum
     * @return false to use orignBuff,true to use targetBuff
     */
    fun onFrame(
        orignBuff: ByteArray,
        targetBuff: ByteArray,
        presentationTimeMs: Long,
        sequenceNum: Int
    ): Boolean {
        return false
    }

    fun onDestroy() {

    }
}