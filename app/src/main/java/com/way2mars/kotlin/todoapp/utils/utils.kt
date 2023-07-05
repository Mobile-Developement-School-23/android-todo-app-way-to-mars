package com.way2mars.kotlin.todoapp.utils

import java.text.SimpleDateFormat
import java.util.*

// converts UNIX-time to a string
fun Long?.toFormatString(): String {
    val date = Date(this ?: 0)
    return SimpleDateFormat("d MMM yyyy", Locale("ru")).format(date)
}

// converts a string to UNIX-time (e.g. "22-06-2007")
fun String?.toUnixTime(): Long {
    if (this == null) return 0
    val date = SimpleDateFormat("dd-MM-yyyy", Locale("ru")).parse(this) ?: return 0
    return date.time
}




// dp --> pixels
//val Number.toPx
//    get() = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP,
//        this.toFloat(),
//        Resources.getSystem().displayMetrics
//    )