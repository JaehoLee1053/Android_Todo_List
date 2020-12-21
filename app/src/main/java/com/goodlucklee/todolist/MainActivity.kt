package com.goodlucklee.todolist

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.goodlucklee.todolist.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.android.synthetic.main.item_todo.view.*

// ViewModel Scope
/*
* Activity Create
* onStart() --> onResume() -->
* */
/*
* Activity rotate
* onPause() --> onStop() --> onDestroy()
* --> onStart() --> onResume() --> finish()
* */
/*
* onPause() --> onStop() --> onDestroy()
* Finished
* */
// onCleared()
// 새로운 Activity가 켜져서 기존 데이터가 사라짐

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = TodoAdapter(
            todoList = viewModel.todoList,
            LayoutInflater.from(this@MainActivity),
            onClickDeleteIcon = {
                viewModel.deleteTodo(it)
            },
            onClickItem = {
                viewModel.toggleTodo(it)
            }
        )
        recycler_view.adapter = adapter

        add_button.setOnClickListener {
            val todo = Todo(edit_text.text.toString())
            viewModel.addTodo(todo)
            edit_text.text.clear()
            recycler_view.adapter?.notifyDataSetChanged()
        }

        viewModel.todoLiveData.observe(this@MainActivity, Observer {
            (recycler_view.adapter as TodoAdapter).setData(it)
        })
    }

//    private fun addTodo() {
//        val todo = Todo(edit_text.text.toString())
//        todoList.add(todo)
//        edit_text.setText("")
//        recycler_view.adapter?.notifyDataSetChanged()
//    }
//
//    private fun deleteTodo(todo: Todo) {
//        todoList.remove(todo)
//        recycler_view.adapter?.notifyDataSetChanged()
//    }
//
//    private fun toggleTodo(todo: Todo) {
//        todo.isDone = !todo.isDone
//        recycler_view.adapter?.notifyDataSetChanged()
//    }
}


