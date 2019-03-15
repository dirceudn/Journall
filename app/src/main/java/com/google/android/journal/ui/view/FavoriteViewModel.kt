package com.google.android.journal.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.journal.JournalApp
import com.google.android.journal.data.local.FavoriteRepository
import com.google.android.journal.data.model.Favorite
import com.google.android.journal.data.model.Status
import com.google.android.journal.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import javax.inject.Inject


class FavoriteViewModel : ViewModel() {


    @Inject
    lateinit var favoriteRepository: FavoriteRepository
    private val favoriteLiveData: MutableLiveData<List<Favorite>> = MutableLiveData()
    val responseLiveData = MutableLiveData<Favorite>()
    private val showLoadingErrorEvent = SingleLiveEvent<String>()
    internal val showFavoriteLoadingEvent = SingleLiveEvent<Boolean>()
    internal val deleteFavoriteEvent = SingleLiveEvent<Boolean>()
    private val disposables = CompositeDisposable()


    init {
        JournalApp.appComponent.inject(this)
    }


    fun addOnFavorite(id: String) {
        disposables.add(favoriteRepository.attach(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSubscribe {}
            .subscribe(
                { result ->
                    responseLiveData.postValue(result)
                    favoriteRepository.insertFavorite(result.article)

                },
                { throwable -> showLoadingErrorEvent.postValue(throwable.message) }
            ))

    }

    override fun onCleared() {
        disposables.clear()
    }


    fun getFavorites(url: String, isRefreshing: Boolean): LiveData<List<Favorite>> {
        return Transformations.switchMap(favoriteRepository.loadFavorites(url, isRefreshing)) {
            it?.run {
                data?.run {
                    favoriteLiveData.value = this
                }

                showFavoriteLoadingEvent.value = status == Status.LOADING
                takeIf { status == Status.ERROR }?.run {
                    showLoadingErrorEvent.value = message

                }

            }
            favoriteLiveData
        }
    }


    fun removeFavorites(_ids: ArrayList<String>) {
        favoriteRepository.deleteFavorites(_ids).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                deleteFavoriteEvent.postValue(response.isSuccessful)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // loginResponseInterface.onSuccess("","", loginDetail);
                deleteFavoriteEvent.postValue(false)
            }
        })


    }

}