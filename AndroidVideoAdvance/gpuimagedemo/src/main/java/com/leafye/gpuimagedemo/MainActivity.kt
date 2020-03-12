package com.leafye.gpuimagedemo

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.leafye.base.BaseActivity
import com.leafye.gpuimagedemo.adapter.FilterAdapter
import com.leafye.gpuimagedemo.databinding.ActivityMainBinding
import com.leafye.gpuimagedemo.vm.FilterViewModel
import com.leafye.gpuimagedemo.vm.MainViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_main.*


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun initVariableId(): Int = BR._all

    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_main

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheetRv) }

    private val filterAdapter by lazy {
        FilterAdapter(this@MainActivity, filterViewModel)
    }

    private val filterViewModel: FilterViewModel by lazy {
        FilterViewModel()
    }

    override fun setupObservers() {
        viewModel.filterItemList.observe(this@MainActivity, Observer {
            filterAdapter.refresh(it)
        })
        filterViewModel.gpuImageFilter.observe(this@MainActivity, Observer { filter ->
            filter ?: return@Observer
            if (gpuImageView.filter?.javaClass != filter.javaClass) {
                gpuImageView.filter = filter
            }
        })
    }

    override fun activityPrepared() {
        initGPUImageView()
        initBottomSheet()
        floatActionBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun initGPUImageView() {
        gpuImageView.apply {
            setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
        }.setImage(BitmapFactory.decodeResource(resources, R.mipmap.time))
    }

    private fun initBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        //STATE_COLLAPSED 折叠
        //STATE_EXPANDED 全部展示
        //STATE_HIDDEN 折叠部分，折叠部分向上滑动，展示全部内容
        bottomSheetRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = filterAdapter
        }
        viewModel.refreshFilterItemList()
    }


}
