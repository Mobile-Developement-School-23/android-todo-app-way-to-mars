package com.way2mars.kotlin.todoapp.model

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

    private var todoItems = mutableListOf<TodoItem>()  // All the tasks

    private var listeners = mutableSetOf<TodoItemListener>()  // all the listeners

    // create 20 random TodoItems
    init { for (i in 1..20)  todoItems.add(todoItemFromRandom(id = i)) }

    fun getTasks() = todoItems

    fun getSize() = todoItems.size

    fun getItem(index: Int) = todoItems[index]

    fun markDone(todoItem: TodoItem?){
        if (todoItem == null) return
        val index = todoItems.indexOfFirst { it.id == todoItem.id }
        if (index == -1) return  // do nothing if there's no such a task

        todoItems = ArrayList(todoItems)
        todoItems[index] = todoItems[index].copy(isCompleted = !todoItems[index].isCompleted)
        notifyChanges()
    }

    fun removeTodoItem(todoItem: TodoItem?) {
        if (todoItem == null) return
        val index = todoItems.indexOfFirst { it.id == todoItem.id }
        if (index == -1) return  // do nothing if there's no such a task

        todoItems = ArrayList(todoItems)
        todoItems.removeAt(index)
        notifyChanges()
    }

    fun addListener(listener: TodoItemListener){
        listeners.add(listener)
        listener.invoke(todoItems)
    }

    fun removeListener(listener: TodoItemListener){
        listeners.remove(listener)
        listener.invoke(todoItems)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(todoItems) }

}