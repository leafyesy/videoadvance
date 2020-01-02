package com.leafye.audiorecorddemo.linxi

import org.java_websocket.drafts.Draft
import org.java_websocket.handshake.ClientHandshakeBuilder
import org.java_websocket.drafts.Draft_17
import org.jetbrains.annotations.NotNull

@SuppressWarnings("deprecation")
class DraftWithOrigin(private val originUrl: String) : Draft_17() {

    override fun copyInstance(): Draft {
        println(originUrl)
        return DraftWithOrigin(originUrl)
    }

    @NotNull
    override fun postProcessHandshakeRequestAsClient(@NotNull request: ClientHandshakeBuilder): ClientHandshakeBuilder {
        super.postProcessHandshakeRequestAsClient(request)
        request.put("Origin", originUrl)
        return request
    }
}