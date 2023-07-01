package com.way2mars.kotlin.todoapp

import com.way2mars.kotlin.todoapp.model.TodoItem

interface Navigator {

    fun showDetails(todoItem: TodoItem)
    fun goBack()
    fun toast(messageRes: Int)

}