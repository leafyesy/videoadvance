package com.leafye.audiorecorddemo.audio.utils

import android.util.Log
import com.leafye.audiorecorddemo.audio.utils.ShortAndByte.byteArray2ShortArray
import com.sixin.speex.Speex
import java.io.FileInputStream
import java.io.FileOutputStream

class Raw2Spx {

    private val speex by lazy { Speex() }

    fun raw2spx(inFileName: String, outFileName: String) {
        var rawFileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            rawFileInputStream = FileInputStream(inFileName)
            fileOutputStream = FileOutputStream(outFileName)
            val rawbyte = ByteArray(320)
            val encoded = ByteArray(160)
            //将原数据转换成spx压缩的文件，speex只能编码160字节的数据，需要使用一个循环
            var readedtotal = 0
            var size = 0
            var encodedtotal = 0
            size = rawFileInputStream.read(rawbyte, 0, 320)
            while (size != -1) {
                readedtotal += size
                val rawdata = byteArray2ShortArray(rawbyte)
                val encodesize = speex.encode(rawdata, 0, encoded, rawdata.size)
                fileOutputStream.write(encoded, 0, encodesize)
                encodedtotal += encodesize
                Log.e(
                    "test",
                    "readedtotal $readedtotal\n size$size\n encodesize$encodesize\n encodedtotal$encodedtotal"
                )
                size = rawFileInputStream.read(rawbyte, 0, 320)
            }
            fileOutputStream.close()
            rawFileInputStream.close()
        } catch (e: Exception) {
            Log.e("test", e.toString())
        }

    }

}