package com.goodlucklee.todolist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(
    private var todoList: List<DocumentSnapshot>,
    private val infalter: LayoutInflater,
    private val onClickDeleteIcon: (todo: DocumentSnapshot) -> Unit,
    private val onClickItem: (todo: DocumentSnapshot) -> Unit
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
        holder.todo.setText(todoElement.getString("text") ?: "")

        if (!(todoElement.getBoolean("isDone") ?: false)) {
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

    fun setData(newData: List<DocumentSnapshot>) {
        todoList = newData
        notifyDataSetChanged()
    }
}
