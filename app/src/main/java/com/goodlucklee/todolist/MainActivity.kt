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
            viewModel.todoList,
            LayoutInflater.from(this@MainActivity),
            onClickDeleteIcon = {
                viewModel.deleteTodo(it)
                recycler_view.adapter?.notifyDataSetChanged()
            },
            onClickItem = {
                viewModel.toggleTodo(it)
                recycler_view.adapter?.notifyDataSetChanged()
            }
        )
        recycler_view.adapter = adapter

        add_button.setOnClickListener {
            val todo = Todo(edit_text.text.toString())
            viewModel.addTodo(todo)
            edit_text.text.clear()
            recycler_view.adapter?.notifyDataSetChanged()
        }
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

class TodoAdapter(
    private var todoList: ArrayList<Todo>,
    private val infalter: LayoutInflater,
    private val onClickDeleteIcon: (todo: Todo) -> Unit,
    private val onClickItem: (todo: Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todo: TextView
        val delete: ImageView

        init {
            todo = itemView.findViewById(R.id.todo_text)
            delete = itemView.findViewById(R.id.delete_image_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = infalter.inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoElement = todoList.get(position)
        holder.todo.setText(todoElement.text)

        if (!todoElement.isDone) {
            holder.todo.todo_text.apply {
                paintFlags = 0
            }
        } else {
            holder.todo.todo_text.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        holder.delete.setOnClickListener {
            onClickDeleteIcon.invoke(todoElement)
        }

        holder.todo.todo_text.setOnClickListener {
            onClickItem.invoke(todoElement)
        }
    }
}

class MainViewModel : ViewModel() {
    val todoList = ArrayList<Todo>()

    fun addTodo(todo: Todo) {
        todoList.add(todo)
    }

    fun deleteTodo(todo: Todo) {
        todoList.remove(todo)
    }

    fun toggleTodo(todo: Todo) {
        todo.isDone = !todo.isDone
    }
}