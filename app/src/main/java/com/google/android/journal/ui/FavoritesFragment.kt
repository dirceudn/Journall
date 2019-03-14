package com.google.android.journal.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.journal.MainActivity
import com.google.android.journal.R
import com.google.android.journal.helper.AppSection
import com.google.android.journal.helper.RecyclerItemClickListener
import com.google.android.journal.helper.factory.AppFragment
import com.google.android.journal.helper.interfaces.PostAdapterListener
import com.google.android.journal.ui.adapters.PostAdapter
import com.google.android.journal.ui.view.FavoriteViewModel
import com.google.android.journal.utils.Constants
import kotlinx.android.synthetic.main.favorites_fragment.*
import java.util.*


class FavoritesFragment : AppFragment(), PostAdapterListener, ActionMode.Callback {


    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteAdapter: PostAdapter
    lateinit var rootView: View
    private var actionMode: ActionMode? = null
    private var isMultiSelect = false
    private var selectedIds: MutableList<String> = ArrayList()


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

        favoriteViewModel.showFavoriteLoadingEvent.observe(
            this,
            Observer { value -> favorite_refresh.isRefreshing = value ?: false })


        favorite_refresh.setOnRefreshListener { fetchFavorites() }


        favorites_recycler_view.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity!!,
                favorites_recycler_view,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (isMultiSelect) {
                            //if multiple selection is enabled then select item on single click else perform normal click on item.
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

    private fun fetchFavorites() {
        favoriteViewModel.getFavorites(Constants.INSTANCE.FAVORITES)
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
                //just to show selected items.
                val stringBuilder = StringBuilder()
                for (data in favoriteAdapter.getPostsFiltered()!!) {
                    if (selectedIds.contains(data.favorite_id))
                        stringBuilder.append("\n").append(data.title)
                }
                Toast.makeText(activity, "Selected items are :$stringBuilder", Toast.LENGTH_SHORT).show()
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