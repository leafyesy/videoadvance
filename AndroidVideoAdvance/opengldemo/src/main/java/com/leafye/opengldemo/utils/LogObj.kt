package com.leafye.opengldemo.utils

import android.annotation.SuppressLint
import android.util.Log
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class LogObj {


    companion object {

        private const val TAG = "LogObj"

        fun toString(any: Any): String {
            val sb by lazy { StringBuilder() }
            try {
                val clazz = any::class.java
                val declaredFields = clazz.declaredFields
                sb.append(clazz.simpleName)
                declaredFields.forEach {
                    sb.append(it.name)
                        .append("=")
                        .append(it.get(any))
                        .append(";")
                }
            } catch (e: Exception) {
                Log.w(TAG, e.toString())
            }
            return sb.toString()
        }

        @SuppressLint("SimpleDateFormat")
        fun logException(tag: String, t: Throwable) {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS")
            val format = sdf.format(Date())
            Log.w(tag, "$format [${t.printStackTrace()}]")
        }


    }

}

fun Any?.logNull(tag: String): Any? {
    val element = Thread.currentThread().stackTrace[2]
    return this.also {
        Log.w(tag, "${element.methodName} ${element.className}==null")
    }
}