package com.leafye.audiorecorddemo.linxi.handler

import android.util.Log
import com.leafye.audiorecorddemo.linxi.LinxiDecoder
import com.leafye.audiorecorddemo.linxi.data.Content
import org.json.JSONException
import org.json.JSONObject

/**
 * 音频数据识别结果处理handler
 */
class AudioDataIdentifyHandler : AbstractHandler<JSONObject, Content>() {
    companion object {
        private const val TAG = "AudioDataIdentify"
    }

    private val linxiDecoder: LinxiDecoder by lazy { LinxiDecoder() }

    override fun handler(input: JSONObject): Content? {
        try {
            val get = input.get("data")
            if (get is String && isJson(get)) {
                return linxiDecoder.decoder(get, Content::class.java)
            }
        } catch (e: JSONException) {
            Log.w(TAG, "AudioDataIdentifyHandler get 'data' error!$e")
        }
        return null
    }

    //判断一个字符串是否为json字符串
    private fun isJson(str: String): Boolean {
        return (str.startsWith("{\"cn\""))
    }
}