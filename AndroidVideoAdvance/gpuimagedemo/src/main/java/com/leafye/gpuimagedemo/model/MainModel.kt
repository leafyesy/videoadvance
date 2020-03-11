package com.leafye.gpuimagedemo.model

import com.leafye.base.BaseModel
import com.leafye.gpuimagedemo.data.FilterItem

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.gpuimagedemo.model
 * @ClassName:      MainModel
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/11 16:17
 * @UpdateUser:
 * @UpdateDate:     2020/3/11 16:17
 * @UpdateRemark:
 */
class MainModel: BaseModel {

    fun getFilterList():MutableList<FilterItem>{
        return mutableListOf<FilterItem>().apply{
            add(FilterItem("holo"))
            add(FilterItem("lemon"))
        }
    }


}