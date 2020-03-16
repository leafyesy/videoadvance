package com.leafye.ffmpegapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.leafye.base.BaseFragment
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
        viewModel.enterAudioHandleEvent.observe(this, Observer<Int> {
            AudioHandleFragment.startFragment(this@MainFragment)
        })
        viewModel.enterVideoHandleEvent.observe(this, Observer<Int> {
            VideoHandleFragment.startFragment(this@MainFragment)
        })
        viewModel.enterVideoPlayEvent.observe(this, Observer {
            VideoPlayFragment.startFragment(this@MainFragment)
        })
    }

    override fun fragmentPrepared() {
    }
}