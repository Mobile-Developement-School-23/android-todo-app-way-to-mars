package com.way2mars.kotlin.todoapp.utils

import java.text.SimpleDateFormat
import java.util.*

// converts UNIX-time to a string
fun Long?.toFormatString(): String {
    this ?: return ""
    val date = Date(this)
    return SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date)
}

// converts a string to UNIX-time (e.g. "22-06-2007")
fun String?.toUnixTime(): Long {
    this ?: return 0
    var date: Date? = null
    try {
        date = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).parse(this) ?: return 0
    }
    catch (e: Exception){
        e.printStackTrace()
    }
    if (date == null) {
        try {
            date = SimpleDateFormat("d-MM-yyyy", Locale.getDefault()).parse(this) ?: return 0
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
    return date?.time ?: 0
}




// dp --> pixels
//val Number.toPx
//    get() = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP,
//        this.toFloat(),
//        Resources.getSystem().displayMetrics
//    )