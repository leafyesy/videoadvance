package com.leafye.base.livedata

import androidx.lifecycle.MutableLiveData

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.base.livedata
 * @ClassName:      CallLiveData
 * @Description:    用于点击或者事件通知的LiveData
 * @Author:         leafye
 * @CreateDate:     2020/3/11 13:56
 * @UpdateUser:
 * @UpdateDate:     2020/3/11 13:56
 * @UpdateRemark:
 */
class CallLiveData : MutableLiveData<Int>() {


    fun call() {
        value = (value ?: 0) + 1
    }

}