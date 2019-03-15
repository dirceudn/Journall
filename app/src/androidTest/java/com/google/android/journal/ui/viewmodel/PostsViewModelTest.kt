package com.google.android.journal.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.journal.data.local.PostsRepository
import com.google.android.journal.ui.view.PostsViewModel
import com.google.android.journal.utils.Constants
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class PostsViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(PostsRepository::class.java)
    private var postViewModel = PostsViewModel()

    @Test
    fun testNull() {
        assertThat(postViewModel.getPosts(Constants.INSTANCE.ARTICLES, false), notNullValue())
        assertThat(postViewModel.getPosts(Constants.INSTANCE.ARTICLES, true), notNullValue())
    }

}