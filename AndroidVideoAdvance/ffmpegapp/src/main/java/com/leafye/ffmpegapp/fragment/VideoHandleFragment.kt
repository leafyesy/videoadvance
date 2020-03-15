package com.leafye.ffmpegapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.leafye.base.BaseFragment
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.baseNavOptions
import com.leafye.ffmpegapp.databinding.FragmentVideoHandleBinding
import com.leafye.ffmpegapp.ffmpeg.Result
import com.leafye.ffmpegapp.vm.VideoHandleViewModel
import kotlinx.android.synthetic.main.fragment_video_handle.*

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.fragment
 * @ClassName:      VideoHandleFragment
 * @Description:    视频处理的Fragment
 * @Author:         leafye
 * @CreateDate:     2020/3/11 13:31
 * @UpdateUser:
 * @UpdateDate:     2020/3/11 13:31
 * @UpdateRemark:
 */
class VideoHandleFragment : BaseFragment<VideoHandleViewModel, FragmentVideoHandleBinding>() {
    companion object {
        fun startFragment(frag: Fragment) {
            frag.findNavController().navigate(R.id.videoHandleFragment, null, baseNavOptions())
        }
    }

    override fun initVariableId() = BR.vm

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = R.layout.fragment_video_handle

    override fun setupObservers() {
        viewModel.resultLiveData.observe(this@VideoHandleFragment, Observer {
            when (it.type) {
                Result.Type.BEGIN,
                Result.Type.PROGRESS ->
                    progressBar.visibility = View.VISIBLE
                Result.Type.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        getFragActivity(),
                        "error code:${it.code} msg:${it.msg}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Result.Type.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(getFragActivity(), "处理成功", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun fragmentPrepared() {
    }
}