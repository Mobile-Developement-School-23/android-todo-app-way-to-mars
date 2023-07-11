package com.way2mars.kotlin.todoapp.model

import com.way2mars.kotlin.todoapp.utils.toFormatString
import com.way2mars.kotlin.todoapp.utils.toUnixTime
import java.util.*
import kotlin.random.Random

/**
 *  The storage for a single Todo_Item
 *
 *  ТЗ
 *   класс должен быть kotlin data class
 *   - id дела, обязательный параметр – String
 *   - текст дела, обязательный параметр – String
 *   - важность/значимость дела, обязательный параметр: должно быть 3 значения: “низкая”, “обычная”, “срочная»
 *   - дедлайн выполнения, опциональный параметр
 *   - флаг выполнения (сделана или нет), обязательный параметр
 *   - дата создания, обязательный параметр
 *   - дата изменения, опциональный параметр
 */

enum class Importance {
    LOW,
    COMMON,
    HIGH,
}

data class TodoItem(
    val id: String,  // Uuid
    val text: String,
    val importance: Importance,
    val deadline: Long?,  // Date?
    var isCompleted: Boolean = false,
    val dateCreated: Long,
    var dateModified: Long? = null
) {
    override fun toString(): String {
        return "'$id' '$text' importance=$importance " +
                "deadline=${deadline?.toFormatString() ?: "отсутствует"} " +
                "isCompleted=$isCompleted " +
                "dateCreated=${dateCreated.toFormatString()} " +
                "dateModified=${dateModified?.toFormatString() ?: "n/a"}"
    }
}

// fabric method to create a random TodoItem
fun todoItemFromRandom(): TodoItem {
    return TodoItem(
       // id = x.toString().padStart(12, '0'),
        id = UUID.randomUUID().toString(),

        text = when (Random.nextInt(0, 3)) {
            0 -> "Купить что-то"
            1 -> "Купить что-то, где-то, зачем-то, но зачем не очень понятно"
            else -> "Купить что-то, где-то, зачем-то, но зачем не очень понятно, " +
                    "но точно чтобы показать как обращаться с деньгами, которые " +
                    "неизвестно где брать"
        },
        importance = when (Random.nextInt(0, 3)) {
            1 -> Importance.COMMON
            2 -> Importance.HIGH
            else -> Importance.LOW
        },
        deadline = when (Random.nextInt(0, 2)) {
            1 -> null
            else -> Random.nextLong(
                "01-06-2023".toUnixTime(),
                "31-12-2023".toUnixTime() + 1,
            )
        },
        isCompleted = Random.nextBoolean(),
        dateCreated = "12-03-2023".toUnixTime(),
        dateModified = "18-06-2023".toUnixTime()
    )
}

fun newTask(): TodoItem{
    return TodoItem(
        id = UUID.randomUUID().toString(),
        text = "",
        importance = Importance.COMMON,
        deadline = null,
        isCompleted = false,
        dateCreated = System.currentTimeMillis(),
        dateModified = null
    )
}