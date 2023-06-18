package com.way2mars.kotlin.todoapp

import android.app.Application

class TodoApplication : Application() {

    private var instance: TodoApplication? = null

    fun getInstance(): TodoApplication? {
        return instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

