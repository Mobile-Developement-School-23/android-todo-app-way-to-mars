package com.way2mars.kotlin.todoapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.way2mars.kotlin.todoapp.di.AppComponent
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository


private const val SHARED_PREFS_STATE = "shared_prefs_state"

class TodoApplication : Application() {

    val repository = TodoItemsRepository()

    lateinit var appComponent: AppComponent

    companion object {
        lateinit var sharedPreferences: SharedPreferences
            private set
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(SHARED_PREFS_STATE, Context.MODE_PRIVATE)
    }

}

