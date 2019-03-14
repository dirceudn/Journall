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
import timber.log.Timber
import javax.inject.Inject


class FavoriteViewModel : ViewModel() {


    @Inject
    lateinit var favoriteRepository: FavoriteRepository
    private val favoriteLiveData: MutableLiveData<List<Favorite>> = MutableLiveData()
     val responseLiveData = MutableLiveData<Favorite>()
    private val showLoadingErrorEvent = SingleLiveEvent<String>()
    private val disposables = CompositeDisposable()


    init {
        JournalApp.appComponent.inject(this)
    }


    fun addOnFavorite(id: String) {
        disposables.add(favoriteRepository.attach(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .doOnSubscribe {
                Timber.d("FAVORITE loading")

            }
            .subscribe(
                { result ->
                    responseLiveData.postValue(result)
                    favoriteRepository.insertFavorite(result.article)
                },
                { throwable -> showLoadingErrorEvent.value = throwable.message }
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