package com.way2mars.kotlin.todoapp

import com.way2mars.kotlin.todoapp.model.TodoItem
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository

interface AppContract {

    fun showDetailsScreen(todoItem: TodoItem)
    fun goBack()
    fun toast(messageRes: Int)

}