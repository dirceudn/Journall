package com.google.android.journal.data.remote

import androidx.lifecycle.LiveData
import com.google.android.journal.data.model.Favorite
import com.google.android.journal.data.model.FavoriteBody
import com.google.android.journal.data.model.Post
import com.google.android.journal.helper.api.ApiResponse
import com.google.android.journal.utils.Constants
import io.reactivex.Observable
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET
    fun getArticles(@Url url: String): LiveData<ApiResponse<List<Post>>>

    @GET
    fun getFavorites(@Url url: String): LiveData<ApiResponse<List<Favorite>>>

    @POST("/favorites")
    fun createFavorite(@Body body: FavoriteBody):  Observable<Favorite>


    companion object Factory {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.INSTANCE.BASE_URL)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}