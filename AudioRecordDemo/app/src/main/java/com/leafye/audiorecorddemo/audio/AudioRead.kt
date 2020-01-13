package com.leafye.audiorecorddemo.audio

import android.os.Environment
import android.os.SystemClock
import android.util.Log
import com.leafye.audiorecorddemo.audio.utils.PcmToWav
import com.leafye.audiorecorddemo.audio.utils.Raw2Spx
import com.leafye.speex.SpeexUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.nio.charset.Charset
import kotlin.experimental.and

/**
 *
 */
class AudioRead(private val filePath: String, private val callback: ReadCallback) : Runnable {
    companion object {
        private const val TAG = "AudioRead"
        private const val CHUNCKED_SIZE = 1280//音频读取的缓存字节数
        private const val INTERNAL_TIME = 40L//ms
    }

    private val raw2Spx: Raw2Spx by lazy {
        Raw2Spx()
    }

    val speexUtil by lazy { SpeexUtil() }

    override fun run() {
//        val tranToMav = tranToMav() ?: return
//        tranToSpeex2(tranToMav)

        //val newSpeexFile = tranToSpeex()
        val bytes = ByteArray(CHUNCKED_SIZE)
        try {
            if (read1(bytes, filePath)) return
        } catch (e: FileNotFoundException) {
            Log.w(TAG, "文件没找到:${filePath}")
        }
    }

    private fun tranToSpeex1(): File? {
        val pcmFile = File(filePath)
        val name = pcmFile.name
        val newSpeexFile = File(pcmFile.parent + "/" + name + ".speex")
        raw2Spx.raw2spx(filePath, newSpeexFile.absolutePath)
        return newSpeexFile
    }

    private fun tranToSpeex2(file: File): File? {
        val raf = RandomAccessFile(file, "r")
        val saveFilePath = Environment.getExternalStorageDirectory().absolutePath + "/save_file.spx"
        val raf2 = RandomAccessFile(saveFilePath, "rw")
        var len = 0
        val sArr = ShortArray(1024)
        val bArr = ByteArray(1024)
        val bArrBuffer = ByteArray(1024)
        while (len != -1) {
            len = raf.read(bArrBuffer)
            bArrBuffer.forEachIndexed { index, byte ->
                sArr[index] = (byte.toShort() and 0xFF)
            }
            speexUtil.encode(sArr, 0, bArr, 1024)
            raf2.write(bArr)
        }
        return File(saveFilePath)
    }

    private fun tranToMav(): File? {
        val pcmFile = File(filePath)
        val name = pcmFile.name
        val newWavFile = File(pcmFile.parent + "/" + name + ".wav")
        val makePCMFileToWAVFile =
            PcmToWav.makePCMFileToWAVFile(filePath, newWavFile.absolutePath, false)
        if (!makePCMFileToWAVFile) {
            Log.w(TAG, "pcm tran wav failure!!!")
            return null
        }
        return newWavFile
    }

    private fun read1(bytes: ByteArray, filePath: String): Boolean {
        val raf = RandomAccessFile(filePath, "r")
        var curLen = 0L
        var num = 1
        var len = raf.read(bytes)
        val length = raf.length()
        while (len != -1) {
            val result = if (len < CHUNCKED_SIZE || curLen + len == length) {
                callback.read(bytes.copyOfRange(0, len), num, true)
            } else {
                callback.read(bytes, num, false)
            }
            if (!result) {
                Log.w(TAG, "data handle error!check net connect")
                break
            }
            curLen += len
            Log.d(TAG, "curLen:$curLen position:${raf.filePointer}")
            if (curLen == length) return true
            len = raf.read(bytes)
            num++
            SystemClock.sleep(INTERNAL_TIME)
        }
        return false
    }

    private fun read2(bytes: ByteArray, filePath: String): Boolean {
        val file = File(filePath)
        val length = file.length()
        val raf = FileInputStream(filePath)

        var curLen = 0L
        var num = 0
        var len = raf.read(bytes)
        //val length = raf.length()
        while (len != -1) {
            val result = if (len < CHUNCKED_SIZE || curLen + len == length) {
                callback.read(bytes.copyOfRange(0, len), num, true)
            } else {
                callback.read(bytes, num, false)
            }
            if (!result) {
                Log.w(TAG, "data handle error!check net connect")
                break
            }
            curLen += len
            Log.d(TAG, "curLen:$curLen")
            if (curLen == length) return true
            len = raf.read(bytes)
            num++
            //SystemClock.sleep(INTERNAL_TIME)
        }
        return false
    }

    private fun getFinishBytes() =
        "{\"end\": true}".toByteArray(Charset.forName("utf-8"))

    interface ReadCallback {
        fun read(byteArray: ByteArray, num: Int, isFinishFrame: Boolean): Boolean
    }
}
