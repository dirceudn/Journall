package com.google.android.journal.data.local

import androidx.lifecycle.LiveData
import com.google.android.journal.AppExecutors
import com.google.android.journal.data.db.JournalDao
import com.google.android.journal.data.model.Favorite
import com.google.android.journal.data.model.FavoriteBody
import com.google.android.journal.data.model.Post
import com.google.android.journal.data.model.Resource
import com.google.android.journal.data.remote.ApiService
import com.google.android.journal.data.remote.NetworkBoundResource
import com.google.android.journal.helper.api.ApiResponse
import com.google.android.journal.utils.RateLimiter
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class FavoriteRepository @Inject
constructor(
    private val appExecutors: AppExecutors,
    private val journalDao: JournalDao,
    private val apiService: ApiService
) {

    private val postsListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)


    fun attach(articleId: String): Observable<ApiResponse<Post>> {
        return apiService.createFavorite(FavoriteBody(articleId))
    }


    fun insertFavorite(post: Post) {
        journalDao.insert(post)
    }

    fun loadFavorites(url: String): LiveData<Resource<List<Favorite>>> {
        return object : NetworkBoundResource<List<Favorite>, List<Favorite>>(appExecutors) {

            override fun deleteDataFromDb(body: List<Favorite>?) {
                // for while its not implemented
            }

            override fun saveCallResult(item: List<Favorite>) {
                journalDao.insertAllFavorites(item)
            }

            override fun shouldFetch(data: List<Favorite>?): Boolean {
                return data == null || data.isEmpty() || postsListRateLimit.shouldFetch(url)
            }

            override fun loadFromDb() = journalDao.getFavorites()

            override fun createCall() = apiService.getFavorites(url)

            override fun onFetchFailed() {
                postsListRateLimit.reset(url)
            }
        }.asLiveData()
    }}