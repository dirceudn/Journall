package com.google.android.journal.injection

import com.google.android.journal.AppExecutors
import com.google.android.journal.MainActivity
import com.google.android.journal.data.db.JournalDb
import com.google.android.journal.data.local.PostsRepository
import com.google.android.journal.ui.HomeFragment
import com.google.android.journal.ui.view.PostsViewModel
import com.google.gson.Gson
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (NetworkModule::class), (DataModule::class)])
interface AppComponent {

    val journalDb: JournalDb

    val postsRepository: PostsRepository

    val appExecutor: AppExecutors

    val gson: Gson

    fun inject(viewModel: PostsViewModel)

    fun inject(activity: MainActivity)

}