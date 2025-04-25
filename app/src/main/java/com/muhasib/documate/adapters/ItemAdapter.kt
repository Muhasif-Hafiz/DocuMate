package com.muhasib.documate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhasib.documate.R
import com.muhasib.documate.data.ItemEntity
import com.muhasib.documate.databinding.ItemListBinding

class ItemAdapter(
    private val onItemClick: (ItemEntity) -> Unit,
    private val onDeleteClick: (ItemEntity) -> Unit,

) : ListAdapter<ItemEntity, ItemAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ItemViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(adapterPosition))
                }
            }

            binding.deleteButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(adapterPosition))
                }
            }

        }

        fun bind(item: ItemEntity) {
            binding.apply {
                itemName.text = item.name
                itemId.text = "ID: ${item.id}"

                item.price?.let {
                    itemPrice.text = "Price: $${"%.2f".format(it)}"
                    itemPrice.visibility = View.VISIBLE
                } ?: run {
                    itemPrice.visibility = View.GONE
                }

                item.capacity?.let {
                    itemCapacity.text = "Capacity: $it"
                    itemCapacity.visibility = View.VISIBLE
                } ?: run {
                    itemCapacity.visibility = View.GONE
                }

                item.color?.let {
                    itemColor.text = "Color: $it"
                    itemColor.visibility = View.VISIBLE
                } ?: run {
                    itemColor.visibility = View.GONE
                }

                if (item.isSynced) {
                    syncStatus.setImageResource(R.drawable.sis_synced_svgrepo_com)
                } else {
                    syncStatus.setImageResource(R.drawable.ic_synced)
                }
                root.setOnClickListener { onItemClick(item) }
                deleteButton.setOnClickListener { onDeleteClick(item) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ItemEntity>() {
        override fun areItemsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
            return oldItem == newItem
        }
    }
}