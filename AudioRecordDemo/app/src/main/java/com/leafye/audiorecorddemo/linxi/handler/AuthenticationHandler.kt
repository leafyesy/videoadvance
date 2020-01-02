package com.leafye.audiorecorddemo.linxi.handler

import android.util.Log
import org.json.JSONObject
import java.lang.Exception

/**
 * 鉴权处理
 */
class AuthenticationHandler : AbstractHandler<JSONObject, String>() {
    companion object {
        private const val TAG = "AuthenticationHandler"
    }

    override fun handler(input: JSONObject): String? {
        try {
            val str = input.getString("data")
            Log.d(TAG, "data:$str")
            if (!isNoAuthInfo(str)) {
                return str
            }
        } catch (e: Exception) {
            Log.w(TAG, "fetch data from json error")
        }
        return null
    }

    private fun isNoAuthInfo(str: String): Boolean {
        return !str.startsWith("ist")
    }


}