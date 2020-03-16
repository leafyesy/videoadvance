package com.leafye.ffmpegapp.fragment

import android.content.ContentUris
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.leafye.base.BaseFragment
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.baseNavOptions
import com.leafye.ffmpegapp.databinding.FragmentVideoPlayBinding
import com.leafye.ffmpegapp.utils.ContentUtils
import com.leafye.ffmpegapp.view.VideoSurfaceView
import com.leafye.ffmpegapp.vm.VideoPlayViewModel
import kotlinx.android.synthetic.main.fragment_video_play.*

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.fragment
 * @ClassName:      VideoPlayFragment
 * @Description:    视频播放
 * @Author:         leafye
 * @CreateDate:     2020/3/16 10:15
 * @UpdateUser:
 * @UpdateDate:     2020/3/16 10:15
 * @UpdateRemark:
 */
class VideoPlayFragment : BaseFragment<VideoPlayViewModel, FragmentVideoPlayBinding>() {
    override fun initVariableId() = BR.vm

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = R.layout.fragment_video_play

    companion object {

        private const val TAG = "VideoPlayFragment"
        private const val REQUEST_CODE_SELECT_FILE = 10

        fun startFragment(frag: Fragment) {
            frag.findNavController().navigate(R.id.videoPlayFragment, null, baseNavOptions())
        }
    }

    private val surfaceView by lazy {
        VideoSurfaceView(context).apply {
            callback = object : VideoSurfaceView.Callback {
                override fun surfaceCreated(surface: Surface) {
                    Log.d(TAG, "uiPrepareSuccess")
                    viewModel.uiPrepareSuccess()//界面准备完毕
                }
            }
        }
    }

    override fun setupObservers() {
    }

    override fun fragmentPrepared() {
        getFragActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        surfaceFL.addView(surfaceView)
        selectVideo.setOnClickListener {
            selectVideoPath()
        }
    }

    private fun selectVideoPath() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_FILE) {
            Log.d(TAG, "")
            data?.data?.let { d ->
                ContentUtils.getPath(getFragActivity(), d).also {
                    Log.d(TAG, "filePath:$it")
                }.apply {
                    viewModel.setSelectVideoPath(this)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getFragActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }
}