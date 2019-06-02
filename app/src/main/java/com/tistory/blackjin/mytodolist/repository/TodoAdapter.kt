package com.tistory.blackjin.mytodolist.repository

import com.tistory.blackjin.mytodolist.room.Todo
import io.reactivex.Flowable

interface TodoAdapter {

    fun getAllTodo(): Flowable<List<Todo>>

    fun clearAll()

    fun insert(vararg todo: Todo)

    fun update(vararg todo: Todo)

    fun delete(vararg todo: Todo)
}