package com.leafye.opengldemo.recordCamera.config

class RecordConfig(
    var videoSize: Size,
    var videoBufferQueueNum: Int,
    var bitRate: Int,
    var renderingMode: Int = MediaConfig.Rending_Model_OpenGLES,
    var defaultCamera: Int,
    var frontCameraDirectionMode: Int,
    var videoFPS: Int,
    var videoGOP: Int,
    var log: Boolean
) {


}