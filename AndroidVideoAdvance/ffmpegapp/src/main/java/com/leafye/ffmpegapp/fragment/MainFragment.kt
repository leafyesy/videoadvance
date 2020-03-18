package com.leafye.ffmpegapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.leafye.base.BaseFragment
import com.leafye.base.observe
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.databinding.FragmentMainBinding
import com.leafye.ffmpegapp.vm.MainFragViewModel

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.fragment
 * @ClassName:      MainFragment
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/9 15:38
 * @UpdateUser:
 * @UpdateDate:     2020/3/9 15:38
 * @UpdateRemark:
 */
class MainFragment : BaseFragment<MainFragViewModel, FragmentMainBinding>() {
    override fun initVariableId(): Int {
        return BR.vm
    }

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_main
    }

    override fun setupObservers() {
        observe(viewModel.enterAudioHandleEvent) {
            AudioHandleFragment.startFragment(this@MainFragment)
        }
        observe(viewModel.enterVideoHandleEvent) {
            VideoHandleFragment.startFragment(this@MainFragment)
        }
        observe(viewModel.enterVideoPlayEvent) {
            VideoPlayFragment.startFragment(this@MainFragment)
        }
        observe(viewModel.enterMeidaPlayEvent) {
            CMediaFragment.startFragment(this@MainFragment)
        }
    }

    override fun fragmentPrepared() {
    }
}