package com.leafye.gpuimagedemo.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.leafye.base.BaseViewModel
import com.leafye.base.VMProduct
import com.leafye.base.livedata.CallLiveData
import com.leafye.gpuimagedemo.data.FilterItem
import com.leafye.gpuimagedemo.model.MainModel

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.gpuimagedemo.vm
 * @ClassName:      MainViewModel
 * @Description:
 * @Author:         leafye
 * @CreateDate:     2020/3/11 16:16
 * @UpdateUser:
 * @UpdateDate:     2020/3/11 16:16
 * @UpdateRemark:
 */
class MainViewModel(model: MainModel) : BaseViewModel<MainModel>(model) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    val filterItemList = MutableLiveData<MutableList<FilterItem>>()

    fun refreshFilterItemList() {
        filterItemList.value = model.getFilterList()
    }

}

class MainViewModelProduct(private val model: MainModel) : VMProduct<MainViewModel>() {
    override fun isThis(classNew: Class<*>): MainViewModel? =
        with(classNew) {
            if (isAssignableFrom(MainViewModel::class.java)) {
                MainViewModel(model)
            } else null
        }

}