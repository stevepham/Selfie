package com.ht117.selfie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ht117.selfie.data.source.local.MyImage
import com.ht117.selfie.databinding.ItemPhotoBinding
import kotlin.math.min

class PhotoAdapter(private val items: MutableList<MyImage> = mutableListOf())
    : RecyclerView.Adapter<PhotoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount() = min(items.size, 3)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(items[position])
    }

    fun dispatchData(newItems: List<MyImage>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class Holder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: MyImage) {
            binding.run {
                iv.load(item.path)
            }
        }
    }
}
