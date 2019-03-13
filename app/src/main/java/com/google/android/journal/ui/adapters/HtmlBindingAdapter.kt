package com.google.android.journal.ui.adapters

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import com.google.android.journal.utils.MImageGetter

object HtmlBindingAdapter {

    @JvmStatic
    @BindingAdapter("bindHtmlText")
    fun bindText(@NonNull textView: TextView, article: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(article,Html.FROM_HTML_MODE_LEGACY, MImageGetter(textView, textView.context), null)
        } else {
            textView.text = Html.fromHtml(article)
        }
    }


}