package com.leafye.audiorecorddemo.utils

import org.apache.commons.codec.binary.Base64
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object EncryptUtil {

    /**
     * 加密数字签名（基于HMACSHA1算法）
     *
     * @param encryptText
     * @param encryptKey
     * @return
     * @throws SignatureException
     */
    @Throws(SignatureException::class)
    fun HmacSHA1Encrypt(encryptText: String, encryptKey: String): String {
        var rawHmac: ByteArray? = null
        try {
            val data = encryptKey.toByteArray(charset("UTF-8"))
            val secretKey = SecretKeySpec(data, "HmacSHA1")
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(secretKey)
            val text = encryptText.toByteArray(charset("UTF-8"))
            rawHmac = mac.doFinal(text)
        } catch (e: InvalidKeyException) {
            throw SignatureException("InvalidKeyException:" + e.message)
        } catch (e: NoSuchAlgorithmException) {
            throw SignatureException("NoSuchAlgorithmException:" + e.message)
        } catch (e: UnsupportedEncodingException) {
            throw SignatureException("UnsupportedEncodingException:" + e.message)
        }

        return String(Base64.encodeBase64(rawHmac))
    }

//    fun MD5(pstr: String): String? {
//        val md5String = charArrayOf(
//            '0',
//            '1',
//            '2',
//            '3',
//            '4',
//            '5',
//            '6',
//            '7',
//            '8',
//            '9',
//            'a',
//            'b',
//            'c',
//            'd',
//            'e',
//            'f'
//        )
//        try {
//            val btInput = pstr.toByteArray()
//            val mdInst = MessageDigest.getInstance("MD5")
//            mdInst.update(btInput)
//            val md = mdInst.digest()
//            val j = md.size
//            val str = CharArray(j * 2)
//            var k = 0
//            for (i in 0 until j) { // i = 0
//                val byte0 = md[i] // 95
//                str[k++] = md5String[byte0 ushr (4) and 0xf] // 5
//                str[k++] = md5String[(byte0 and 0xf).toInt()] // F
//            }
//
//            return String(str)
//        } catch (e: Exception) {
//            return null
//        }
//    }
}
