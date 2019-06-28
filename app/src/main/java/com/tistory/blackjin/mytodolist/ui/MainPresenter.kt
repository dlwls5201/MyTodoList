package com.tistory.blackjin.mytodolist.ui

import com.tistory.blackjin.mytodolist.extensions.runOnIoScheduler
import com.tistory.blackjin.mytodolist.operator.plusAssign
import com.tistory.blackjin.mytodolist.room.Todo
import com.tistory.blackjin.mytodolist.room.TodoDao
import com.tistory.blackjin.mytodolist.utils.Dlog
import com.tistory.blackjin.mytodolist.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainPresenter(
    private val view: MainContract.View,
    private val repository: TodoDao,
    private val compositeDisposable: CompositeDisposable,
    private val schedulerProvider: BaseSchedulerProvider
): MainContract.Presenter {

    override fun loadTodos() {
        view.showProgress()

        compositeDisposable += repository.getAllTodo()
            //.delay(3000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({

                Dlog.d("$it")

                if (it.isNullOrEmpty()) {
                    view.showEmptyMessage()
                } else {
                    view.hideEmptyMessage()
                }

                view.setTodos(it)

                view.hideProgress()

            }) {
                Dlog.e(it.message)
            }
    }

    override fun changeTodoChk(todo: Todo) {
        val tempTodo = todo.copy(chk = !todo.chk)

        compositeDisposable += runOnIoScheduler {
            repository.update(tempTodo)
        }
    }

    override fun insertTodo() {

        val title = view.getEditTitle()
        val time = view.getTime()

        if (title.isEmpty()) {
            view.showEditTitleNotNullMessage()
        } else {

            compositeDisposable += runOnIoScheduler {
                repository.insert(
                    Todo(title = title, time = time, chk = false)
                )
            }

            view.setEditTitleNull()
            view.hideKeyboard()
        }


    }

    override fun deleteTodo(todo: Todo) {
        compositeDisposable += runOnIoScheduler {
            repository.delete(todo)
        }
    }
}