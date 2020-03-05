package com.leafye.ffmpegapp

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


        private fun String.splitToArray(): Array<String> {
            return this.split(" ").toTypedArray()
        }


    }


}