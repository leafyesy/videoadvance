package com.leafye.audiorecorddemo.tanslate

import android.util.Log
import com.lingxi.voicesupport.Translate.TranslateTools
import com.lingxi.voicesupport.Translate.entity.TranslateResult
import com.lingxi.voicesupport.Translate.listener.LXTranslateResultListener
import java.util.ArrayList

object TranslateManager {

    private const val TAG = "TranslateManager"

    private val tranL by lazy {
        object : LXTranslateResultListener {
            override fun translateResult(requestId: String?, p1: ArrayList<TranslateResult>?) {
                Log.d(
                    TAG,
                    "------------requestId:$requestId -------start----------------------------"
                )
                p1?.forEach {
                    Log.d(TAG, ">>$it")
                }
                Log.d(
                    TAG,
                    "------------requestId:$requestId --------end-----------------------------"
                )
            }

            override fun translateError(requestId: String?, p1: String?) {
                Log.d(
                    TAG,
                    "------------requestId:$requestId --------error$p1------------------------"
                )
            }
        }
    }

    fun translate(source: String, requestId: String) {
        TranslateTools.getInstance()
            .getTranslateResult(
                TranslateTools.LANG_CN,
                TranslateTools.LANG_EN,
                source,
                requestId,
                tranL
            )
    }


}