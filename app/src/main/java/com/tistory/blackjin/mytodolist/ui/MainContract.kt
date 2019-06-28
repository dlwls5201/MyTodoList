package com.tistory.blackjin.mytodolist.ui

import com.tistory.blackjin.mytodolist.room.Todo

interface MainContract {

    interface View {

        fun getEditTitle(): String

        fun getTime(): String

        fun setEditTitleNull()

        fun showEditTitleNotNullMessage()


        fun setTodos(items: List<Todo>)


        fun hideKeyboard()

        fun showEmptyMessage()

        fun hideEmptyMessage()

        fun showProgress()

        fun hideProgress()

    }

    interface Presenter {

        fun loadTodos()

        fun changeTodoChk(todo: Todo)

        fun insertTodo()

        fun deleteTodo(todo: Todo)
    }
}