package com.leafye.base.recyclerview

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by leafye on 2020/3/11.
 */
open class BaseBindingAdapter<T>(
    protected val context: Context,
    private var itemList: MutableList<T>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_NORMAL = 0
    }

    private val holderWrapList = mutableListOf<ViewHolderWrap<T, *, *>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val filter = holderWrapList.filter { it.isThis(viewType) }
        if (filter.isNullOrEmpty()) {
            throw BaseRecyclerViewException("not init ViewHolder List")
        }
        return filter[0].onCreateVH(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        itemList?.let { list ->
            holderWrapList.filter {
                it.isThis(getItemViewType(position))
            }.forEach() {
                it.bind(holder, list, position)
            }
        }
    }

    fun refresh(itemList: MutableList<T>) {
        this.itemList?.clear()
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun addHolder(vhWrap: ViewHolderWrap<T, *, *>) {
        holderWrapList.add(vhWrap)
    }
}