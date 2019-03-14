package com.google.android.journal.helper.factory

import com.google.android.journal.helper.AppSection
import com.google.android.journal.ui.DetailFragment
import com.google.android.journal.ui.FavoritesFragment
import com.google.android.journal.ui.HomeFragment

/**
 * This class is responsible to manager all fragments created
 */
class FragmentFactory {

    companion object {
        fun getFragment(appSection: AppSection) = when (appSection) {
            AppSection.HOME -> HomeFragment()
            AppSection.FAVORITES -> FavoritesFragment()
            AppSection.POST_DETAIL -> DetailFragment()
            else -> HomeFragment()
        }
    }
}

