package com.google.android.journal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.journal.MainActivity
import com.google.android.journal.R
import com.google.android.journal.helper.AppSection
import com.google.android.journal.helper.factory.AppFragment
import com.google.android.journal.helper.interfaces.PostAdapterListener
import com.google.android.journal.ui.adapters.PostAdapter
import com.google.android.journal.ui.view.FavoriteViewModel
import com.google.android.journal.utils.Constants
import kotlinx.android.synthetic.main.favorites_fragment.*


class FavoritesFragment : AppFragment(), PostAdapterListener {


    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteAdapter: PostAdapter
    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.favorites_fragment, container, false)

        return rootView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteAdapter = PostAdapter(this)

        attachData()




    }

    private fun attachData() {

        favorites_recycler_view.layoutManager = GridLayoutManager(activity, 2)
        favorites_recycler_view.adapter = favoriteAdapter

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        fetchFavorites()

        favoriteViewModel.showFavoriteLoadingEvent.observe(this, Observer { value -> favorite_refresh.isRefreshing = value ?: false })


        favorite_refresh.setOnRefreshListener { fetchFavorites() }


    }

    fun fetchFavorites() {
        favoriteViewModel.getFavorites(Constants.INSTANCE.FAVORITES)
            .observe(viewLifecycleOwner, Observer { favorites ->
                favoriteAdapter.setPosts(favorites.map { it.article })
            })

    }

    override fun onPostSelected(position: Int) {
        val args = Bundle()
        args.putParcelable(Constants.INSTANCE.ARG_ARTICLES, favoriteAdapter.getPostsFiltered()?.get(position))
        (activity as MainActivity).navigateToSection(AppSection.POST_DETAIL, true, args)
    }

}