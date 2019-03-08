package com.google.android.journal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.journal.R
import com.google.android.journal.helper.factory.AppFragment

class FavoritesFragment: AppFragment() {

    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.favorites_fragment, container, false)

        return rootView

    }
}