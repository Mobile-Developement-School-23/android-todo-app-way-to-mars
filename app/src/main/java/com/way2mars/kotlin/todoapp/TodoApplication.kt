package com.way2mars.kotlin.todoapp

import android.app.Application
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class TodoApplication : Application() {

    companion object {
        var colorStateListGreenLight: ColorStateList? = null
            private set

        var colorStateListRed: ColorStateList? = null
            private set

        var imageTypeCommon: Drawable? = null
            private set

        var imageTypeHigh: Drawable? = null
            private set

    }

    override fun onCreate() {
        super.onCreate()

        colorStateListGreenLight = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked)
            ), intArrayOf(
                ContextCompat.getColor(this, R.color.green_light_theme),
                ContextCompat.getColor(this, R.color.gray_light_light_theme)
            )
        )
        colorStateListRed = ColorStateList.valueOf(this.resources.getColor(R.color.red, null))
        imageTypeCommon = ResourcesCompat.getDrawable(this.resources, R.drawable.arrow, null)
        imageTypeHigh = ResourcesCompat.getDrawable(this.resources, R.drawable.high_priority, null)
    }
}

