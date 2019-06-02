package com.tistory.blackjin.mytodolist.api

import android.content.Context
import com.tistory.blackjin.mytodolist.room.Todo
import com.tistory.blackjin.mytodolist.ui.MainActivity
import com.tistory.blackjin.mytodolist.utils.Dlog
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

class TodoapiImpl private constructor() : TodoApi {

    companion object {

        var INSTANCE: TodoapiImpl? = null

        fun getInstance(context: Context): TodoapiImpl {

            if(INSTANCE == null) {
                INSTANCE = TodoapiImpl()
            }

            return INSTANCE!!
        }
    }

    private val todos = arrayListOf(
        Todo(0, "Title1","2019.05.01", false),
        Todo(1, "Title2","2019.05.01", false),
        Todo(2, "Title3","2019.05.01", false),
        Todo(3, "Title4","2019.05.01", false),
        Todo(4, "Title5","2019.05.01", false)
    )

    override fun getAllTodo(): Flowable<List<Todo>> {
        return Flowable.just(todos.toList())
            .delay(3000, TimeUnit.MILLISECONDS)
    }

    override fun clearAll() {
        getAllTodo()
    }

    override fun insert(vararg todo: Todo) {
        Dlog.d("insert : ${Arrays.toString(todo)}")
        todos.add(todo[0])
    }

    override fun update(vararg todo: Todo) {
        Dlog.d("update : ${Arrays.toString(todo)}")

        var index = 0

        for((i, value) in todos.withIndex()) {
            if(value.id == todo[0].id) {
                index = i
            }
        }

        Dlog.d("index : $index")
        todos[index] = todo[0]
    }

    override fun delete(vararg todo: Todo) {
        Dlog.d("delete : ${Arrays.toString(todo)}")
        todos.remove(todo[0])
    }

}