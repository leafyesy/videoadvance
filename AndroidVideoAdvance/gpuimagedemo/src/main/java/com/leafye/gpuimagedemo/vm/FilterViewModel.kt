package com.leafye.gpuimagedemo.vm

import android.graphics.BitmapFactory
import android.graphics.PointF
import androidx.lifecycle.MutableLiveData
import com.leafye.base.utils.Utils
import com.leafye.gpuimagedemo.R
import com.leafye.gpuimagedemo.data.FilterItem
import jp.co.cyberagent.android.gpuimage.filter.*

/**
 * Created by leafye on 2020/3/12.
 */
class FilterViewModel {

    val gpuImageFilter = MutableLiveData<GPUImageFilter?>()

    fun filterClick(filterItem: FilterItem) {
        gpuImageFilter.value = when (filterItem.type) {
            FilterItem.FilterType.CONTRAST -> GPUImageContrastFilter(2.0F)
            FilterItem.FilterType.GRAYSCALE -> GPUImageGrayscaleFilter()
            FilterItem.FilterType.GAMMA -> GPUImageGammaFilter(2.0f)
            FilterItem.FilterType.INVERT -> GPUImageColorInvertFilter()
            FilterItem.FilterType.PIXELATION -> GPUImagePixelationFilter()
            FilterItem.FilterType.HUE -> GPUImageHueFilter(90.0f)
            FilterItem.FilterType.BRIGHTNESS -> GPUImageBrightnessFilter(1.5f)
            FilterItem.FilterType.SEPIA -> GPUImageSepiaToneFilter()
            FilterItem.FilterType.SHARPEN -> GPUImageSharpenFilter()
            FilterItem.FilterType.SOBEL_EDGE_DETECTION -> GPUImageSobelEdgeDetectionFilter()
            FilterItem.FilterType.THRESHOLD_EDGE_DETECTION -> GPUImageThresholdEdgeDetectionFilter()
            FilterItem.FilterType.THREE_X_THREE_CONVOLUTION -> GPUImage3x3ConvolutionFilter()
            FilterItem.FilterType.EMBOSS -> GPUImageEmbossFilter()
            FilterItem.FilterType.POSTERIZE -> GPUImagePosterizeFilter()
            FilterItem.FilterType.FILTER_GROUP -> GPUImageFilterGroup(
                listOf(
                    GPUImageContrastFilter(),
                    GPUImageDirectionalSobelEdgeDetectionFilter(),
                    GPUImageGrayscaleFilter()
                )
            )
            FilterItem.FilterType.SATURATION -> GPUImageSaturationFilter(1.0f)
            FilterItem.FilterType.EXPOSURE -> GPUImageExposureFilter(0.0f)
            FilterItem.FilterType.HIGHLIGHT_SHADOW -> GPUImageHighlightShadowFilter(
                0.0f,
                1.0f
            )
            FilterItem.FilterType.MONOCHROME -> GPUImageMonochromeFilter(
                1.0f, floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)
            )
            FilterItem.FilterType.OPACITY -> GPUImageOpacityFilter(1.0f)
            FilterItem.FilterType.RGB -> GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            FilterItem.FilterType.WHITE_BALANCE -> GPUImageWhiteBalanceFilter(
                5000.0f,
                0.0f
            )
            FilterItem.FilterType.VIGNETTE -> GPUImageVignetteFilter(
                PointF(0.5f, 0.5f),
                floatArrayOf(0.0f, 0.0f, 0.0f),
                0.3f,
                0.75f
            )
            FilterItem.FilterType.BLEND_DIFFERENCE -> createBlendFilter(GPUImageDifferenceBlendFilter::class.java)
            FilterItem.FilterType.BLEND_SOURCE_OVER -> createBlendFilter(GPUImageSourceOverBlendFilter::class.java)
            FilterItem.FilterType.BLEND_COLOR_BURN -> createBlendFilter(GPUImageColorBurnBlendFilter::class.java)
            FilterItem.FilterType.BLEND_COLOR_DODGE -> createBlendFilter(GPUImageColorDodgeBlendFilter::class.java)
            FilterItem.FilterType.BLEND_DARKEN -> createBlendFilter(GPUImageDarkenBlendFilter::class.java)
            FilterItem.FilterType.BLEND_DISSOLVE -> createBlendFilter(GPUImageDissolveBlendFilter::class.java)
            FilterItem.FilterType.BLEND_EXCLUSION -> createBlendFilter(GPUImageExclusionBlendFilter::class.java)
            FilterItem.FilterType.BLEND_HARD_LIGHT -> createBlendFilter(GPUImageHardLightBlendFilter::class.java)
            FilterItem.FilterType.BLEND_LIGHTEN -> createBlendFilter(GPUImageLightenBlendFilter::class.java)
            FilterItem.FilterType.BLEND_ADD -> createBlendFilter(GPUImageAddBlendFilter::class.java)
            FilterItem.FilterType.BLEND_DIVIDE -> createBlendFilter(GPUImageDivideBlendFilter::class.java)
            FilterItem.FilterType.BLEND_MULTIPLY -> createBlendFilter(GPUImageMultiplyBlendFilter::class.java)
            FilterItem.FilterType.BLEND_OVERLAY -> createBlendFilter(GPUImageOverlayBlendFilter::class.java)
            FilterItem.FilterType.BLEND_SCREEN -> createBlendFilter(GPUImageScreenBlendFilter::class.java)
            FilterItem.FilterType.BLEND_ALPHA -> createBlendFilter(GPUImageAlphaBlendFilter::class.java)
            FilterItem.FilterType.BLEND_COLOR -> createBlendFilter(GPUImageColorBlendFilter::class.java)
            FilterItem.FilterType.BLEND_HUE -> createBlendFilter(GPUImageHueBlendFilter::class.java)
            FilterItem.FilterType.BLEND_SATURATION -> createBlendFilter(GPUImageSaturationBlendFilter::class.java)
            FilterItem.FilterType.BLEND_LUMINOSITY -> createBlendFilter(GPUImageLuminosityBlendFilter::class.java)
            FilterItem.FilterType.BLEND_LINEAR_BURN -> createBlendFilter(GPUImageLinearBurnBlendFilter::class.java)
            FilterItem.FilterType.BLEND_SOFT_LIGHT -> createBlendFilter(GPUImageSoftLightBlendFilter::class.java)
            FilterItem.FilterType.BLEND_SUBTRACT -> createBlendFilter(GPUImageSubtractBlendFilter::class.java)
            FilterItem.FilterType.BLEND_CHROMA_KEY -> createBlendFilter(GPUImageChromaKeyBlendFilter::class.java)
            FilterItem.FilterType.BLEND_NORMAL -> createBlendFilter(GPUImageNormalBlendFilter::class.java)
            FilterItem.FilterType.GAUSSIAN_BLUR -> GPUImageGaussianBlurFilter()
            FilterItem.FilterType.CROSSHATCH -> GPUImageCrosshatchFilter()
            FilterItem.FilterType.BOX_BLUR -> GPUImageBoxBlurFilter()
            FilterItem.FilterType.CGA_COLORSPACE -> GPUImageCGAColorspaceFilter()
            FilterItem.FilterType.DILATION -> GPUImageDilationFilter()
            FilterItem.FilterType.KUWAHARA -> GPUImageKuwaharaFilter()
            FilterItem.FilterType.RGB_DILATION -> GPUImageRGBDilationFilter()
            FilterItem.FilterType.SKETCH -> GPUImageSketchFilter()
            FilterItem.FilterType.TOON -> GPUImageToonFilter()
            FilterItem.FilterType.SMOOTH_TOON -> GPUImageSmoothToonFilter()
            FilterItem.FilterType.BULGE_DISTORTION -> GPUImageBulgeDistortionFilter()
            FilterItem.FilterType.GLASS_SPHERE -> GPUImageGlassSphereFilter()
            FilterItem.FilterType.HAZE -> GPUImageHazeFilter()
            FilterItem.FilterType.LAPLACIAN -> GPUImageLaplacianFilter()
            FilterItem.FilterType.NON_MAXIMUM_SUPPRESSION -> GPUImageNonMaximumSuppressionFilter()
            FilterItem.FilterType.SPHERE_REFRACTION -> GPUImageSphereRefractionFilter()
            FilterItem.FilterType.SWIRL -> GPUImageSwirlFilter()
            FilterItem.FilterType.WEAK_PIXEL_INCLUSION -> GPUImageWeakPixelInclusionFilter()
            FilterItem.FilterType.FALSE_COLOR -> GPUImageFalseColorFilter()
            FilterItem.FilterType.COLOR_BALANCE -> GPUImageColorBalanceFilter()
            FilterItem.FilterType.LEVELS_FILTER_MIN -> GPUImageLevelsFilter()
            FilterItem.FilterType.HALFTONE -> GPUImageHalftoneFilter()
            FilterItem.FilterType.BILATERAL_BLUR -> GPUImageBilateralBlurFilter()
            FilterItem.FilterType.TRANSFORM2D -> GPUImageTransformFilter()
            //FilterItem.FilterType.TONE_CURVE -> GPUImageToneCurveFilter().apply {
            //    setFromCurveFileInputStream(
            //        Utils.getContext().resources.openRawResource(R.raw.tone_cuver_sample)
            //    )
            //}
            //FilterItem.FilterType.LUMINANCE -> GPUImageLuminanceFilter()
            //FilterItem.FilterType.LUMINANCE_THRESHSOLD -> GPUImageLuminanceThresholdFilter(0.5f)
            //FilterItem.FilterType.LOOKUP_AMATORKA -> GPUImageLookupFilter().apply {
            //    bitmap = BitmapFactory.decodeResource(R.drawable.lookup_amatorka)
            //}
            //FilterItem.FilterType.ZOOM_BLUR -> GPUImageZoomBlurFilter()
            //FilterItem.FilterType.SOLARIZE -> GPUImageSolarizeFilter()
            //FilterItem.FilterType.VIBRANCE -> GPUImageVibranceFilter()
            else -> null
        }
    }

    private fun createBlendFilter(
        filterClass: Class<out GPUImageTwoInputFilter>
    ): GPUImageFilter {
        return try {
            filterClass.newInstance().apply {
                bitmap =
                    BitmapFactory.decodeResource(Utils.getContext().resources, R.mipmap.time)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GPUImageFilter()
        }
    }

}