package com.leafye.ffmpegapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.leafye.base.BaseFragment
import com.leafye.ffmpegapp.BR
import com.leafye.ffmpegapp.R
import com.leafye.ffmpegapp.databinding.FragmentAudioHandleBinding
import com.leafye.ffmpegapp.vm.AudioHandleViewModel

/**
 *
 * @ProjectName:    AndroidVideoAdvance
 * @Package:        com.leafye.ffmpegapp.fragment
 * @ClassName:      AudioHandleFragment
 * @Description:     java类作用描述
 * @Author:         leafye
 * @CreateDate:     2020/3/9 15:36
 * @UpdateUser:
 * @UpdateDate:     2020/3/9 15:36
 * @UpdateRemark:
 */
class AudioHandleFragment : BaseFragment<AudioHandleViewModel, FragmentAudioHandleBinding>() {

    companion object {

        fun startFragment(frag: Fragment) {

            frag.findNavController().navigate(R.id.audioHandleFragment, null, navOptions {
                anim {
                    enter = R.anim.anim_slide_in_right
                    exit = R.anim.anim_slide_out_left
                    popEnter = R.anim.anim_slide_in_left
                    popExit = R.anim.anim_slide_out_right
                }
            })
        }
    }

    override fun initVariableId(): Int = BR.vm

    override fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_audio_handle
    }

    override fun setupObservers() {

    }

    override fun fragmentPrepared() {

    }


}