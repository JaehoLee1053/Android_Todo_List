package com.goodlucklee.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val todoLiveData = MutableLiveData<ArrayList<Todo>>()
    val todoList = ArrayList<Todo>()

    fun addTodo(todo: Todo) {
        todoList.add(todo)
        todoLiveData.value = todoList
    }

    fun deleteTodo(todo: Todo) {
        todoList.remove(todo)
        todoLiveData.value = todoList
    }

    fun toggleTodo(todo: Todo) {
        todo.isDone = !todo.isDone
        todoLiveData.value = todoList
    }
}