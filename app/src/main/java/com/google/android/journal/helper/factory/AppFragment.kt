package com.google.android.journal.helper.factory

import androidx.fragment.app.Fragment
import com.google.android.journal.MainActivity
import com.google.android.journal.helper.AppSection

open class AppFragment : Fragment() {

    lateinit var appSection: AppSection

    internal var isRootSection = false

    /**
     * get a unique string identifier for this fragment. Can be used as a key to add
     * into the back stack.
     *
     * @return unique tag
     */
    fun getFragmentTag(): String {
        return this.appSection.toString()
    }

    /**
     * obtain the current instance of the activity holding this fragment.
     *
     * @return main activity instance
     */
    internal fun getMainActivity(): MainActivity {
        return activity as MainActivity
    }

    fun getIsRootSection(): Boolean {
        return this.isRootSection
    }
}