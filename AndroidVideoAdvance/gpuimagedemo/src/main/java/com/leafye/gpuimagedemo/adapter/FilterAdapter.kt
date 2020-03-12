package com.leafye.gpuimagedemo.adapter

import android.content.Context
import android.view.ViewGroup
import com.leafye.base.ext.bindingInflate
import com.leafye.base.recyclerview.BaseBindViewHolder
import com.leafye.base.recyclerview.BaseBindingAdapter
import com.leafye.base.recyclerview.ViewHolderWrap
import com.leafye.gpuimagedemo.BR
import com.leafye.gpuimagedemo.R
import com.leafye.gpuimagedemo.data.FilterItem
import com.leafye.gpuimagedemo.databinding.ItemFilterBinding
import com.leafye.gpuimagedemo.vm.FilterViewModel

/**
 * Created by leafye on 2020/3/12.
 */
class FilterAdapter(
    context: Context,
    private val viewModel: FilterViewModel,
    itemList: MutableList<FilterItem>? = null
) :
    BaseBindingAdapter<FilterItem>(context, itemList) {

    init {
        addHolder(FilterItemViewHolder())
    }


    inner class FilterItemViewHolder :
        ViewHolderWrap<FilterItem, ItemFilterBinding, BaseBindViewHolder<ItemFilterBinding>>() {

        override fun createBinding(parent: ViewGroup?): ItemFilterBinding =
            bindingInflate(context, R.layout.item_filter, parent)

        override fun onBind(
            itemBinding: ItemFilterBinding,
            itemList: MutableList<FilterItem>,
            position: Int
        ) {
            itemBinding.setVariable(BR.ft, itemList[position])
            itemBinding.vm = viewModel
        }
    }
}