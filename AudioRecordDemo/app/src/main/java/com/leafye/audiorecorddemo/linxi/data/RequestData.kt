package com.leafye.audiorecorddemo.linxi.data

import com.google.gson.annotations.SerializedName

/**
 * status: 2 开始进行转写 4 音频发送结束
 * samples:音频数据字节数组
 * snumber:数据序号,从1开始
 * sid:第一步建立连接取得的sid
 */
data class RequestData(
    @SerializedName("status")
    val status: Int,
    //@SerializedName("samples")
    val samples: ByteArray,
    @SerializedName("snumber")
    val snumber: Int,
    @SerializedName("sid")
    val sid: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RequestData

        if (status != other.status) return false
        if (!samples.contentEquals(other.samples)) return false
        if (snumber != other.snumber) return false
        if (sid != other.sid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status
        result = 31 * result + samples.contentHashCode()
        result = 31 * result + snumber
        result = 31 * result + sid.hashCode()
        return result
    }

    override fun toString(): String {
        return super.toString()
    }
}