package com.leafye.audiorecorddemo

import android.media.AudioRecord
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leafye.audiorecorddemo.adapter.ResultShowAdapter
import com.leafye.audiorecorddemo.audio.AudioRead
import com.leafye.audiorecorddemo.audio.AudioRecorder
import com.leafye.audiorecorddemo.linxi.LinxiWebSocket
import com.leafye.audiorecorddemo.linxi.callback.AudioParseResultCallback
import com.leafye.audiorecorddemo.linxi.callback.WebSocketCallback
import com.leafye.audiorecorddemo.linxi.data.Content
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors.*

class MainActivity : AppCompatActivity() {

    private val audioRecorder: AudioRecorder = AudioRecorder()

    private var showAdapter: ResultShowAdapter? = null

    private val executor = newSingleThreadExecutor()

    private var audioRead: AudioRead? = null


    companion object {
        private const val REQUEST_PERMISSION = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handlePermission()
        initView()
        startRecord.setOnClickListener { startRecord() }
        stopRecord.setOnClickListener { audioRecorder.stop() }
        connectServer.setOnClickListener { startConnect() }
        send.setOnClickListener { sendAudioData() }
        close.setOnClickListener { LinxiWebSocket.close() }
        LinxiWebSocket.registerWebSocketCallback(webSocketCallback)
        LinxiWebSocket.registerAudioParseResultCallback(audioParseResultCallback)
    }

    private fun initView() {
        parseResultRv.layoutManager =
            LinearLayoutManager(this@MainActivity)
                .apply {
                    stackFromEnd = true
                    //reverseLayout = true
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        LinxiWebSocket.unregisterWebSocketCallback(webSocketCallback)
        LinxiWebSocket.unregisterAudioParseResultCallback(audioParseResultCallback)
    }

    private val webSocketCallback: WebSocketCallback by lazy {
        object : WebSocketCallback {

            override fun onOpen() {
                runOnUiThread {
                    statusShow.text = "Ws connect success!"
                }
            }

            override fun onFailure(t: Throwable) {
                runOnUiThread {
                    statusShow.text = "Ws connect failure!"
                }
            }

            override fun onClosing(code: Int, reason: String) {
                runOnUiThread {
                    statusShow.text = "Ws connect closing!"
                }
            }

            override fun onClosed(code: Int, reason: String) {
                runOnUiThread {
                    statusShow.text = "Ws connect close!"
                }
            }
        }
    }

    private val audioParseResultCallback by lazy {
        object : AudioParseResultCallback<Content> {
            override fun parse(result: Content) {
                runOnUiThread {
                    if (showAdapter == null) {
                        showAdapter = ResultShowAdapter(mutableListOf<Content>()
                            .also {
                                it.add(result)
                            })
                        parseResultRv.adapter = showAdapter
                    } else {
                        showAdapter?.addAudioParseResult(result)
                        parseResultRv.smoothScrollToPosition(showAdapter?.itemCount ?: 0)
                    }
                }
            }
        }
    }

    private fun sendAudioData() {
        //循环读取音频数据
        if (audioRead == null) {
            audioRead = AudioRead(audioRecorder.getSavePath(), object : AudioRead.ReadCallback {
                override fun read(byteArray: ByteArray, num: Int, isFinishFrame: Boolean): Boolean {
                    return LinxiWebSocket.send(isFinishFrame, byteArray, num)
                }
            }).also {
                executor.execute(it)
            }
        } else {
            executor.execute(audioRead!!)
        }
    }

    private fun startConnect() {
        LinxiWebSocket.openWebSocket()
    }

    private fun startRecord() {
        val recordState = audioRecorder.getRecordState()
        if (recordState == AudioRecord.STATE_UNINITIALIZED) {
            Toast.makeText(this@MainActivity, "未初始化", Toast.LENGTH_SHORT).show()
            return
        }
        audioRecorder.startRecording()
    }

    private fun handlePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.INTERNET
            ),
            REQUEST_PERMISSION
        )
    }


}
