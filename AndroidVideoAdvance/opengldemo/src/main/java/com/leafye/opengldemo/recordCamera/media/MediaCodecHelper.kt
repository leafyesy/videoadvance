package com.leafye.opengldemo.recordCamera.media

import android.media.MediaCodec
import android.media.MediaFormat.KEY_COLOR_FORMAT
import android.media.MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar
import android.media.MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar
import android.media.MediaFormat.KEY_MIME
import android.media.MediaCodec.createEncoderByType
import android.media.MediaCodecInfo
import android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR
import android.media.MediaFormat.KEY_BITRATE_MODE
import android.media.MediaCodecInfo.CodecProfileLevel.AVCLevel31
import android.media.MediaFormat.KEY_LEVEL
import android.media.MediaCodecInfo.CodecProfileLevel.AVCProfileBaseline
import android.media.MediaFormat
import android.media.MediaFormat.KEY_PROFILE
import android.media.MediaFormat.KEY_I_FRAME_INTERVAL
import android.media.MediaFormat.KEY_FRAME_RATE
import android.media.MediaFormat.KEY_BIT_RATE
import android.media.MediaFormat.KEY_HEIGHT
import android.media.MediaFormat.KEY_WIDTH
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.leafye.opengldemo.recordCamera.config.MediaMakerConfig
import java.io.IOException

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.opengldemo.recordCamera.media
 * @ClassName:      MediaCodecHelper
 * @Description:    媒体文件编码类工具封装
 * @Author:         leafye
 * @CreateDate:     2020/1/21 14:17
 * @UpdateUser:
 * @UpdateDate:     2020/1/21 14:17
 * @UpdateRemark:
 */
class MediaCodecHelper {

    companion object {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun createSoftVideoMediaCodec(
            config: MediaMakerConfig,
            videoFormat: MediaFormat
        ): MediaCodec? {
            videoFormat.setString(KEY_MIME, "video/avc")
            videoFormat.setInteger(KEY_WIDTH, config.videoWidth)
            videoFormat.setInteger(KEY_HEIGHT, config.videoHeight)
            videoFormat.setInteger(KEY_BIT_RATE, config.mediacdoecAVCBitRate)
            videoFormat.setInteger(KEY_FRAME_RATE, config.mediacodecAVCFrameRate)
            videoFormat.setInteger(
                KEY_I_FRAME_INTERVAL,
                config.mediacodecAVCIFrameInterval
            )
            videoFormat.setInteger(
                KEY_PROFILE,
                AVCProfileBaseline
            )
            videoFormat.setInteger(
                KEY_LEVEL,
                AVCLevel31
            )
            videoFormat.setInteger(
                KEY_BITRATE_MODE,
                BITRATE_MODE_CBR
            )
            var result: MediaCodec? = null
            try {
                result = createEncoderByType(videoFormat.getString(KEY_MIME)!!)
                //select color
                val colorful = result.codecInfo
                    .getCapabilitiesForType(videoFormat.getString(KEY_MIME))
                    .colorFormats
                var dstVideoColorFormat = -1
                //select mediacodec colorformat
                if (isArrayContain(colorful, COLOR_FormatYUV420SemiPlanar)) {
                    dstVideoColorFormat = COLOR_FormatYUV420SemiPlanar
                    config.mediacodecAVCColorFormat = COLOR_FormatYUV420SemiPlanar
                }
                if (dstVideoColorFormat == -1 &&
                    isArrayContain(
                        colorful, COLOR_FormatYUV420Planar
                    )
                ) {
                    dstVideoColorFormat = COLOR_FormatYUV420Planar
                    config.mediacodecAVCColorFormat = COLOR_FormatYUV420Planar
                }
                if (dstVideoColorFormat == -1) {
                    Log.e("", "!!!!!!!!!!!UnSupport,mediaCodecColorFormat")
                    return null
                }
                videoFormat.setInteger(KEY_COLOR_FORMAT, dstVideoColorFormat)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

            return result
        }

        fun createAudioMediaCodec(config: MediaMakerConfig, audioFormat: MediaFormat): MediaCodec? {
            //Audio
            val result: MediaCodec
            audioFormat.setString(KEY_MIME, "audio/mp4a-latm")
            audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, config.mediacodecAACProfile)
            audioFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, config.mediacodecAACSampleRate)
            audioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, config.mediacodecAACChannelCount)
            audioFormat.setInteger(KEY_BIT_RATE, config.mediacodecAACBitRate)
            audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, config.mediacodecAACMaxInputSize)
            Log.d("", "creatingAudioEncoder,format=$audioFormat")
            try {
                result = createEncoderByType(audioFormat.getString(KEY_MIME))
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

            return result
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun createHardVideoMediaCodec(
            config: MediaMakerConfig,
            videoFormat: MediaFormat
        ): MediaCodec? {
            videoFormat.setString(KEY_MIME, "video/avc")
            videoFormat.setInteger(KEY_WIDTH, config.videoWidth)
            videoFormat.setInteger(KEY_HEIGHT, config.videoHeight)
            videoFormat.setInteger(
                KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
            )
            videoFormat.setInteger(KEY_BIT_RATE, config.mediacdoecAVCBitRate)
            videoFormat.setInteger(KEY_FRAME_RATE, config.mediacodecAVCFrameRate)
            videoFormat.setInteger(
                KEY_I_FRAME_INTERVAL,
                config.mediacodecAVCIFrameInterval
            )
            videoFormat.setInteger(
                KEY_PROFILE,
                AVCProfileBaseline
            )
            videoFormat.setInteger(
                KEY_LEVEL,
                AVCLevel31
            )
            videoFormat.setInteger(
                KEY_BITRATE_MODE,
                BITRATE_MODE_CBR
            )
            var result: MediaCodec? = null
            try {
                result = createEncoderByType(videoFormat.getString(KEY_MIME))
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

            return result
        }


        private fun isArrayContain(src: IntArray, target: Int): Boolean {
            for (color in src) {
                if (color == target) {
                    return true
                }
            }
            return false
        }

        private fun isProfileContain(
            src: Array<MediaCodecInfo.CodecProfileLevel>,
            target: Int
        ): Boolean {
            for (color in src) {
                if (color.profile == target) {
                    return true
                }
            }
            return false
        }

    }


}