package com.google.android.journal.ui.view

import androidx.databinding.library.baseAdapters.BR.data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.journal.JournalApp
import com.google.android.journal.data.local.FavoriteRepository
import com.google.android.journal.data.model.Favorite
import com.google.android.journal.data.model.Post
import com.google.android.journal.data.model.Resource
import com.google.android.journal.helper.api.ApiResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class FavoriteViewModel : ViewModel() {


    @Inject
    lateinit var favoriteRepository: FavoriteRepository
    private val favoriteLiveData: MutableLiveData<List<Favorite>> = MutableLiveData()
    private val responseLiveData = MutableLiveData<Resource<ApiResponse<Post>>>()
    private val disposables = CompositeDisposable()


    init {
        JournalApp.appComponent.inject(this)
    }


    fun addOnFavorite(id: String) {
        disposables.add(favoriteRepository.attach(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSubscribe {
                responseLiveData.postValue(Resource.loading())
                Timber.d("Timber")


            }
            .subscribe(
                { result ->
                    responseLiveData.postValue(Resource.success(result))
                    Timber.d("FAVORITE$result")
                },
                { throwable -> responseLiveData.postValue(Resource.error(throwable)) }
            ))

    }

    override fun onCleared() {
        disposables.clear()
    }


    fun getFavorites(url: String): LiveData<List<Favorite>> {
        return Transformations.switchMap(favoriteRepository.loadFavorites(url)) {
            it?.run {
                data?.run {
                    favoriteLiveData.value = this
                }

            }
            favoriteLiveData
        }
    }


}