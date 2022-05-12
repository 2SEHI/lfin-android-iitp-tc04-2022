package com.lfin.android.iitp.lfin_android_iitp_tc04_2022

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.databinding.RecyclerviewItemBinding
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.db.data.QueryPlanEntity

class LogAdaptor : ListAdapter<QueryPlanEntity, LogAdaptor.ItemViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder = ItemViewHolder(
        RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            title.text = item.b_file_name
            description.text = item.metadata
        }
    }

    class ItemViewHolder(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root)
}

val COMPARATOR = object : DiffUtil.ItemCallback<QueryPlanEntity>() {
    override fun areItemsTheSame(oldItem: QueryPlanEntity, newItem: QueryPlanEntity): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: QueryPlanEntity, newItem: QueryPlanEntity): Boolean =
        oldItem == newItem

}