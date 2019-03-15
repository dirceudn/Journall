package com.google.android.journal.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.journal.MainActivity
import com.google.android.journal.R
import com.google.android.journal.helper.AppSection
import com.google.android.journal.helper.BaseMessage
import com.google.android.journal.helper.factory.AppFragment
import com.google.android.journal.helper.interfaces.PostAdapterListener
import com.google.android.journal.ui.adapters.PostAdapter
import com.google.android.journal.ui.view.PostsViewModel
import com.google.android.journal.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : AppFragment(), PostAdapterListener {


    lateinit var postsViewModel: PostsViewModel
    lateinit var postAdapter: PostAdapter
    lateinit var rootView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.home_fragment, container, false)
        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        return rootView


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        /* SearchView to find a article by title */
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                postAdapter.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                postAdapter.filter?.filter(query)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onStart() {
        attachData()
        super.onStart()
    }

    /* Helpers*/

    private fun attachData() {
        postAdapter = PostAdapter(this)

        recycler_view.layoutManager = GridLayoutManager(activity, 2)
        recycler_view.adapter = postAdapter

        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        postsViewModel.showLoadingEvent.observe(this, Observer { value -> refresh.isRefreshing = value ?: false })
        postsViewModel.viewMessage.observe(
            this,
            Observer { value -> showMessage(value) })


        fetchPosts(false)

        refresh.setOnRefreshListener {
            fetchPosts(true)
        }

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
        val snackBar = Snackbar.make(rootView, message.errorString, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("Okay") { snackBar.dismiss() }
        snackBar.show()

    }

    private fun fetchPosts(isRefreshing: Boolean) {
        postsViewModel.getPosts(Constants.INSTANCE.ARTICLES, isRefreshing)
            .observe(viewLifecycleOwner, Observer { posts ->
                postAdapter.setPosts(posts)
            })
    }

    override fun onPostSelected(position: Int) {
        val args = Bundle()
        args.putParcelable(Constants.INSTANCE.ARG_ARTICLES, postAdapter.getPostsFiltered()?.get(position))
        (activity as MainActivity).navigateToSection(AppSection.POST_DETAIL, true, args)
    }


}