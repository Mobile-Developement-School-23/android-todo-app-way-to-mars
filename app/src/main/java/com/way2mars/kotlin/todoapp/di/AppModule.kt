package com.way2mars.kotlin.todoapp.di

import android.content.Context
import dagger.Module

@Module
class AppModule(val context: Context){

    fun provideContext(): Context{
        return context
    }

}