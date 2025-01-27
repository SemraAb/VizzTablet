package com.samraa.vizztablet.ui.home.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.samraa.data.enums.OrderStatus
import com.samraa.data.models.dto.OrderDto
import com.samraa.vizztablet.R
import com.samraa.vizztablet.databinding.ItemTableRowBinding

class TableItemVH(private val binding: ItemTableRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        dto: OrderDto
    ) {
        val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_circle)?.mutate()

        binding.dto = dto

//        if(dto.isStatusLocked){
//            binding.itemHolder.isEnabled = false
//            binding.itemHolder.isClickable = false
//            binding.itemHolder.alpha = 0.5F
//        }else{
//            binding.itemHolder.isEnabled = true
//            binding.itemHolder.isClickable = true
//            binding.itemHolder.alpha =  1F
//        }

        when (dto.orderStatus) {
            OrderStatus.IN_PROGRESS -> {
                binding.orderStatus.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_peding)
                binding.orderStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.orange
                    )
                )
                binding.orderStatus.text ="Pending"
                drawable?.apply {
                    setTint(ContextCompat.getColor(itemView.context, R.color.orange))
                    setTintMode(PorterDuff.Mode.SRC_IN)
                }

            }

            OrderStatus.DONE -> {
                binding.orderStatus.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_done)
                binding.orderStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.done_green_text
                    )
                )
                binding.orderStatus.text ="Done"

                drawable?.apply {
                    setTint(ContextCompat.getColor(itemView.context, R.color.done_green_text))
                    setTintMode(PorterDuff.Mode.SRC_IN)
                }
            }

            OrderStatus.FAILED -> {
                binding.orderStatus.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_fail)
                binding.orderStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.red
                    )
                )
                binding.orderStatus.text ="Fail"

                drawable?.apply {
                    setTint(ContextCompat.getColor(itemView.context, R.color.red))
                    setTintMode(PorterDuff.Mode.SRC_IN)
                }
            }

        }
        binding.orderStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)


    }


    companion object {
        fun from(parent: ViewGroup): TableItemVH {
            val binding =
                ItemTableRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

            return TableItemVH(binding)
        }
    }
}

