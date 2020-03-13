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
            add(FilterItem("BLEND_MULTIPLY`", FilterItem.FilterType.BLEND_MULTIPLY))
            add(FilterItem("BLEND_OVERLAY", FilterItem.FilterType.BLEND_OVERLAY))
            add(FilterItem("BLEND_SCREEN", FilterItem.FilterType.BLEND_SCREEN))
            add(FilterItem("BLEND_ALPHA", FilterItem.FilterType.BLEND_ALPHA))
            add(FilterItem("BLEND_COLOR", FilterItem.FilterType.BLEND_COLOR))
            add(FilterItem("BLEND_HUE", FilterItem.FilterType.BLEND_HUE))
            add(FilterItem("BLEND_SATURATION", FilterItem.FilterType.BLEND_SATURATION))
            add(FilterItem("BLEND_LUMINOSITY", FilterItem.FilterType.BLEND_LUMINOSITY))
            add(FilterItem("BLEND_LINEAR_BURN", FilterItem.FilterType.BLEND_LINEAR_BURN))
            add(FilterItem("BLEND_SOFT_LIGHT", FilterItem.FilterType.BLEND_SOFT_LIGHT))
            add(FilterItem("BLEND_SUBTRACT", FilterItem.FilterType.BLEND_SUBTRACT))
            add(FilterItem("BLEND_CHROMA_KEY", FilterItem.FilterType.BLEND_CHROMA_KEY))
            add(FilterItem("BLEND_NORMAL", FilterItem.FilterType.BLEND_NORMAL))
            add(FilterItem("LOOKUP_AMATORKA", FilterItem.FilterType.LOOKUP_AMATORKA))
            add(FilterItem("GAUSSIAN_BLUR", FilterItem.FilterType.GAUSSIAN_BLUR))
            add(FilterItem("CROSSHATCH", FilterItem.FilterType.CROSSHATCH))
            add(FilterItem("BOX_BLUR", FilterItem.FilterType.BOX_BLUR))
            add(FilterItem("CGA_COLORSPACE", FilterItem.FilterType.CGA_COLORSPACE))
            add(FilterItem("DILATION", FilterItem.FilterType.DILATION))
            add(FilterItem("KUWAHARA", FilterItem.FilterType.KUWAHARA))
            add(FilterItem("RGB_DILATION", FilterItem.FilterType.RGB_DILATION))
            add(FilterItem("SKETCH", FilterItem.FilterType.SKETCH))
            add(FilterItem("SMOOTH_TOON", FilterItem.FilterType.SMOOTH_TOON))
            add(FilterItem("BULGE_DISTORTION", FilterItem.FilterType.BULGE_DISTORTION))
            add(FilterItem("GLASS_SPHERE", FilterItem.FilterType.GLASS_SPHERE))
            add(FilterItem("HAZE", FilterItem.FilterType.HAZE))
            add(FilterItem("LAPLACIAN", FilterItem.FilterType.LAPLACIAN))
            add(
                FilterItem(
                    "NON_MAXIMUM_SUPPRESSION",
                    FilterItem.FilterType.NON_MAXIMUM_SUPPRESSION
                )
            )
            add(FilterItem("SPHERE_REFRACTION", FilterItem.FilterType.SPHERE_REFRACTION))
            add(FilterItem("SWIRL", FilterItem.FilterType.SWIRL))
            add(FilterItem("WEAK_PIXEL_INCLUSION", FilterItem.FilterType.WEAK_PIXEL_INCLUSION))
            add(FilterItem("FALSE_COLOR", FilterItem.FilterType.FALSE_COLOR))
            add(FilterItem("COLOR_BALANCE", FilterItem.FilterType.COLOR_BALANCE))
            add(FilterItem("LEVELS_FILTER_MIN", FilterItem.FilterType.LEVELS_FILTER_MIN))
            add(FilterItem("BILATERAL_BLUR", FilterItem.FilterType.BILATERAL_BLUR))
            add(FilterItem("ZOOM_BLUR", FilterItem.FilterType.ZOOM_BLUR))
            add(FilterItem("HALFTONE", FilterItem.FilterType.HALFTONE))
            add(FilterItem("TRANSFORM2D", FilterItem.FilterType.TRANSFORM2D))
            add(FilterItem("SOLARIZE", FilterItem.FilterType.SOLARIZE))
            add(FilterItem("VIBRANCE", FilterItem.FilterType.VIBRANCE))
        }
    }


}