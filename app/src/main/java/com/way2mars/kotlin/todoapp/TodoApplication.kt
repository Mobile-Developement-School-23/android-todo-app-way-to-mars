package com.way2mars.kotlin.todoapp

import android.app.Application
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.way2mars.kotlin.todoapp.model.TodoItemsRepository


class TodoApplication : Application() {

    val repository = TodoItemsRepository()

    companion object {
        var colorStateListGreenLight: ColorStateList? = null
            private set
        var colorStateListRed: ColorStateList? = null
            private set
        var imageTypeLow: Drawable? = null
            private set
        var imageTypeHigh: Drawable? = null
            private set

        var iconImportanceLow: Drawable? = null
            private set
        var iconImportanceCommon: Drawable? = null
        private set
        var iconImportanceHigh: Drawable? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        colorStateListGreenLight = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ), intArrayOf(
                ContextCompat.getColor(this, R.color.green_light_theme),
                ContextCompat.getColor(this, R.color.gray_light_light_theme)
            )
        )
        colorStateListRed = ColorStateList.valueOf(this.resources.getColor(R.color.red, null))
        imageTypeLow = ResourcesCompat.getDrawable(this.resources, R.drawable.arrow, null)
        imageTypeHigh = ResourcesCompat.getDrawable(this.resources, R.drawable.high_priority, null)

        iconImportanceLow = ResourcesCompat.getDrawable(this.resources, R.drawable.priority_low, null)
        iconImportanceCommon = ResourcesCompat.getDrawable(this.resources, R.drawable.priority_common, null)
        iconImportanceHigh = ResourcesCompat.getDrawable(this.resources, R.drawable.priority_high, null)
    }
}

