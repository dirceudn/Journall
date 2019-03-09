package com.google.android.journal.helper.factory

import com.google.android.journal.helper.AppSection
import com.google.android.journal.ui.DetailFragment
import com.google.android.journal.ui.FavoritesFragment
import com.google.android.journal.ui.HomeFragment

class FragmentFactory {

    companion object {
        fun getFragment(appSection: AppSection): AppFragment {
            return when (appSection) {
                AppSection.HOME -> HomeFragment()
                AppSection.FAVORITES -> FavoritesFragment()
                AppSection.POST_DETAIL -> DetailFragment()
                else -> HomeFragment()
            }
        }
    }

}