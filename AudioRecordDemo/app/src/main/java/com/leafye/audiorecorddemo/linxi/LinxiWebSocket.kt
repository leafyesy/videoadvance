package com.leafye.audiorecorddemo.linxi

import android.text.TextUtils
import android.util.Log
import com.leafye.audiorecorddemo.linxi.callback.AudioParseResultCallback
import com.leafye.audiorecorddemo.linxi.callback.WebSocketCallback
import com.leafye.audiorecorddemo.linxi.data.Content
import com.leafye.audiorecorddemo.linxi.data.ErrorHandler
import com.leafye.audiorecorddemo.linxi.handler.AbstractHandler
import com.leafye.audiorecorddemo.linxi.handler.AudioDataIdentifyHandler
import com.leafye.audiorecorddemo.linxi.handler.AuthenticationHandler
import com.leafye.audiorecorddemo.linxi.handler.LoggerHandler
import okhttp3.*
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject
import org.java_websocket.exceptions.WebsocketNotConnectedException
import java.nio.channels.NotYetConnectedException
import java.nio.charset.Charset

object LinxiWebSocket {

    private const val TAG = "LinxiWebSocket"
    private const val HOST = "webist.lingxicloud.com/ist"
    private const val BASE_URL: String = "ws://$HOST"

    private const val PARAM_LANG = "lang"
    private const val PARAM_CODEC = "codec"
    private const val PARAM_APP_ID = "appid"

    private const val _APP_ID = "5jgdks02"
    private const val _LANG_CN = "cn"
    private const val _LANG_EN = "en"
    private const val _CODEC_RAW = "raw"

    private var webSocket: WebSocket? = null

    private var sId: String? = null
    /**
     * 是否在关闭连接中
     */
    @Volatile
    private var isClosing: Boolean = false
    /**
     * 是否处于请求连接中
     */
    @Volatile
    private var isRequestConnect: Boolean = false

    private val linxiEncoder: LinxiEncoder by lazy { LinxiEncoder() }

    /**
     * ws 状态回调
     */
    private val webSocketCallbackList: MutableList<WebSocketCallback> by lazy {
        mutableListOf<WebSocketCallback>()
    }
    /**
     * 音频数据解析回调
     */
    private val audioParseResultCallbackList: MutableList<AudioParseResultCallback<Content>> by lazy {
        mutableListOf<AudioParseResultCallback<Content>>()
    }

    /**
     * 将数据转换成业务数据
     * 比如:从string数据中转换出鉴权数据
     *      从string数据中转换出语音识别结果
     */
    private val tranHandlerList: MutableList<AbstractHandler<JSONObject, *>> by lazy {
        mutableListOf<AbstractHandler<JSONObject, *>>().apply {
            add(LoggerHandler())
            add(ErrorHandler())
            add(AuthenticationHandler())
            add(AudioDataIdentifyHandler())
        }
    }

