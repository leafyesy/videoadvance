package com.leafye.audiorecorddemo.linxi.data

import com.google.gson.annotations.SerializedName

open class BaseData {

    @SerializedName("errcode")
    val errcode: Int = 0

    @SerializedName("msg")
    var msg: String = ""

    fun isOk(): Boolean {
        return errcode == 0
    }

}