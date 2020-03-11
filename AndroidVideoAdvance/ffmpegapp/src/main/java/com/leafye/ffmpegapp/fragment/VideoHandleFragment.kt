package com.leafye.ffmpegapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.leafye.base.BaseFragment
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.databinding.FragmentVideoHandleBinding
import com.leafye.ffmpegapp.vm.VideoHandleViewModel

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
            frag.findNavController().navigate(R.id.videoHandleFragment)
        }
    }

    override fun initVariableId() = BR.vm

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_video_handle
    }

    override fun setupObservers() {
    }

    override fun fragmentPrepared() {
    }
}