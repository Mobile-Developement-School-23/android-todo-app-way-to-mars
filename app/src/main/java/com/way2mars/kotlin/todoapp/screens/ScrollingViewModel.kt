package com.way2mars.kotlin.todoapp.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.way2mars.kotlin.todoapp.TodoApplication
import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.model.TodoItemListener
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository

private const val KEY_FILTER_FLAG = "key_filter_flag"

class ScrollingViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {

    private val _tasks = MutableLiveData<List<TodoItem>>()
    val tasks: LiveData<List<TodoItem>> = _tasks

    private val _countDone = MutableLiveData<Int>()
    val countDone: LiveData<Int> = _countDone

    val filterState
        get() = repository.filter

    private val listener: TodoItemListener = {
        _tasks.value = it
        _countDone.value = repository.getDoneCount()
    }

    init {
        loadItems()
        loadState()
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeListener(listener)
    }

    private fun loadItems() {
        repository.addListener(listener)
    }

    fun moveItem(task: TodoItem, moveBy: Int) {
        repository.moveItem(task, moveBy)
    }

    fun removeItem(task: TodoItem) {
        repository.removeItem(task)
    }

    fun markDone(task: TodoItem) {
        repository.markDone(task)
    }

    fun setFilter(flag: Boolean) {
        repository.filter = flag
        TodoApplication.sharedPreferences.edit().putBoolean(KEY_FILTER_FLAG, flag).apply()
    }

    private fun loadState() {
        val flag = TodoApplication.sharedPreferences.getBoolean(KEY_FILTER_FLAG, true)
        repository.filter = flag
    }

}