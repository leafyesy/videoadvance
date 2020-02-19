package com.leafye.opengldemo.utils

import android.content.res.Resources
import android.text.TextUtils
import com.leafye.opengldemo.BaseApplication
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.opengldemo.utils
 * @ClassName:      ResUtils
 * @Description:    资源文件读取类
 * @Author:         leafye
 * @CreateDate:     2020/1/22 14:33
 * @UpdateUser:
 * @UpdateDate:     2020/1/22 14:33
 * @UpdateRemark:
 */
class ResUtils {

    companion object{
        fun rawRes(resourceId: Int): String {
            val builder = StringBuilder()
            try {
                val inputStream = BaseApplication.instance.resources.openRawResource(resourceId)
                val streamReader = InputStreamReader(inputStream)

                val bufferedReader = BufferedReader(streamReader)
                var textLine: String? = bufferedReader.readLine()
                while (!TextUtils.isEmpty(textLine)) {
                    builder.append(textLine)
                    builder.append("\n")
                    textLine = bufferedReader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

            return builder.toString()
        }
    }



}