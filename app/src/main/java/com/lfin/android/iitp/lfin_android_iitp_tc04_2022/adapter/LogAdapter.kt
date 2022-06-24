package com.lfin.android.iitp.lfin_android_iitp_tc04_2022.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lfin.android.iitp.lfin_android_iitp_tc04_2022.R

class LogAdapter : ListAdapter<String, LogAdapter.ItemViewHolder>(COMPARATOR) {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logTextView: TextView = itemView.findViewById(R.id.logString)

        fun bind(logString: String){
            logTextView.text = logString
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_item, viewGroup, false)

        return ItemViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        val logString = getItem(position)
        viewHolder.bind(logString)
    }
}
val COMPARATOR = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

}
