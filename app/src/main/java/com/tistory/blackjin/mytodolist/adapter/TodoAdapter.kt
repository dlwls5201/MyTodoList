package com.tistory.blackjin.mytodolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tistory.blackjin.mytodolist.R
import com.tistory.blackjin.mytodolist.room.Todo
import com.tistory.blackjin.mytodolist.utils.TodoDiffCallback

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoHolder>() {

    private var mTodos: MutableList<Todo> = mutableListOf()

    private var mLlistener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TodoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )

    override fun getItemCount() = mTodos.size

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        mTodos[position].let { todo ->
            with(holder) {

                tvTitle.text = todo.title
                tvTime.text = todo.time
                checkBox.isChecked = todo.chk

                checkBox.setOnClickListener {
                    mLlistener?.onItemClickCheckBox(todo)
                }

                btnDelete.setOnClickListener { mLlistener?.onItemClickDelete(todo) }
            }
        }
    }

    fun updateListItems(newTodos: MutableList<Todo>) {

        val diffCallback = TodoDiffCallback(mTodos, newTodos)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mTodos.clear()
        mTodos.addAll(newTodos)

        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.mLlistener = listener
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