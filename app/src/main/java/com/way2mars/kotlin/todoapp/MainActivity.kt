package com.way2mars.kotlin.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.way2mars.kotlin.todoapp.databinding.ActivityMainBinding
import com.way2mars.kotlin.todoapp.screens.ScrollingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container_view, ScrollingFragment())
                .commit()
    }
}