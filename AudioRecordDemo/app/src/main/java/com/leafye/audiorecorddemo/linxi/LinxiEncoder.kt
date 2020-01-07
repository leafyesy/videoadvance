package com.leafye.audiorecorddemo.linxi

import org.json.JSONArray
import org.json.JSONObject
import kotlin.experimental.and

class LinxiEncoder {

    companion object {
        private const val TAG = "LinxiEncoder"
    }

    fun encoder(isFinishFrame: Boolean, byteArr: ByteArray, snumber: Int, sid: String): String {
        val apply = JSONObject()
            .apply {
                put("Status", if (isFinishFrame) 4 else 2)
                put("snumber", snumber)
                put("sid", sid)
                val jsonArray = JSONArray().apply {
                    byteArr.forEach {
                        put(it.toShort() and 0xFF)//+0x80  128
                        //put(it)
                    }//对数据进行偏移
                }
                put("samples", jsonArray)
            }
        //val toJson = gson.toJson(RequestData(if (isFinishFrame) 4 else 2, byteArr, snumber, sid))
        //Log.d(TAG, "encode:$apply")
        return apply.toString()
    }

}