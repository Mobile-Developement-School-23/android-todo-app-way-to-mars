package com.way2mars.kotlin.todoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.way2mars.kotlin.todoapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_fragment_container_view, ScrollingFragment())
            .commit()
    }
}