package com.leafye.audiorecorddemo.protogenesis

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.speech.RecognizerIntent
import android.content.Intent
import kotlinx.android.synthetic.main.activity_protogenesis_audio_recognizer.*
import android.util.Log

/**
 * 系统语音识别的功能.比如小米手机的小爱同学
 */
class TestProtogenesisAudioRecognizerActivity : AppCompatActivity() {

    companion object {
        private const val VOICE_RECOGNITION_REQUEST_CODE = 1234
        private const val TAG = "TestProtogenesis"

        fun startActivity(context: Context) {
            context.startActivity(
                Intent(
                    context,
                    TestProtogenesisAudioRecognizerActivity::class.java
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.leafye.audiorecorddemo.R.layout.activity_protogenesis_audio_recognizer)
        val pm = packageManager
        val activities =
            pm.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
        if (activities.size != 0) {
            startRecognizer.isEnabled = true
            startRecognizer.setOnClickListener {
                startVoiceRecognitionActivity()
            }
        } else {
            startRecognizer.isEnabled = false
        }
        back.setOnClickListener { onBackPressed() }
    }

    private fun startVoiceRecognitionActivity() {
        //通过Intent传递语音识别的模式
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            //语言模式和自由形式的语音识别
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            //提示语音开始
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo")
        }
        //开始执行我们的Intent、语音识别
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (VOICE_RECOGNITION_REQUEST_CODE == requestCode && data != null) {
            // 取得语音的字符
            val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            matches?.forEachIndexed { index, s ->
                Log.d(TAG, "返回结果 index:$index   content:$s")
            }
        }
    }

}