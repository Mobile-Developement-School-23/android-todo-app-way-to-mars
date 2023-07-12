package com.way2mars.kotlin.todoapp.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository
import com.way2mars.kotlin.todoapp.utils.TestException

class TaskViewModel(
     private val repository: TodoItemsRepository
) : ViewModel() {

    private val _task = MutableLiveData<TodoItem>()
    val task: LiveData<TodoItem> = _task

    fun loadTask(taskId: String){
        if (_task.value != null) return  // do nothing if task is already loaded up
        try {
            _task.value = repository.getById(taskId)
        } catch (e: TestException){
            e.printStackTrace()
        }
    }

    fun removeTask(){
        val todoItem = this.task.value ?: return
        repository.removeItem(todoItem)
    }

    fun saveTask(){


    }

}

