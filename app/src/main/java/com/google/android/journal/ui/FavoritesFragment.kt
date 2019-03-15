package com.google.android.journal.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.journal.MainActivity
import com.google.android.journal.R
import com.google.android.journal.data.model.Post
import com.google.android.journal.helper.AppSection
import com.google.android.journal.helper.RecyclerItemClickListener
import com.google.android.journal.helper.factory.AppFragment
import com.google.android.journal.helper.interfaces.PostAdapterListener
import com.google.android.journal.ui.adapters.PostAdapter
import com.google.android.journal.ui.view.FavoriteViewModel
import com.google.android.journal.utils.Constants
import kotlinx.android.synthetic.main.favorites_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*


class FavoritesFragment : AppFragment(), PostAdapterListener, ActionMode.Callback {


    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteAdapter: PostAdapter
    lateinit var rootView: View
    private var actionMode: ActionMode? = null
    private var isMultiSelect = false
    private var selectedIds: ArrayList<String> = ArrayList()

    private var bundle: Bundle = Bundle()
    private var itemState: String = "item"
    private var dataState: String = "data"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.favorites_fragment, container, false)

        return rootView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteAdapter = PostAdapter(this)
        favorites_recycler_view.layoutManager = GridLayoutManager(activity, 2)
        favorites_recycler_view.adapter = favoriteAdapter

        if (savedInstanceState == null) {
            attachData()
        } else {
            restoreData()
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (favorites_recycler_view != null) {
            val parcelable: Parcelable = favorites_recycler_view.layoutManager?.onSaveInstanceState()!!
            bundle.putParcelable(itemState, parcelable)
            bundle.putParcelableArrayList(dataState, ArrayList(favoriteAdapter.getPostsFiltered()!!))
        }

        super.onSaveInstanceState(outState)


    }

    private fun restoreData() {
        val listState: Parcelable = bundle.getParcelable(itemState)!!
        // getting recyclerview items
        val list: List<Post> = bundle.getParcelableArrayList(dataState)!!
        // Restoring adapter items
        favoriteAdapter.setPosts(list)
        // Restoring recycler view position
        favorites_recycler_view.layoutManager?.onRestoreInstanceState(listState)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


    private fun attachData() {



        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        fetchFavorites(false)

        favoriteViewModel.showFavoriteLoadingEvent.observe(
            this,
            Observer { value -> favorite_refresh.isRefreshing = value ?: false })

        favoriteViewModel.deleteFavoriteEvent.observe(this, Observer { isDeleted ->
            if (isDeleted) {
                actionMode?.finish()
                fetchFavorites(true)

            }
        })


        favorite_refresh.setOnRefreshListener { fetchFavorites(true) }


        favorites_recycler_view.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity!!,
                favorites_recycler_view,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (isMultiSelect) {
                            multiSelect(position)
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                        if (!isMultiSelect) {
                            selectedIds = ArrayList()
                            isMultiSelect = true

                            if (actionMode == null) {
                                actionMode = activity?.startActionMode(this@FavoritesFragment)
                            }
                        }

                        multiSelect(position)
                    }
                })
        )


    }

    private fun fetchFavorites(isRefreshing: Boolean) {
        favoriteViewModel.getFavorites(Constants.INSTANCE.FAVORITES, isRefreshing)
            .observe(viewLifecycleOwner, Observer { favorites ->
                favoriteAdapter.setPosts(favorites.map { it.article })
            })

    }

    private fun multiSelect(position: Int) {
        val data = favoriteAdapter.getItem(position)
        if (data != null) {
            if (actionMode != null) {
                if (selectedIds.contains(data.favorite_id))
                    selectedIds.remove(data.favorite_id)
                else
                    data.favorite_id?.let { selectedIds.add(it) }

                if (selectedIds.size > 0)
                    actionMode?.title = selectedIds.size.toString() //show selected item count on action mode.
                else {
                    actionMode?.title = "" //remove item count from action mode.
                    actionMode?.finish() //hide action mode.
                }
                favoriteAdapter.setSelectedIds(selectedIds)

            }
        }
    }

    override fun onPostSelected(position: Int) {
        val args = Bundle()
        args.putParcelable(Constants.INSTANCE.ARG_ARTICLES, favoriteAdapter.getPostsFiltered()?.get(position))
        (activity as MainActivity).navigateToSection(AppSection.POST_DETAIL, true, args)
    }


    override fun onActionItemClicked(p0: ActionMode?, menuItem: MenuItem?): Boolean {
        when (menuItem?.itemId) {
            R.id.action_delete -> {
                favoriteViewModel.removeFavorites(selectedIds)
                return true
            }
        }
        return false
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        isMultiSelect = false
        selectedIds = ArrayList()
        favoriteAdapter.setSelectedIds(ArrayList())
        actionMode = null

    }

    override fun onDestroy() {
        super.onDestroy()
        actionMode?.finish()
    }

}