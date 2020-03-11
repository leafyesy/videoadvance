package com.leafye.gpuimagedemo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.leafye.base.BaseActivity
import com.leafye.base.recyclerview.BaseBindViewHolder
import com.leafye.base.recyclerview.BaseBindingAdapter
import com.leafye.base.recyclerview.ViewHolderWrap
import com.leafye.gpuimagedemo.data.FilterItem
import com.leafye.gpuimagedemo.databinding.ActivityMainBinding
import com.leafye.gpuimagedemo.databinding.ItemFilterBinding
import com.leafye.gpuimagedemo.vm.MainViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_main.*


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun initVariableId(): Int = BR._all

    override fun initContentView(savedInstanceState: Bundle?) = R.layout.activity_main

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheetRv) }

    private val filterAdapter by lazy {
        BaseBindingAdapter<FilterItem>(this@MainActivity)
            .apply {
                addHolder(createFilterHolderWrap())
            }
    }

    override fun setupObservers() {
        viewModel.filterItemList.observe(this@MainActivity, Observer {
            filterAdapter.refresh(it)
        })
    }

    override fun activityPrepared() {
        initGPUImageView()
        initBottomSheet()
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

    private fun createFilterHolderWrap() = object :
        ViewHolderWrap<FilterItem, ItemFilterBinding, BaseBindViewHolder<ItemFilterBinding>>() {

        override fun createBinding(parent: ViewGroup?): ItemFilterBinding {
            return DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(this@MainActivity),
                R.layout.item_filter,
                parent,
                false
            ) as ItemFilterBinding
        }

        override fun onBind(
            itemBinding: ItemFilterBinding,
            itemList: MutableList<FilterItem>,
            position: Int
        ) {
            itemBinding.setVariable(BR.ft, itemList[position])
        }
    }


}
