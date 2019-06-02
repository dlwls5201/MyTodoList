package com.tistory.blackjin.mytodolist.repository

import android.content.Context
import com.tistory.blackjin.mytodolist.api.TodoapiImpl
import com.tistory.blackjin.mytodolist.room.TodoDatabase

class TodoRepository(private val context: Context) {

    fun getApi(): TodoAdapter {
        return TodoapiImpl.getInstance(context)
    }

    fun getRoom(): TodoAdapter {
        return TodoDatabase.getInstance(context).getTodoDao()
    }
}