package com.samraa.vizztablet.ui.home.adapter

import android.view.ViewGroup
import androidx.compose.runtime.collection.MutableVector
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samraa.data.models.dto.OrderDto

class TableAdapter : ListAdapter<OrderDto, RecyclerView.ViewHolder>(DiffCallback) {
    // Add these callback properties
    var onSwipeToDelete: ((OrderDto) -> Unit)? = null
    var onSwipeToDone: ((OrderDto) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TableItemVH.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TableItemVH) {
             holder.bind(getItem(position))
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<OrderDto>() {
        override fun areItemsTheSame(oldItem: OrderDto, newItem: OrderDto): Boolean =
            oldItem.id == newItem.id // Assuming OrderDto has an id field

        override fun areContentsTheSame(oldItem: OrderDto, newItem: OrderDto): Boolean =
            oldItem == newItem
    }
}