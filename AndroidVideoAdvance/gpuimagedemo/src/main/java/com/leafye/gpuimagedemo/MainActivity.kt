package com.leafye.gpuimagedemo

import android.os.Bundle
import com.leafye.base.BaseActivity
import com.leafye.gpuimagedemo.databinding.ActivityMain2Binding
import com.leafye.gpuimagedemo.vm.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMain2Binding>() {

    override fun initVariableId(): Int = BR.vm

    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_main2

    //private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheetRv) }

    override fun setupObservers() {

    }

    override fun activityPrepared() {
        //gpuImageView.setImage(BitmapFactory.decodeResource(resources, R.mipmap.timg))
        initBottomSheet()
    }

    private fun initBottomSheet() {
        //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        //STATE_COLLAPSED 折叠
        //STATE_EXPANDED 全部展示
        //STATE_HIDDEN 折叠部分，折叠部分向上滑动，展示全部内容
    }


}
