package com.leafye.ffmpegapp.fragment.base

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.leafye.base.BaseFragment
import com.leafye.base.BaseModel
import com.leafye.base.BaseViewModel

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.fragment.base
 * @ClassName:      SelectFileFragment
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/18 16:48
 * @UpdateUser:
 * @UpdateDate:     2020/3/18 16:48
 * @UpdateRemark:
 */
abstract class SelectFileFragment<VM : BaseViewModel<out BaseModel>, B : ViewDataBinding> :
    BaseFragment<VM, B>() {

    protected fun selectVideoPath(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(intent, requestCode)
    }

}