package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.storyapp.DiffCallback
import com.example.storyapp.databinding.ItemRowBinding
import com.example.storyapp.model.StoryModel

class StoryAdapter : PagingDataAdapter<StoryModel, StoryAdapter.ViewHolder>(DiffCallback) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var root = binding.root
        var image = binding.ivItemPhoto
        var name = binding.tvItemName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.apply {
            name.text = story?.name
            Glide.with(itemView.context)
                .load(story?.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)

            root.setOnClickListener {
                onItemClickCallback?.onItemClicked(story as StoryModel)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryModel)
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}