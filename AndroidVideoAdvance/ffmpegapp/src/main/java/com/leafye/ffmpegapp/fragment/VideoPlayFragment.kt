package com.leafye.ffmpegapp.fragment

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.baseNavOptions
import com.leafye.ffmpegapp.databinding.FragmentVideoPlayBinding
import com.leafye.ffmpegapp.fragment.base.SelectFileFragment
import com.leafye.ffmpegapp.utils.ContentUtils
import com.leafye.ffmpegapp.view.VideoSurfaceView
import com.leafye.ffmpegapp.vm.VideoPlayViewModel
import kotlinx.android.synthetic.main.fragment_video_play.*
import kotlin.concurrent.thread

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
class VideoPlayFragment : SelectFileFragment<VideoPlayViewModel, FragmentVideoPlayBinding>() {
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
                }
            }
        }
    }

    override fun setupObservers() {
        viewModel.playEvent.observe(this, Observer {
            thread {
                YeFFmpeg.instance().play(viewModel.pathStr.value, surfaceView.surface)
            }
        })
    }

    override fun fragmentPrepared() {
        getFragActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        selectVideo.setOnClickListener {
            selectVideoPath(REQUEST_CODE_SELECT_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_FILE) {
            Log.d(TAG, "")
            data?.data?.let { d ->
                ContentUtils.getPath(getFragActivity(), d).also {
                    Log.d(TAG, "filePath:$it")
                }.apply {
                    //pathTv.text = this
                    viewModel.setSelectVideoPath(this)
                    surfaceFL.removeAllViews()
                    surfaceFL.addView(surfaceView)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        getFragActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }
}