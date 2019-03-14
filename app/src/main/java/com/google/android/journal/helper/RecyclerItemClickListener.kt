package com.google.android.journal.helper

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * CopyRight Created by Quantum on 8/31/2017.
 * Thanks for the help
 */

class RecyclerItemClickListener(_context: Context, _recyclerview: RecyclerView, _listener: OnItemClickListener) :
    RecyclerView.OnItemTouchListener {

    private var mListener: OnItemClickListener = _listener

    var mGestureDetector: GestureDetector

    init {

        mGestureDetector = GestureDetector(_context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = _recyclerview.findChildViewUnder(e.x, e.y)

                if (childView != null) {
                    mListener.onItemLongClick(childView, _recyclerview.getChildAdapterPosition(childView))
                }
            }
        })
    }


    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)

        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
        }

        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }


}