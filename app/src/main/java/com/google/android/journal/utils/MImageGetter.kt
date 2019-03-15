package com.google.android.journal.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.journal.injection.GlideApp


/**
 * Idea From http://www.programmersought.com/article/8779111456/
 *
 **/
open class MImageGetter(internal var mTv: TextView, internal var mContext: Context) : Html.ImageGetter {
    override fun getDrawable(source: String): Drawable {
        val drawable = LevelListDrawable()
        GlideApp.with(mContext)
            .asBitmap()
            .load(source)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmapDrawable = BitmapDrawable(mContext.resources, bitmap)
                    drawable.addLevel(1, 1, bitmapDrawable)
                    drawable.setBounds(0, 0, bitmap.width, bitmap.height)
                    drawable.level = 1
                    mTv.invalidate()
                    mTv.text = mTv.text
                }
            })
        return drawable
    }
}

