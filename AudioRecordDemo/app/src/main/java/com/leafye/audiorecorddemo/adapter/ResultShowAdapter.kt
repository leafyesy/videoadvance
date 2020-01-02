package com.leafye.audiorecorddemo.adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leafye.audiorecorddemo.linxi.data.Content

class ResultShowAdapter(private val dataList: MutableList<Content>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ResultViewHolder(TextView(parent.context).apply {
            setPadding(10, 10, 10, 10)
            setTextColor(Color.RED)
            textSize = 20F
        })

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResultViewHolder) {
            holder.showTv.text = dataList[position].getResultStr()
        }
    }

    fun addAudioParseResult(result: Content) {
        dataList.add(result)
        notifyDataSetChanged()
    }

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showTv: TextView by lazy {
            itemView as TextView
        }
    }

}

