package com.google.android.journal.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.journal.JournalApp
import com.google.android.journal.data.local.PostsRepository
import com.google.android.journal.data.model.Post
import javax.inject.Inject

class PostsViewModel : ViewModel() {

    @Inject
    lateinit var postsRepository: PostsRepository

    private val postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()


    init {
        JournalApp.appComponent.inject(this)
    }


    fun getPosts(url:String): LiveData<List<Post>> {
        return Transformations.switchMap(postsRepository.loadPosts(url)) {
            it?.run {
                data?.run {
                    postsLiveData.value = this
                }

            }
            postsLiveData
        }
    }


}