package com.leafye.opengldemo.recordCamera.config

import com.leafye.opengldemo.utils.LogObj

class MediaMakerConfig {

    companion object {
        val RENDERING_MODE_OPENGLES = 2
        /**
         * same with jni
         */
        val FLAG_DIRECTION_FLIP_HORIZONTAL = 0x01
        val FLAG_DIRECTION_FLIP_VERTICAL = 0x02
        val FLAG_DIRECTION_ROATATION_0 = 0x10
        val FLAG_DIRECTION_ROATATION_90 = 0x20
        val FLAG_DIRECTION_ROATATION_180 = 0x40
        val FLAG_DIRECTION_ROATATION_270 = 0x80
    }

    var done: Boolean = false
    var printDetailMsg: Boolean = false
    var renderingMode: Int = 0
    var frontCameraDirectionMode: Int = 0
    var backCameraDirectionMode: Int = 0
    var isPortrait: Boolean = false
    var previewVideoWidth: Int = 0
    var previewVideoHeight: Int = 0
    var videoWidth: Int = -1
    var videoHeight: Int = -1
    var videoFPS: Int = -1
    var videoGOP: Int = 1
    var cropRatio: Float = 0.toFloat()
    var previewColorFormat: Int = -1
    var previewBufferSize: Int = -1
    var mediacodecAVCColorFormat: Int = -1
    var mediacdoecAVCBitRate: Int = -1
    var videoBufferQueueNum: Int = -1
    var audioBufferQueueNum: Int = -1
    var audioRecoderFormat: Int = -1
    var audioRecoderSampleRate: Int = -1
    var audioRecoderChannelConfig: Int = -1
    var audioRecoderSliceSize: Int = -1
    var audioRecoderSource: Int = -1
    var audioRecoderBufferSize: Int = -1
    var previewMaxFps: Int = -1
    var previewMinFps: Int = -1
    var mediacodecAVCFrameRate: Int = -1
    var mediacodecAVCIFrameInterval: Int = -1
    var mediacodecAVCProfile: Int = -1
    var mediacodecAVClevel: Int = -1

    var mediacodecAACProfile: Int = -1
    var mediacodecAACSampleRate: Int = -1
    var mediacodecAACChannelCount: Int = -1
    var mediacodecAACBitRate: Int = -1
    var mediacodecAACMaxInputSize: Int = -1

    //face detect
    var isFaceDetectEnable = false
    var isSquare = false

    var saveVideoEnable = false
    var saveVideoPath: String? = null

    override fun toString(): String {
        return LogObj.toString(this)
    }


}