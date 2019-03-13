package com.google.android.journal.ui.adapters

import android.widget.TextView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateBindingAdapter{

    @JvmStatic
    @BindingAdapter("bindServerDate")
    fun bindServerDate(@NonNull textView: TextView, dateTime: String?) {
        val readFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
        val writeFormat = SimpleDateFormat("dd/MM/YYYY ", Locale.ENGLISH)
        var date: Date? = null
        try {
            date = readFormat.parse(dateTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var formattedDate = ""
        if (date != null) {
            formattedDate = writeFormat.format(date)
        }


        textView.text = formattedDate
    }

}