package com.tistory.blackjin.mytodolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjin.mytodolist.R
import com.tistory.blackjin.mytodolist.room.Todo


class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoHolder>() {

    private var todos: MutableList<Todo> = mutableListOf()

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TodoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )

    override fun getItemCount() = todos.size

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        todos[position].let {todo ->
            with(holder) {

                tvTitle.text = todo.title
                tvTime.text = todo.time
                checkBox.isChecked = todo.chk

                checkBox.setOnClickListener {
                    listener?.onItemClickCheckBox(todo)
                }

                btnDelete.setOnClickListener { listener?.onItemClickDelete(todo) }
            }
        }
    }

    fun setItems(todos: MutableList<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    class TodoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.tvItemTodoTitle)
        val tvTime = itemView.findViewById<TextView>(R.id.tvItemTodoTime)
        val checkBox = itemView.findViewById<CheckBox>(R.id.cbItemTodo)
        val btnDelete = itemView.findViewById<Button>(R.id.btnItemTodoDelete)
    }

    interface ItemClickListener {

        fun onItemClickCheckBox(todo: Todo)

        fun onItemClickDelete(todo: Todo)
    }
}