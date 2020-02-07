package com.leafye.opengldemo.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

/**
 * Created by leafye on 2020-02-04.
 */
class PermissionUtils {

    companion object {
        /**
         * 权限组检查和请求
         */
        fun checkPermissionM(
            activity: Activity,
            requestCode: Int,
            vararg permissionVararg: String,
            isRequest: Boolean = true
        ): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionVararg.filter {
                    activity.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
                }.takeIf {
                    it.isNotEmpty()
                }?.let {
                    if (isRequest)
                        ActivityCompat.requestPermissions(activity, it.toTypedArray(), requestCode)
                    return false
                }
            }
            return true
        }
    }

}