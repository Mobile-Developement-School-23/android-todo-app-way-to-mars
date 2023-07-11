package com.way2mars.kotlin.todoapp.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.model.TodoItemListener
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository

class ScrollingViewModel(
    private val repository: TodoItemsRepository
): ViewModel() {

    private val _tasks = MutableLiveData<List<TodoItem>>()
    val tasks: LiveData<List<TodoItem>> = _tasks
    val countDone
        get() = repository.getDoneCount()

    private val listener: TodoItemListener = {
        _tasks.value = it
    }

    init {
        loadItems()
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeListener(listener)
    }

    fun loadItems(){
        repository.addListener(listener)
    }

    fun moveItem(task: TodoItem, moveBy: Int){
        repository.moveItem(task, moveBy)
    }

    fun removeItem(task: TodoItem) {
        repository.removeItem(task)
    }

    fun markDone(task: TodoItem){
        repository.markDone(task)
    }

    fun setFilter(flag: Boolean){
        repository.setFilter(flag)
    }

}