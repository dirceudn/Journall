package com.google.android.journal.ui.view

import androidx.lifecycle.*
import com.google.android.journal.JournalApp
import com.google.android.journal.data.local.PostsRepository
import com.google.android.journal.data.model.Post
import com.google.android.journal.data.model.Resource
import com.google.android.journal.data.model.Status
import com.google.android.journal.helper.BaseMessage
import com.google.android.journal.utils.Constants
import com.google.android.journal.utils.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

class PostsViewModel : ViewModel() {

    @Inject
    lateinit var postsRepository: PostsRepository
    internal val showLoadingEvent = SingleLiveEvent<Boolean>()

    private val postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    internal val viewMessage: SingleLiveEvent<BaseMessage> = SingleLiveEvent()


    init {
        JournalApp.appComponent.inject(this)
    }


    fun getPosts(url: String, isRefreshing: Boolean): LiveData<List<Post>> {
        return Transformations.switchMap(postsRepository.loadPosts(url, isRefreshing)) {
            it?.run {
                data?.run {
                    postsLiveData.value = this
                }
                showLoadingEvent.value = status == Status.LOADING
                takeIf { status == Status.ERROR }?.run {
                    viewMessage.value = BaseMessage.Error("Some error occurred")


                }

                takeIf { status == Status.SUCCESS }?.run {
                    viewMessage.value = BaseMessage.Success("Posts loaded with successful")

                }


            }
            postsLiveData
        }
    }


}