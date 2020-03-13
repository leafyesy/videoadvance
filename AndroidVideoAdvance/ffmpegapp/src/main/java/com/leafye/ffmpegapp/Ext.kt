package com.leafye.ffmpegapp

import android.util.Log
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.leafye.ffmpegapp.utils.FFmpegTestFileManager
import com.leafye.ffmpegapp.vm.AudioHandleViewModel

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.base
 * @ClassName:      Ext
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/11 14:38
 * @UpdateUser:
 * @UpdateDate:     2020/3/11 14:38
 * @UpdateRemark:
 */

fun baseNavOptions(): NavOptions {
    return navOptions {
        anim {
            enter = R.anim.anim_slide_in_right
            exit = R.anim.anim_slide_out_left
            popEnter = R.anim.anim_slide_in_left
            popExit = R.anim.anim_slide_out_right
        }
    }
}

fun checkPath(vararg path: String?): MutableList<String>? =
    mutableListOf<String>().apply {
        path.forEach {
            if (!FFmpegTestFileManager.checkFile(it)) {
                Log.e("FileCheck", "文件不存在:$it")
                return null
            } else {
                add(it!!)
            }
        }
    }