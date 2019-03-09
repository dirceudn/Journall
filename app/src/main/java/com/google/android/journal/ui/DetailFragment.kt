package com.google.android.journal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.journal.helper.factory.AppFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.google.android.journal.R
import com.google.android.journal.data.model.Post


class DetailFragment : AppFragment() {

    private var binding: ViewDataBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // rootView = inflater.inflate(R.layout.postdetail_fragment, container, false)

       binding =  DataBindingUtil.inflate(
            inflater,
            R.layout.postdetail_fragment, container, false
        )

        val post: Post = arguments?.getParcelable(HomeFragment.ARG_ARTICLE)!!

        bindPost(post)

        return binding?.root


    }

    fun bindPost(data: Any?){
        binding?.setVariable(BR.data, data)
        binding?.executePendingBindings()
    }

}