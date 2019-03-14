package com.google.android.journal.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.google.android.journal.R
import com.google.android.journal.data.model.Post
import com.google.android.journal.helper.interfaces.PostAdapterListener


class PostAdapter(listener: PostAdapterListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(), Filterable {


    private var posts: List<Post>? = null
    private var postsFiltered: List<Post>? = null
    private var postListener: PostAdapterListener? = listener

    private var selectedIds: List<String> = ArrayList()


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postsFiltered!![position], postListener)
        holder.display(selectedIds.contains(postsFiltered!![position].favorite_id))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.post_item, parent, false)

        return PostViewHolder(binding)
    }


    fun setPosts(listPosts: List<Post>) {
        posts = listPosts
        postsFiltered = listPosts
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return if (posts != null)
            postsFiltered!!.size
        else
            0

    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    postsFiltered = posts
                } else {
                    val filteredList = ArrayList<Post>()
                    for (row in posts!!) {

                        if (row.title!!.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }

                    postsFiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = postsFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                @Suppress("UNCHECKED_CAST")
                postsFiltered = filterResults.values as ArrayList<Post>
                notifyDataSetChanged()
            }
        }

    }

    fun getPostsFiltered(): List<Post>? {
        return postsFiltered
    }

    fun getItem(position: Int): Post? {
        return getPostsFiltered()?.get(position)
    }

    fun setSelectedIds(selectedIds: List<String>) {
        this.selectedIds = selectedIds
        notifyDataSetChanged()
    }

    class PostViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Any, postListener: PostAdapterListener?) {
            binding.setVariable(BR.data, data)
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                postListener?.onPostSelected(adapterPosition)

            }
        }

        fun display(isSelected: Boolean) {
            binding.root.setBackgroundColor(if (isSelected) Color.DKGRAY else Color.WHITE)
        }

    }


}