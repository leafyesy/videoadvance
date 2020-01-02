package com.leafye.audiorecorddemo.linxi.handler

import android.util.Log
import org.json.JSONObject

class LoggerHandler : AbstractHandler<JSONObject, Any>() {
    override fun handler(input: JSONObject): Any? {
        Log.d("Linxi-log", input.toString())
        return null
    }
}