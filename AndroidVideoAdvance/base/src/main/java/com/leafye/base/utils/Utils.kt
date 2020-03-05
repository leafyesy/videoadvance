package com.leafye.base.utils

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Utils {

    private lateinit var context: Context

    fun initConfig(context: Context) {
        this.context = context
    }

    @JvmStatic
    fun getContext(): Context = context

}