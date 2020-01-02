package com.leafye.audiorecorddemo.linxi

import com.google.gson.Gson

class LinxiDecoder {

    private val gson by lazy { Gson() }

    fun <T> decoder(json: String, clazz: Class<T>): T {
        return gson.fromJson<T>(json, clazz)
    }

}