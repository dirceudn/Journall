package com.google.android.journal

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.google.android.journal.helper.AppSection
import com.google.android.journal.helper.factory.FragmentFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if(!BuildConfig.FEATURE_FAVORITE_ENABLED){
            bottom_navigation.menu.removeItem(R.id.navigation_favorites)
        }

        if (savedInstanceState == null) {
            navigateToSection(AppSection.HOME, false, null)
        }
    }


    fun navigateToSection(section: AppSection, addToStack: Boolean, args: Bundle?) {

        val fragment = FragmentFactory.getFragment(section)
        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.frag_container, fragment)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            fragment.arguments = args
            if (addToStack)
                addToBackStack(fragment.getFragmentTag())
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_articles -> navigateToSection(AppSection.HOME, false, null)
            R.id.navigation_favorites -> navigateToSection(AppSection.FAVORITES, false, null)
        }
        return true

    }
}
