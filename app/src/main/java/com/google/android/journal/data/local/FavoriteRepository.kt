package com.google.android.journal.data.local

import androidx.lifecycle.LiveData
import com.google.android.journal.AppExecutors
import com.google.android.journal.data.db.JournalDao
import com.google.android.journal.data.model.*
import com.google.android.journal.data.remote.ApiService
import com.google.android.journal.data.remote.NetworkBoundResource
import com.google.android.journal.utils.RateLimiter
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class FavoriteRepository @Inject
constructor(
    private val appExecutors: AppExecutors,
    private val journalDao: JournalDao,
    private val apiService: ApiService
) {

    private val postsListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)


    fun attach(articleId: String): Observable<Favorite> {
        return apiService.createFavorite(FavoriteBody(articleId))
    }

    fun deleteFavorites(ids: ArrayList<String>): Call<ResponseBody> {
        return apiService.deleteFavorites(FavoritesBody(ids))
    }


    fun insertFavorite(post: Post) {
        journalDao.insert(post)
    }

    fun loadFavorites(url: String, isRefreshing: Boolean): LiveData<Resource<List<Favorite>>> {
        return object : NetworkBoundResource<List<Favorite>, List<Favorite>>(appExecutors) {

            override fun deleteDataFromDb(body: List<Favorite>?) {

            }

            override fun saveCallResult(item: List<Favorite>) {
                journalDao.insertAllFavorites(item)
                journalDao.deleteOldData(item.map { it.id })
            }

            override fun shouldFetch(data: List<Favorite>?): Boolean {
                return data == null || data.isEmpty() || postsListRateLimit.shouldFetch(url) || isRefreshing
            }

            override fun loadFromDb() = journalDao.getFavorites()

            override fun createCall() = apiService.getFavorites(url)

            override fun onFetchFailed() {
                postsListRateLimit.reset(url)
            }
        }.asLiveData()
    }


}