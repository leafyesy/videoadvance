package com.leafye.gpuimagedemo.vm

import androidx.lifecycle.MutableLiveData
import com.leafye.gpuimagedemo.data.FilterItem
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter

/**
 * Created by leafye on 2020/3/12.
 */
class FilterViewModel {

    val gpuImageFilter = MutableLiveData<GPUImageFilter?>()

    fun filterClick(filterItem: FilterItem) {
        gpuImageFilter.value = when (filterItem.type) {
            FilterItem.FilterType.CONTRAST -> GPUImageContrastFilter(2.0F)
            FilterItem.FilterType.GRAYSCALE -> GPUImageGrayscaleFilter()

            else -> null
        }
    }

}