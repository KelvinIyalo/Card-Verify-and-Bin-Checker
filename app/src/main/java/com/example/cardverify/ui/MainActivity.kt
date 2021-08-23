package com.example.cardverify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cardverify.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CardVerify)
        setContentView(R.layout.activity_main)
    }
}