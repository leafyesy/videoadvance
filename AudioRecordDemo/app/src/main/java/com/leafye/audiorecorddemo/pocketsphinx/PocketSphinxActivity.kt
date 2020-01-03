package com.leafye.audiorecorddemo.pocketsphinx

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import java.lang.ref.WeakReference

import java.io.IOException
import android.widget.TextView
import java.io.File
import com.leafye.audiorecorddemo.R
import edu.cmu.pocketsphinx.*
import kotlinx.android.synthetic.main.activity_pocketsphinx.*
import android.widget.Toast
import android.widget.Toast.makeText

/**
 * 识别指令
 */
class PocketSphinxActivity : AppCompatActivity(), RecognitionListener {

    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PocketSphinxActivity::class.java))
        }

        /* Named searches allow to quickly reconfigure the decoder */
        private const val KWS_SEARCH = "wakeup"
        private const val FORECAST_SEARCH = "forecast"
        private const val DIGITS_SEARCH = "digits"
        private const val PHONE_SEARCH = "phones"
        private const val MENU_SEARCH = "menu"

        /* Keyword we are looking for to activate menu */
        private val KEYPHRASE = "google now"

        /* Used to handle permission request */
        private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    }

    private var recognizer: SpeechRecognizer? = null

    private val captions: HashMap<String, Int> by lazy {
        HashMap<String, Int>().apply {
            put(KWS_SEARCH, R.string.kws_caption)
            put(MENU_SEARCH, R.string.menu_caption)
            put(DIGITS_SEARCH, R.string.digits_caption)
            put(PHONE_SEARCH, R.string.phone_caption)
            put(FORECAST_SEARCH, R.string.forecast_caption)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pocketsphinx)
        SetupTask(this).execute()
    }


    @SuppressLint("StaticFieldLeak")
    private inner class SetupTask(activity: PocketSphinxActivity) :
        AsyncTask<Void, Void, Exception>() {
        val activityReference: WeakReference<PocketSphinxActivity> by lazy {
            WeakReference(activity)
        }

        override fun doInBackground(vararg params: Void?): Exception? {
            try {
                val assets = Assets(activityReference.get())
                val assetDir = assets.syncAssets()
                activityReference.get()!!.setupRecognizer(assetDir)
            } catch (e: IOException) {
                return e
            }
            return null
        }

        override fun onPostExecute(result: Exception?) {
            if (result != null) {
                (activityReference.get()!!.findViewById(R.id.caption_text) as TextView).text =
                    "Failed to init recognizer $result"
            } else {
                activityReference.get()!!.switchSearch(KWS_SEARCH)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recognizer?.let {
            it.cancel()
            it.shutdown()
        }
    }

    private fun setupRecognizer(assetsDir: File) {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
            .setAcousticModel(File(assetsDir, "en-us-ptm"))
            .setDictionary(File(assetsDir, "cmudict-en-us.dict"))
            .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
            .recognizer
            .also {
                it.addListener(this)
                /* In your application you might not need to add all those searches.
                  They are added here for demonstration. You can leave just one.
                 */
                // Create keyword-activation search.
                it.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE)

                // Create grammar-based search for selection between demos
                val menuGrammar = File(assetsDir, "menu.gram")
                it.addGrammarSearch(MENU_SEARCH, menuGrammar)

                // Create grammar-based search for digit recognition
                val digitsGrammar = File(assetsDir, "digits.gram")
                it.addGrammarSearch(DIGITS_SEARCH, digitsGrammar)

                // Create language model search
                val languageModel = File(assetsDir, "weather.dmp")
                it.addNgramSearch(FORECAST_SEARCH, languageModel)

                // Phonetic search
                val phoneticModel = File(assetsDir, "en-phone.dmp")
                it.addAllphoneSearch(PHONE_SEARCH, phoneticModel)
            }
    }

    private fun switchSearch(searchName: String) {
        recognizer?.let {
            it.stop()
            // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
            if (searchName == KWS_SEARCH)
                it.startListening(searchName)
            else
                it.startListening(searchName, 10000)
            captions[searchName]?.let {
                val caption = resources.getString(it)
                (findViewById<TextView>(R.id.caption_text)).text = caption
            }
        }
    }

    override fun onResult(hypothesis: Hypothesis?) {
        result_text.text = ""
        if (hypothesis != null) {
            val text = hypothesis.hypstr
            makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPartialResult(hypothesis: Hypothesis?) {
        if (hypothesis == null)
            return
        when (hypothesis.hypstr) {
            KEYPHRASE -> switchSearch(MENU_SEARCH)
            DIGITS_SEARCH -> switchSearch(DIGITS_SEARCH)
            PHONE_SEARCH -> switchSearch(PHONE_SEARCH)
            FORECAST_SEARCH -> switchSearch(FORECAST_SEARCH)
            else -> result_text.text = hypothesis.hypstr
        }
    }

    override fun onTimeout() {
        switchSearch(KWS_SEARCH)
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onEndOfSpeech() {
        recognizer?.let {
            if (it.searchName != KWS_SEARCH)
                switchSearch(KWS_SEARCH)
        }

    }

    override fun onError(error: java.lang.Exception?) {
        error?.let { caption_text.text = error.message }
    }


}