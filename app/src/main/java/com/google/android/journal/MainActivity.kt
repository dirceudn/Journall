package com.google.android.journal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.journal.ui.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frag_container, HomeFragment()).commit()
        }
    }
}
