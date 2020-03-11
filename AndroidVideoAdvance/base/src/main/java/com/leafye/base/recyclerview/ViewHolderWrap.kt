package com.leafye.base.recyclerview

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by leafye on 2020/3/12.
 */
abstract class ViewHolderWrap<T, B : ViewDataBinding, VH : BaseBindViewHolder<B>> {

    var isExecutePendingBindings: Boolean = true

    fun onCreateVH(parent: ViewGroup?) =
        BaseBindViewHolder(createBinding(parent))

    fun isThis(viewType: Int) = viewType == getMatchViewType()

    open fun getMatchViewType() = BaseBindingAdapter.VIEW_TYPE_NORMAL

    abstract fun createBinding(parent: ViewGroup?): B

    fun bind(vh: RecyclerView.ViewHolder, itemList: MutableList<T>, position: Int) {
        val itemBinding = (vh as BaseBindViewHolder<B>).itemBinding
        onBind(itemBinding, itemList, position)
        if (isExecutePendingBindings) {
            itemBinding.executePendingBindings()
        }
    }

    abstract fun onBind(itemBinding: B, itemList: MutableList<T>, position: Int)

}