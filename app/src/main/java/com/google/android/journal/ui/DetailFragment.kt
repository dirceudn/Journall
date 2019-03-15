package com.google.android.journal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.journal.R
import com.google.android.journal.data.model.Post
import com.google.android.journal.helper.BaseMessage
import com.google.android.journal.helper.factory.AppFragment
import com.google.android.journal.ui.view.FavoriteViewModel
import com.google.android.journal.utils.Constants
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class DetailFragment : AppFragment() {

    private var binding: ViewDataBinding? = null
    private lateinit var favoriteViewModel: FavoriteViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.postdetail_fragment, container, false
        )
        binding?.lifecycleOwner = this

        //create view model
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        favoriteViewModel.responseLiveData.observe(viewLifecycleOwner, Observer { favorite ->
            bindPost(favorite.article, favoriteViewModel)
        })

        favoriteViewModel.viewMessageEvent.observe(
            this,
            Observer { value -> showMessage(value) })
        //bind data object
        val post: Post = arguments?.getParcelable(Constants.INSTANCE.ARG_ARTICLES)!!
        bindPost(post, favoriteViewModel)



        return binding?.root


    }

    private fun showMessage(message: BaseMessage?) {
        when (message) {
            is BaseMessage.Error -> {
                showSnackBar(message)
            }
            is BaseMessage.Success -> {
                showToastMessage(message)
            }
        }
    }

    private fun showToastMessage(message: BaseMessage.Success) {
        Toast.makeText(activity, message.toastMessage, Toast.LENGTH_LONG).show()

    }

    private fun showSnackBar(message: BaseMessage.Error) {
        val snackBar = Snackbar.make(binding?.root!!, message.errorString, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("Okay") { snackBar.dismiss() }
        snackBar.show()

    }

    private fun bindPost(data: Any?, model: ViewModel) {
        binding?.setVariable(BR.data, data)
        binding?.setVariable(BR.model, model)
        binding?.executePendingBindings()
    }

}