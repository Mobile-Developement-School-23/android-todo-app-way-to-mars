package com.way2mars.kotlin.todoapp.model

import com.way2mars.kotlin.todoapp.utils.TestException
import java.util.*

/**
 * The repository of Todo_Items as StateFlow Repository
 *
 *  ТЗ
 *   - класс должен возвращать список дел
 *   - метод добавления нового дела (дело передаётся как аргумент функции)
 *   - в текущей реализации список дел сейчас можно захардкодить (минимум 10-20 значений)
 *   - дела должны быть максимально разнообразны, чтобы покрыть все комбинации возможных значений и
 *     проверить работу экрана наиболее полным образом
 */

typealias TodoItemListener = (todoItem: List<TodoItem>) -> Unit

class TodoItemsRepository {

    private var filter = false

    private var unfilteredItems = mutableListOf<TodoItem>()
    private val filteredItems
        get() = if (!filter) unfilteredItems else unfilteredItems.filter { !it.isCompleted }.toMutableList()

    private var listeners = mutableSetOf<TodoItemListener>()  // all the listeners

    // create 20 random TodoItems
    init {
        for (i in 1..20) unfilteredItems.add(todoItemFromRandom())
    }

//    fun getAllTasks() = todoItems
//
//    fun getSize() = todoItems.size
//
//    fun getItem(index: Int) = todoItems[index]

    fun getDoneCount(): Int = unfilteredItems.filter { it.isCompleted }.size

    fun getById(id: String): TodoItem {
        if (id == "") return newTask()
        val task = filteredItems.firstOrNull { it.id == id } ?: TestException()
        return task as TodoItem
    }

    fun setFilter(flag: Boolean) {
        filter = flag
        notifyChanges()
    }

    fun markDone(todoItem: TodoItem?) {
        if (todoItem == null) return
        val index = unfilteredItems.indexOfFirst { it.id == todoItem.id }
        if (index == -1) return  // do nothing if there's no such a task

        unfilteredItems = ArrayList(unfilteredItems)
        unfilteredItems[index] = unfilteredItems[index].copy(isCompleted = !unfilteredItems[index].isCompleted)
        notifyChanges()
    }

    fun removeItem(todoItem: TodoItem?) {
        if (todoItem == null) return
        val index = unfilteredItems.indexOfFirst { it.id == todoItem.id }
        if (index == -1) return  // do nothing if there's no such a task

        unfilteredItems = ArrayList(unfilteredItems)
        unfilteredItems.removeAt(index)
        notifyChanges()
    }

    fun moveItem(todoItem: TodoItem, moveBy: Int) {
        val oldIndex = unfilteredItems.indexOfFirst { it.id == todoItem.id }
        if (oldIndex == -1) return

        val newIndex = oldIndex + moveBy
        unfilteredItems = ArrayList(unfilteredItems)
        Collections.swap(unfilteredItems, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: TodoItemListener) {
        listeners.add(listener)
        listener.invoke(filteredItems)
    }

    fun removeListener(listener: TodoItemListener) {
        listeners.remove(listener)
        listener.invoke(filteredItems)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(filteredItems) }

}