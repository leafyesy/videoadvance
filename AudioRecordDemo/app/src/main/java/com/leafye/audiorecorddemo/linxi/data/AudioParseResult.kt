package com.leafye.audiorecorddemo.linxi.data

import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("cn")
    val cn: CnX,
    @SerializedName("seg_id")
    val segId: Int
) {
    fun getResultStr(): String {
        return StringBuilder().apply {
            cn.st.rt.forEach { rt ->
                rt.ws.forEach { wList ->
                    wList.cw.forEach { w ->
                        append(w.w)
                    }
                }
            }
        }.toString()
    }
}

data class CnX(
    @SerializedName("st")
    val st: St
)

data class St(
    @SerializedName("bg")
    val bg: String,
    @SerializedName("ed")
    val ed: String,
    @SerializedName("rt")
    val rt: List<Rt>,
    @SerializedName("type")
    val type: String
)

data class Rt(
    @SerializedName("ws")
    val ws: List<W>
)

data class W(
    @SerializedName("cw")
    val cw: List<Cw>,
    @SerializedName("wb")
    val wb: Int,
    @SerializedName("we")
    val we: Int
)

data class Cw(
    @SerializedName("w")
    val w: String,
    @SerializedName("wp")
    val wp: String
)

////解析的内容
//class Content(
//    @SerializedName("st")
//    val start: Start,
//    @SerializedName("seg_id")
//    val segId: Int
//){
//    fun getResultStr(): String {
//        val sb = StringBuilder()
//        start.rtList.forEach { rt ->
//            rt.wordList.forEach { wList ->
//                wList.cWordList.forEach { w ->
//                    sb.append(w.word)
//                }
//            }
//        }
//        return ""
//    }
//}
//
//class Start(
//    /**
//     * 句子开始时间(中间结果的bg为准确值)
//     */
//    @SerializedName("bg")
//    val begin: String,
//    /**
//     * 句子结束时间(中间结果的ed为0)
//     */
//    @SerializedName("ed")
//    val end: String,
//
//    @SerializedName("rt")
//    var rtList: MutableList<Rt>,
//    /**
//     * 结果类型标识
//     */
//    @SerializedName("type")
//    var type: String
//) {
//    companion object {
//        const val TYPE_END = 0
//        const val TYPE_MID = 1
//    }
//}
//
//class Rt(
//    @SerializedName("ws")
//    val wordList: MutableList<WordList>
//)
//
///**
// * 转换的词列表
// */
//class WordList(
//    @SerializedName("cw")
//    val cWordList: MutableList<CWord>,
//    /**
//     * 词开始时间
//     */
//    @SerializedName("wb")
//    val wordBegin: Int,
//    /**
//     * 词结束时间
//     */
//    @SerializedName("we")
//    val wordEnd: Int
//)
//
///**
// * 转换的词
// */
//class CWord(
//    /**
//     * 词识别内容
//     */
//    @SerializedName("w")
//    val word: String,
//    /**
//     * 词标识
//     */
//    @SerializedName("wp")
//    val wordP: String
//) {
//    companion object {
//        /**
//         * 词标识:普通词
//         */
//        const val WP_NORMAL = "n"
//        /**
//         * 词标识:顺滑词
//         */
//        const val WP_SMOOTH = "s"
//        /**
//         * 词标识:标点
//         */
//        const val WP_PUNCTUATION = "p"
//    }
//}