package com.leafye.opengldemo.utils

import android.app.Activity
import android.graphics.Point
import android.hardware.Camera
import android.hardware.Camera.getCameraInfo
import android.view.Surface


/**
 * Created by leafye on 2020-01-27.
 */
class CameraUtils {

    companion object {
        fun calculateCameraPreviewOrientation(activity: Activity, cameraId: Int): Int {
            val info = Camera.CameraInfo()
            getCameraInfo(cameraId, info)
            val rotation = activity.windowManager.defaultDisplay.rotation
            var degrees = 0
            when (rotation) {
                Surface.ROTATION_0 -> degrees = 0
                Surface.ROTATION_90 -> degrees = 90
                Surface.ROTATION_180 -> degrees = 180
                Surface.ROTATION_270 -> degrees = 270
            }
            var result: Int
            if (Camera.CameraInfo.CAMERA_FACING_FRONT === info.facing) {
                result = (info.orientation + degrees) % 360
                result = (360 - result) % 360
            } else {
                result = (info.orientation - degrees + 360) % 360
            }
            return result
        }

        fun findProperSize(surfaceSize: Point, sizeList: List<Camera.Size>?): Camera.Size? {
            if (surfaceSize.x <= 0 || surfaceSize.y <= 0 || sizeList == null) {
                return null
            }
            val surfaceWidth = surfaceSize.x
            val surfaceHeight = surfaceSize.y

            val ratioListList = ArrayList<ArrayList<Camera.Size>>()
            for (size in sizeList) {
                addRatioList(ratioListList, size)
            }
            val surfaceRatio = surfaceWidth.toFloat() / surfaceHeight
            var bestRatioList: List<Camera.Size>? = null
            var ratioDiff = java.lang.Float.MAX_VALUE
            for (ratioList in ratioListList) {
                val ratio = ratioList.get(0).width.toFloat() / ratioList.get(0).height
                val newRatioDiff = Math.abs(ratio - surfaceRatio)
                if (newRatioDiff < ratioDiff) {
                    bestRatioList = ratioList
                    ratioDiff = newRatioDiff
                }
            }
            var bestSize: Camera.Size? = null
            var diff = Integer.MAX_VALUE
            assert(bestRatioList != null)
            for (size in bestRatioList!!) {
                val newDiff =
                    Math.abs(size.width - surfaceWidth) + Math.abs(size.height - surfaceHeight)
                if (size.height >= surfaceHeight && newDiff < diff) {
                    bestSize = size
                    diff = newDiff
                }
            }
            if (bestSize != null) {
                return bestSize
            }
            diff = Integer.MAX_VALUE
            for (size in bestRatioList) {
                val newDiff =
                    Math.abs(size.width - surfaceWidth) + Math.abs(size.height - surfaceHeight)
                if (newDiff < diff) {
                    bestSize = size
                    diff = newDiff
                }
            }

            return bestSize
        }

        private fun addRatioList(
            ratioListList: MutableList<ArrayList<Camera.Size>>,
            size: Camera.Size
        ) {
            val ratio = size.width.toFloat() / size.height
            for (ratioList in ratioListList) {
                val mine = ratioList[0].width.toFloat() / ratioList[0].height
                if (ratio == mine) {
                    ratioList.add(size)
                    return
                }
            }
            val ratioList = ArrayList<Camera.Size>()
            ratioList.add(size)
            ratioListList.add(ratioList)
        }

        fun chooseFixedPreviewFps(parameters: Camera.Parameters, expectedThoudandFps: Int): Int {
            val supportedFps = parameters.supportedPreviewFpsRange
            for (entry in supportedFps) {
                if (entry[0] == entry[1] && entry[0] == expectedThoudandFps) {
                    parameters.setPreviewFpsRange(entry[0], entry[1])
                    return entry[0]
                }
            }
            val temp = IntArray(2)
            parameters.getPreviewFpsRange(temp)
            return if (temp[0] == temp[1]) {
                temp[0]
            } else {
                temp[1] / 2
            }
        }


    }

}