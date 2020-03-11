package com.leafye.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by leafye on 2020/3/11.
 */
class BaseBindViewHolder<B : ViewDataBinding>(val itemBinding: B) :
    RecyclerView.ViewHolder(itemBinding.root)