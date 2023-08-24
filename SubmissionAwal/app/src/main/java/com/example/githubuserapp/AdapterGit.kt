package com.example.githubuserapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserapp.databinding.ItemRowBinding

class AdapterGit : RecyclerView.Adapter<AdapterGit.UserViewHolder>() {

    private val list = ArrayList<Users>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setUser(users: ArrayList<Users>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: Users) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(users)
            }
            binding.apply {
                Glide.with(itemView.context)
                    .load(users.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(userImage)
                userName.text = users.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
    }
}