    fun openWebSocket() {
        if (webSocket == null && !isRequestConnect) {
            isRequestConnect = true
            val reqBuilder: Request.Builder = Request.Builder()
                .url(buildWsUrl())
            val req = reqBuilder.build()
            OkHttpClient().newWebSocket(req, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    LinxiWebSocket.webSocket = webSocket
                    isClosing = false
                    isRequestConnect = false
                    notifyWsOpen()
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                    handlerMessage(bytes.string(Charset.forName("utf-8")))
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    handlerMessage(text)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    LinxiWebSocket.webSocket = null
                    isClosing = false
                    isRequestConnect = false
                    notifyWsFailure(t)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    isClosing = true
                    notifyWsClosing(code, reason)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    LinxiWebSocket.webSocket = null
                    isClosing = false
                    notifyWsClosed(code, reason)
                }
            })
        } else {
            if (isClosing) {
                Log.w(TAG, "webSocket连接正在关闭中...")
            }
        }
    }

    /**
     * websocket连接是否可用
     */
    fun isWebSocketValid(): Boolean {
        return webSocket != null
    }

    /**
     * 发送数据,将音频文件进行包装
     *
     */
    fun send(isFinishFrame: Boolean, byteArr: ByteArray, number: Int): Boolean {
        if (checkWebSocketValid()) return false
        if (checkAuthIsInit()) return false
        val encoderByte = linxiEncoder.encoder(
            isFinishFrame,
            byteArr,
            number,
            sId!!
        )
        //val buffer = ByteBuffer.wrap(byteArr)
        return try {
            webSocket?.send(encoderByte) ?: false
        } catch (e: IllegalArgumentException) {
            false
        } catch (e: NotYetConnectedException) {
            false
        } catch (e: WebsocketNotConnectedException) {
            false
        }
    }

    fun close(): Boolean {
        if (checkWebSocketValid()) return false
        return try {
            webSocket?.close(1000, "") ?: false
        } catch (e: IllegalArgumentException) {
            false
        } catch (e: NotYetConnectedException) {
            false
        }
    }

    /**
     * 注册websocket回调
     */
    fun registerWebSocketCallback(webSocketCallback: WebSocketCallback) {
        if (!this.webSocketCallbackList.contains(webSocketCallback)) {
            this.webSocketCallbackList.add(webSocketCallback)
        }
    }

    fun unregisterWebSocketCallback(webSocketCallback: WebSocketCallback) {
        this.webSocketCallbackList.remove(webSocketCallback)
    }

    fun registerAudioParseResultCallback(audioParseResultCallback: AudioParseResultCallback<Content>) {
        if (!audioParseResultCallbackList.contains(audioParseResultCallback)) {
            audioParseResultCallbackList.add(audioParseResultCallback)
        }
    }

    fun unregisterAudioParseResultCallback(audioParseResultCallback: AudioParseResultCallback<Content>) {
        audioParseResultCallbackList.remove(audioParseResultCallback)
    }

    private fun handlerMessage(str: String) {
        if (TextUtils.isEmpty(str)) return
        //解析文字
        try {
            val json = JSONObject(str)
            val pair = tranHandler(json)
            handleTranResult(pair.second, pair.first)
        } catch (e: JSONException) {
            Log.w(TAG, "json解析失败-------------------------------")
            Log.w(TAG, str)
            Log.w(TAG, "json解析失败--------------------------------end")
        }
    }

    /**
     * 依次遍历处理的handler,获取处理结果和处理的handler对象
     */
    private fun tranHandler(json: JSONObject): Pair<Any?, AbstractHandler<JSONObject, *>?> {
        var any: Any? = null
        var selectHandler: AbstractHandler<JSONObject, *>? = null
        for (handler in tranHandlerList) {
            val result = handler.handler(json)
            if (result != null) {
                any = result
                selectHandler = handler
                break
            }
        }
        return Pair(any, selectHandler)
    }

    /**
     * 根据解析处理的结果处理
     */
    private fun handleTranResult(tranHandler: AbstractHandler<JSONObject, *>?, result: Any?) {
        if (result == null) return
        when (tranHandler) {
            is AuthenticationHandler -> {
                if (result is String) {
                    sId = result
                }
            }
            is AudioDataIdentifyHandler -> {
                if (result is Content) {
                    notifyParseResult(result)//处理音频处理结果
                }
            }
        }
    }

    private fun checkAuthIsInit(): Boolean {
        if (TextUtils.isEmpty(sId)) {
            Log.w(TAG, "auth is not init!!!")
            return true
        }
        return false
    }

    private fun checkWebSocketValid(): Boolean {
        if (!isWebSocketValid()) {
            Log.w(TAG, "websocket is not open!!!")
            return true
        }
        return false
    }

    private fun buildWsUrl() =
        "$BASE_URL?$PARAM_LANG=$_LANG_EN&$PARAM_CODEC=$_CODEC_RAW&$PARAM_APP_ID=$_APP_ID"

    private fun notifyWsOpen() {
        webSocketCallbackList.forEach { it.onOpen() }
    }

    private fun notifyWsFailure(t: Throwable) {
        webSocketCallbackList.forEach { it.onFailure(t) }
    }

    private fun notifyWsClosing(code: Int, reason: String) {
        webSocketCallbackList.forEach { it.onClosing(code, reason) }
    }

    private fun notifyWsClosed(code: Int, reason: String) {
        webSocketCallbackList.forEach { it.onClosed(code, reason) }
    }

    private fun notifyParseResult(result: Content) {
        audioParseResultCallbackList.forEach { it.parse(result) }
    }
}
