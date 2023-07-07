package com.way2mars.kotlin.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.way2mars.kotlin.todoapp.databinding.ActivityMainBinding
import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.screens.ScrollingFragment
import com.way2mars.kotlin.todoapp.screens.TaskFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    private val currentFragment: Fragment  // returns actual fragment in a container
        get() = supportFragmentManager.findFragmentById(R.id.main_fragment_container_view)!!

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

    override fun showDetails(todoItem: TodoItem) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_fragment_container_view, TaskFragment.newInstance(todoItem.id))
            .commit()
    }

    override fun goBack() {
        //onBackPressed()
        onBackPressedDispatcher.onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Snackbar.make(binding.root, messageRes, Snackbar.LENGTH_SHORT).show()
    }


}