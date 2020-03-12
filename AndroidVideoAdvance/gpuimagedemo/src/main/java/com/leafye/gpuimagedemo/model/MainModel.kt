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
class MainModel : BaseModel {

    fun getFilterList(): MutableList<FilterItem> {
        return mutableListOf<FilterItem>().apply {
            add(FilterItem("contrast", FilterItem.FilterType.CONTRAST))
            add(FilterItem("grayscale", FilterItem.FilterType.GRAYSCALE))
            add(FilterItem("sharpen", FilterItem.FilterType.SHARPEN))
            add(FilterItem("sepia", FilterItem.FilterType.SEPIA))
            add(FilterItem("sobel_edge_detection", FilterItem.FilterType.SOBEL_EDGE_DETECTION))
            add(
                FilterItem(
                    "threshold_edge_detection",
                    FilterItem.FilterType.THRESHOLD_EDGE_DETECTION
                )
            )
            add(
                FilterItem(
                    "three_x_three_convolution",
                    FilterItem.FilterType.THREE_X_THREE_CONVOLUTION
                )
            )
            add(FilterItem("filter_group", FilterItem.FilterType.FILTER_GROUP))
            add(FilterItem("emboss", FilterItem.FilterType.EMBOSS))
            add(FilterItem("posterize", FilterItem.FilterType.POSTERIZE))
            add(FilterItem("gamma", FilterItem.FilterType.GAMMA))
            add(FilterItem("brightness", FilterItem.FilterType.BRIGHTNESS))
            add(FilterItem("invert", FilterItem.FilterType.INVERT))
            add(FilterItem("hue", FilterItem.FilterType.HUE))
            add(FilterItem("pixelation", FilterItem.FilterType.PIXELATION))
            add(FilterItem("saturation", FilterItem.FilterType.SATURATION))
            add(FilterItem("exposure", FilterItem.FilterType.EXPOSURE))
            add(FilterItem("highlight_shadow", FilterItem.FilterType.HIGHLIGHT_SHADOW))
            add(FilterItem("monochrome", FilterItem.FilterType.MONOCHROME))
            add(FilterItem("opacity", FilterItem.FilterType.OPACITY))
            add(FilterItem("rgb", FilterItem.FilterType.RGB))
            add(FilterItem("white_balance", FilterItem.FilterType.WHITE_BALANCE))
            add(FilterItem("vignette", FilterItem.FilterType.VIGNETTE))
            add(FilterItem("tone_curve", FilterItem.FilterType.TONE_CURVE))
            add(FilterItem("luminance", FilterItem.FilterType.LUMINANCE))
            add(FilterItem("luminance_threshsold", FilterItem.FilterType.LUMINANCE_THRESHSOLD))
            add(FilterItem("blend_color_burn", FilterItem.FilterType.BLEND_COLOR_BURN))
            add(FilterItem("blend_color_dodge", FilterItem.FilterType.BLEND_COLOR_DODGE))
            add(FilterItem("blend_darken", FilterItem.FilterType.BLEND_DARKEN))
            add(FilterItem("blend_difference", FilterItem.FilterType.BLEND_DIFFERENCE))
            add(FilterItem("blend_dissolve", FilterItem.FilterType.BLEND_DISSOLVE))
            add(FilterItem("blend_exclusion", FilterItem.FilterType.BLEND_EXCLUSION))
            add(FilterItem("blend_source_over", FilterItem.FilterType.BLEND_SOURCE_OVER))
            add(FilterItem("blend_hard_light", FilterItem.FilterType.BLEND_HARD_LIGHT))
            add(FilterItem("blend_lighten", FilterItem.FilterType.BLEND_LIGHTEN))
            add(FilterItem("blend_add", FilterItem.FilterType.BLEND_ADD))
            add(FilterItem("blend_divide`", FilterItem.FilterType.BLEND_DIVIDE))
        }
    }


}