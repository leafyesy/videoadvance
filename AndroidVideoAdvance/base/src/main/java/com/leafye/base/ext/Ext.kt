package com.leafye.base.ext

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by leafye on 2020/3/12.
 */
fun <T : ViewDataBinding> bindingInflate(
    context: Context, @LayoutRes resId: Int,
    parent: ViewGroup? = null,
    isAttach: Boolean = false
): T {
    return DataBindingUtil.inflate<ViewDataBinding>(
        LayoutInflater.from(context),
        resId,
        parent,
        isAttach
    ) as T
}

fun <T> Any.logi(tag: String = "") = this.also {
    val t =
        if (TextUtils.isEmpty(tag)) {
            this.javaClass.simpleName
        } else {
            tag
        }
    Log.i(t, this.toString())
} as T
