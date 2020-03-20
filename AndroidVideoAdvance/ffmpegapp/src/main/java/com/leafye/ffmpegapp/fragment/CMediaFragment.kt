package com.leafye.ffmpegapp.fragment

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
import com.leafye.base.observe
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.YeFFmpeg
import com.leafye.ffmpegapp.baseNavOptions
import com.leafye.ffmpegapp.databinding.FragmentCMediaBinding
import com.leafye.ffmpegapp.fragment.base.SelectFileFragment
import com.leafye.ffmpegapp.utils.ContentUtils
import com.leafye.ffmpegapp.view.VideoSurfaceView
import com.leafye.ffmpegapp.vm.CMediaViewModel
import kotlinx.android.synthetic.main.fragment_c_media.*
import kotlin.concurrent.thread

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.fragment
 * @ClassName:      CMediaFragment
 * @Description:
 * @Author:         leafye
 * @CreateDate:     2020/3/18 16:39
 * @UpdateUser:
 * @UpdateDate:     2020/3/18 16:39
 * @UpdateRemark:
 */
class CMediaFragment : SelectFileFragment<CMediaViewModel, FragmentCMediaBinding>() {

    companion object {
        private const val REQUEST_CODE_SELECT_FILE = 1001
        private const val TAG = "CMediaFragment"

        fun startFragment(frag: Fragment) {
            frag.findNavController().navigate(R.id.cMediaFragment, null, baseNavOptions())
        }
    }

    override fun initVariableId(): Int {
        return BR.vm
    }

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_c_media
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
        observe(viewModel.selectCall) {
            selectVideoPath(REQUEST_CODE_SELECT_FILE)
        }
        observe(viewModel.setUpCall) {
            thread {

            }
            val setupMedia =
                YeFFmpeg.instance().setupMedia(viewModel.pathLiveData.value, surfaceView.surface)
            if (setupMedia == 0) {
                viewModel.status.value = "设置文件路径成功"
            } else {
                viewModel.status.value = "设置文件路径失败,code:$setupMedia"
            }
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

    override fun fragmentPrepared() {
        getFragActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onDestroy() {
        super.onDestroy()
        getFragActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}