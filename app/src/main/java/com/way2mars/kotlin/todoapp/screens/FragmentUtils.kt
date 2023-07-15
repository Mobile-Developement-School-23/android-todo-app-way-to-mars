package com.way2mars.kotlin.todoapp.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.way2mars.kotlin.todoapp.AppContract
import com.way2mars.kotlin.todoapp.TodoApplication

class ViewModelFactory(
    private val app: TodoApplication
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ScrollingViewModel::class.java -> {
                ScrollingViewModel(app.repository)
            }

            TaskViewModel::class.java -> {
                TaskViewModel(app.repository)
            }

            else -> {
                return super.create(modelClass)
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as TodoApplication)

fun Fragment.contract() = requireActivity() as AppContract