package com.goodlucklee.todolist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainViewModel : ViewModel() {
    val db = Firebase.firestore
    val todoLiveData = MutableLiveData<ArrayList<Todo>>()
    val todoList = ArrayList<Todo>()

    init {

    }

    fun fetchData() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            db.collection(user.uid)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    todoList.clear()
                    for (document in value!!) {
                        val todo = Todo(
                            document.getString("text") ?: "Empty",
                            document.getBoolean("isDone") ?: false
                        )
                        addTodo(todo)
                    }
                }
//                .get()
//                .addOnSuccessListener { result ->
//                    todoList.clear()
//                    for (document in result) {
//                        val todo = Todo(
//                            document.data["text"] as String,
//                            document.data["isDone"] as Boolean
//                        )
//                        addTodo(todo)
//                    }
//                }
        }
    }

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