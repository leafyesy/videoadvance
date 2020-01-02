package com.leafye.audiorecorddemo.linxi.data

import android.util.Log
import com.leafye.audiorecorddemo.linxi.handler.AbstractHandler
import org.json.JSONException
import org.json.JSONObject

class ErrorHandler : AbstractHandler<JSONObject, Error>() {
    companion object {
        private const val TAG = "ErrorHandler"
    }

    override fun handler(input: JSONObject): Error? {
        try {
            val code = input.getInt("errcode")
            if (code != 0) {
                val msg = input.getString("msg")
                Log.w(TAG, "errCode:$code  msg:$msg")
                return Error(code, msg)
            }
        } catch (e: JSONException) {

        }
        return null
    }

}

class Error(val errCode: Int, val msg: String)