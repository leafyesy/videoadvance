package com.leafye.ffmpegapp

import java.util.*

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp
 * @ClassName:      FFmpegUtils
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/5 17:17
 * @UpdateUser:
 * @UpdateDate:     2020/3/5 17:17
 * @UpdateRemark:
 */
class FFmpegUtils {

    companion object {
        /**
         * 使用ffmpeg命令行进行音频转码
         */
        fun transformAudio(
            srcFile: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s %s"
            return String.format(cmd, srcFile, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行音频剪切
         */
        fun cutAudio(
            srcFile: String,
            startTime: Int,
            duration: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -ss %d -t %d %s"
            return String.format(cmd, srcFile, startTime, duration, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行音频合并
         */
        fun concatAudio(
            srcFile: String,
            appendFile: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i concat:%s|%s -acodec copy %s"
            return String.format(cmd, srcFile, appendFile, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行音频混合
         */
        fun mixAudio(
            srcFile: String,
            mixFile: String,
            targetFile: String
        ): Array<String> {
            val cmd =
                "ffmpeg -i %s -i %s -filter_complex amix=inputs=2:duration=first -strict -2 %s"
            return String.format(cmd, srcFile, mixFile, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行音视频合成
         */
        fun mediaMux(
            videoFile: String,
            audioFile: String,
            duration: Int,
            muxFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -i %s -t %d %s"
            return String.format(cmd, videoFile, audioFile, duration, muxFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行抽取音频
         */
        fun extractAudio(
            srcFile: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -acodec copy -vn %s"
            return String.format(cmd, srcFile, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行抽取视频
         */
        fun extractVideo(
            srcFile: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -vcodec copy -an %s"
            return String.format(cmd, srcFile, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行视频转码
         */
        fun transformVideo(
            srcFile: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -vcodec copy -acodec copy %s"
            return String.format(cmd, srcFile, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行视频剪切
         */
        fun cutVideo(
            srcFile: String,
            startTime: Int,
            duration: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -ss %d -t %d -acodec copy -vcodec copy %s"
            return String.format(cmd, srcFile, startTime, duration, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行视频截图
         */
        fun sreenShot(
            srcFile: String,
            time: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -f image2 -ss %d -vframes 1- an %s"
            return String.format(cmd, srcFile, time, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行给视频添加水印
         */
        fun addWaterMark(
            srcFile: String,
            waterMark: String,
            resolution: String,
            bitRate: Int,
            targetFile: String
        ): Array<String> {
            val bitRateStr = "${bitRate}k"
            val cmd = "ffmpeg -i %s -i %s -s %s -s %s -b:v %s -filter_complex overlay=0:0 %s"
            return String.format(cmd, srcFile, targetFile, waterMark, resolution, bitRateStr)
                .splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行视频转成Gif动图
         *  @param srcFile    源文件
         * @param startTime  开始时间
         * @param duration   截取时长
         * @param targetFile 目标文件
         * @param resolution 分辨率
         * @param frameRate  帧率
         * @return Gif文件
         */
        fun generateGif(
            srcFile: String,
            startTime: Int,
            duration: Int,
            resolution: String,
            frameRate: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -ss %d -t %d -s %s -r %d -f git %s"
            return String.format(
                cmd,
                srcFile,
                startTime,
                duration,
                resolution,
                frameRate,
                targetFile
            ).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行屏幕录制
         */
        fun sreenRecord(
            size: String,
            recordTime: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t %d %s"
            return String.format(cmd, size, recordTime, targetFile).splitToArray()
        }

        /**
         * 使用ffmpeg命令行进行图片合成视频
         */
        fun pictureToVideo(
            srcFile: String,
            frameRate: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -f image2 -r %d -i %simg#d.jpg -vcodec mpeg4 %s"
            return String.format(cmd, frameRate, srcFile, targetFile).apply {
                replace("#", "%")
            }.splitToArray()
        }

        /**
         * 转换图片尺寸大小
         */
        fun covertResolution(
            srcFile: String,
            resolution: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -s %s %s"
            return String.format(cmd, srcFile, resolution, targetFile).splitToArray()
        }

        /**
         * 音频编码
         */
        fun encodeAudio(
            srcFile: String,
            targetFile: String,
            sampleRate: Int,
            channel: Int
        ): Array<String> {
            val cmd = "ffmpeg -f s16le -ar %d -ac %d -i %s %s"
            return String.format(cmd, srcFile, targetFile, sampleRate, channel).splitToArray()
        }

        /**
         * 多画面拼接视频
         */
        fun multiVideo(
            input1: String, input2: String,
            targetFile: String,
            videoLayout: Int
        ): Array<String> {
            val cmd = "ffmpeg -i %s -i %s -filter_complex hstack %s".apply {
                if (videoLayout == VideoLayout.LAYOUT_VERTICAL) {
                    replace("hstack", "vstack")
                }
            }
            return String.format(cmd, input1, input2, targetFile).splitToArray()
        }

        /**
         * 视频反序倒播
         */
        fun reverseVideo(
            inputFile: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -i %s -filter_complex [0:v]reverse[v] -map [v] %s"
            return String.format(cmd, inputFile, targetFile).splitToArray()
        }

        /**
         * 视频降噪
         */
        fun denoiseVideo(
            inputFile: String,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -nr 500 %s"
            return String.format(cmd, inputFile, targetFile).splitToArray()
        }

        /**
         * 视频抽帧转成图片
         */
        fun videoToImage(
            inputFile: String,
            startTime: Int,
            duration: Int,
            frameRate: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -ss %s -t %s -r %s %s"
            return String.format(cmd, inputFile, startTime, duration, frameRate, targetFile)
                .splitToArray()
        }

        /**
         * 视频叠加成画中画
         */
        fun picInPicVideo(
            inputFile1: String,
            inputFile2: String,
            x: Int,
            y: Int,
            targetFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -i %s -filter_complex overlay=%d:%d %s"
            return String.format(cmd, inputFile1, inputFile2, x, y, targetFile).splitToArray()
        }

        /**
         * mp4视频的moov往mdat前面移动
         */
        fun moveMoovAhead(
            inputFile: String,
            outputFile: String
        ): Array<String> {
            val cmd = "ffmpeg -i %s -movflags faststart -acodec copy -vcodec copy %s"
            return String.format(Locale.getDefault(), cmd, inputFile, outputFile).splitToArray()
        }

        /**
         * 使用ffprobe探测多媒体格式
         */
        fun probeFormat(inputFile: String): Array<String> {
            val cmd = "ffprobe -i %s -show_streams -show_format -print_format json"
            return String.format(Locale.getDefault(), cmd, inputFile).splitToArray()
        }


        private fun String.splitToArray(): Array<String> {
            return this.split(" ").toTypedArray()
        }
    }


}

class VideoLayout {
    companion object {
        val LAYOUT_HORIZONTAL = 1
        val LAYOUT_VERTICAL = 2
    }
}