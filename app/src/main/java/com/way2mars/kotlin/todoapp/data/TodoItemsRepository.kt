package com.way2mars.kotlin.todoapp.data

import kotlinx.coroutines.flow.MutableStateFlow

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

class TodoItemsRepository {

    private val _todoItems = mutableListOf<TodoItem>()
    private val todoItemsFlow: MutableStateFlow<List<TodoItem>> = MutableStateFlow(mutableListOf())

    // create 20 random TodoItems
    init {
        for (i in 1..20) {
            _todoItems.add(todoItemFromRandom(id = i))
        }
    }

    fun getFlow() = todoItemsFlow

    fun getSize() = _todoItems.size

    fun getItem(index: Int) = _todoItems[index]

    fun addItem(todoItem: TodoItem) {
        _todoItems.add(todoItem)
        todoItemsFlow.tryEmit(_todoItems)
    }

    fun updateRepository(todoItem: TodoItem) {
        val positionTodo = _todoItems.indexOfFirst { it.id == todoItem.id }
        if (positionTodo != -1) {
            _todoItems[positionTodo] = todoItem
            todoItemsFlow.tryEmit(_todoItems)
        }
    }

    fun deleteTodo(todoItem: TodoItem?) {
        if (todoItem == null) return

//        val newList = mutableListOf<TodoItem>()
//        _todoItems.forEach {
//            if (it != todoItem) {
//                newList.add(it)
//            }
//        }
//        todoItemsFlow.tryEmit(newList)
//        _todoItems.remove(todoItem)
        if (_todoItems.remove(todoItem)) {
            todoItemsFlow.tryEmit(_todoItems)
        }
    }
}