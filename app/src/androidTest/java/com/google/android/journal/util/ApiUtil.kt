package com.google.android.journal.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.github.api.ApiResponse
import retrofit2.Response

object ApiUtils {
    fun <T : Any> successCall(data: T) = createCall(Response.success(data))

    fun <T : Any> createCall(response: Response<T>) = MutableLiveData<ApiResponse<T>>().apply {
        value = ApiResponse.create(response)
    } as LiveData<ApiResponse<T>>
}
