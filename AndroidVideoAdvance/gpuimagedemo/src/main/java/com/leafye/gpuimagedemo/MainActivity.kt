package com.leafye.gpuimagedemo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
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

    private val filterViewModel: FilterViewModel by lazy {
        FilterViewModel()
    }

    private val filterAdapter by lazy {
        FilterAdapter(this@MainActivity, filterViewModel)
    }

    override fun setupObservers() {
        viewModel.filterItemList.observe(this@MainActivity, Observer {
            filterAdapter.refresh(it)
        })
        viewModel.bottomSheetOpen.observe(this@MainActivity, Observer {
            bottomSheetBehavior.state =
                if (it) BottomSheetBehavior.STATE_EXPANDED
                else BottomSheetBehavior.STATE_COLLAPSED
        })
        filterViewModel.gpuImageFilter.observe(this@MainActivity, Observer { filter ->
            filter
                ?.takeIf {
                    gpuImageView.filter?.javaClass != it.javaClass
                }?.let {
                    gpuImageView.filter = filter
                }
        })
    }

    override fun activityPrepared() {
        initGPUImageView()
        initBottomSheet()
    }

    private fun initGPUImageView() {
        gpuImageView.apply {
            setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
            setImage(BitmapFactory.decodeResource(resources, R.mipmap.time))
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                floatActionBtn.rotation =
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) 90F else -90F
            }
        })
        bottomSheetRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = filterAdapter
        }
        floatActionBtn.apply {
            rotation = -90F
            setOnClickListener {
                viewModel.floatActionClick()
            }
        }
        viewModel.refreshFilterItemList()
    }


}
