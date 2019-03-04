package com.google.android.journal.data.local

import androidx.lifecycle.LiveData
import com.google.android.journal.AppExecutors
import com.google.android.journal.data.db.JournalDao
import com.google.android.journal.data.model.Post
import com.google.android.journal.data.model.Resource
import com.google.android.journal.data.remote.ApiService
import com.google.android.journal.data.remote.NetworkBoundResource
import com.google.android.journal.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject
constructor (
    private val appExecutors: AppExecutors,
    private val journalDao: JournalDao,
    private val apiService: ApiService
) {


    private val postsListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadPosts(url: String): LiveData<Resource<List<Post>>> {
        return object : NetworkBoundResource<List<Post>, List<Post>>(appExecutors) {

            override fun deleteDataFromDb(body: List<Post>?) {
                // for while its not implemented
            }

            override fun saveCallResult(item: List<Post>) {
                journalDao.insertAll(item)
            }

            override fun shouldFetch(data: List<Post>?): Boolean {
                return data == null || data.isEmpty() || postsListRateLimit.shouldFetch(url)
            }

            override fun loadFromDb() = journalDao.getPosts()

            override fun createCall() = apiService.getArticles(url)

            override fun onFetchFailed() {
                postsListRateLimit.reset(url)
            }
        }.asLiveData()
    }

}