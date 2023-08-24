package com.example.githubuserfinder

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserfinder.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        users.clear()
        users.addAll(items)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(ivAvatar)
                tvUsername.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }
}