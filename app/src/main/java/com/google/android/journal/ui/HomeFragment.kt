package com.google.android.journal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.journal.R
import com.google.android.journal.ui.adapters.PostAdapter
import com.google.android.journal.ui.view.PostsViewModel
import com.google.android.journal.utils.Constants
import kotlinx.android.synthetic.main.home_fragment.*
import timber.log.Timber


class HomeFragment : Fragment() {

    lateinit var postsViewModel: PostsViewModel
    lateinit var postAdapter: PostAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        postAdapter = PostAdapter()

        attachData()

    }

    /* Helpers*/

    fun attachData() {

        //init recyclerview

        recycler_view.layoutManager = GridLayoutManager(activity, 2)

        recycler_view.adapter = postAdapter


        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        postsViewModel.getPosts(Constants.INSTANCE.ARTICLES)
            .observe(this, Observer { posts ->
                postAdapter.setPosts(posts)
                Timber.d("Posts${posts}")
            })

    }

}