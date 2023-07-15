package com.way2mars.kotlin.todoapp

import com.way2mars.kotlin.todoapp.model.TodoItem

interface AppContract {

    fun showDetailsScreen(todoItem: TodoItem)
    fun createNewTask()
    fun goBack()
    fun toast(messageRes: Int)

}