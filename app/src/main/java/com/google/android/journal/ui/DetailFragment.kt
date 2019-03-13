package com.google.android.journal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.journal.R
import com.google.android.journal.data.model.Post
import com.google.android.journal.helper.factory.AppFragment
import com.google.android.journal.ui.view.FavoriteViewModel
import timber.log.Timber


class DetailFragment : AppFragment() {

    private var binding: ViewDataBinding? = null
    lateinit var favoriteViewModel: FavoriteViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.postdetail_fragment, container, false
        )
        binding?.lifecycleOwner = this

        //create view model
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)

        //bind data object
        val post: Post = arguments?.getParcelable(HomeFragment.ARG_ARTICLE)!!
        bindPost(post, favoriteViewModel)

        return binding?.root


    }

    private fun bindPost(data: Any?, model: ViewModel) {
        binding?.setVariable(BR.data, data)
        binding?.setVariable(BR.model, model)
        binding?.executePendingBindings()
    }

}