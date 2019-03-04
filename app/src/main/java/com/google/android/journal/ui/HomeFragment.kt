package com.google.android.journal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.journal.R
import com.google.android.journal.ui.view.PostsViewModel
import com.google.android.journal.utils.Constants
import timber.log.Timber

class HomeFragment : Fragment() {

    lateinit var postsViewModel: PostsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        attachData()

    }

    /* Helpers*/

    fun attachData() {
        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        postsViewModel.getPosts(Constants.INSTANCE.ARTICLES)
            .observe(this, Observer { posts ->
                Timber.d("Posts${posts.get(0).content}") })

    }

}