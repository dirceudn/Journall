package com.google.android.journal.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.google.android.journal.R
import com.google.android.journal.data.model.Post

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    private var posts: List<Post>? = null

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts!![position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.post_item, parent, false)

        return PostViewHolder(binding)
    }


    fun setPosts(listPosts: List<Post>) {
        posts = listPosts
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int{
        return if (posts != null)
            posts!!.size
        else
            0

    }

    class PostViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Any) {
            binding.setVariable(BR.data, data)
            binding.executePendingBindings()
        }
    }

}