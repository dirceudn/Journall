package com.google.android.journal.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.journal.ui.view.FavoriteViewModel
import com.google.android.journal.utils.Constants
import org.hamcrest.core.IsNull
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FavoriteViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private var favoriteViewModel = FavoriteViewModel()

    @Test
    fun testNull() {
        Assert.assertThat(favoriteViewModel.getFavorites(Constants.INSTANCE.FAVORITES, false), IsNull.notNullValue())
        Assert.assertThat(favoriteViewModel.getFavorites(Constants.INSTANCE.FAVORITES, true), IsNull.notNullValue())
    }
}