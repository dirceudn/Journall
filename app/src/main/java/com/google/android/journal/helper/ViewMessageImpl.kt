package com.google.android.journal.helper

import android.view.View
import android.widget.Toast
import com.google.android.journal.helper.interfaces.ViewMessage
import com.google.android.material.snackbar.Snackbar

abstract class ViewMessageImpl : ViewMessage {

    fun showToastMessage(view: View, message: String) {
        Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
    }

    fun showSnackMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}