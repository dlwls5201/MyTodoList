package com.tistory.blackjin.mytodolist

import com.tistory.blackjin.mytodolist.room.Todo
import com.tistory.blackjin.mytodolist.room.TodoDao
import com.tistory.blackjin.mytodolist.ui.MainContract
import com.tistory.blackjin.mytodolist.ui.MainPresenter
import com.tistory.blackjin.mytodolist.utils.schedulers.BaseSchedulerProvider
import com.tistory.blackjin.mytodolist.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    private lateinit var view: MainContract.View

    @Mock
    private lateinit var repository: TodoDao

    @Mock
    private lateinit var compositeDisposable: CompositeDisposable


    private var schedulerProvider: BaseSchedulerProvider? = null

    private lateinit var presenter: MainPresenter

    private lateinit var inOrder: InOrder



    @Before
    fun setUp() {

        schedulerProvider = ImmediateSchedulerProvider()

        presenter = MainPresenter(view, repository, compositeDisposable, schedulerProvider!!)

        inOrder = Mockito.inOrder(view, repository)

    }

    @Test
    fun test_load_todos() {

        val todos = listOf(Todo(1, "title1","today",false))

        `when`(repository.getAllTodo())
            .thenReturn(Flowable.just(todos))

        presenter.loadTodos()

        inOrder.verify(view).showProgress()

        inOrder.verify(repository).getAllTodo()

        inOrder.verify(view).hideEmptyMessage()
        inOrder.verify(view).setTodos(todos)
        inOrder.verify(view).hideProgress()
    }

    @Test
    fun test_load_todos_empty() {

        val todos = listOf<Todo>()

        `when`(repository.getAllTodo())
            .thenReturn(Flowable.just(todos))

        presenter.loadTodos()

        inOrder.verify(view).showProgress()

        inOrder.verify(repository).getAllTodo()

        inOrder.verify(view).showEmptyMessage()
        inOrder.verify(view).setTodos(todos)
        inOrder.verify(view).hideProgress()
    }


    @Test
    fun test_insert_todo() {

        `when`(view.getEditTitle()).thenReturn("blackJin")
        `when`(view.getTime()).thenReturn("2019")

        presenter.insertTodo()

        val title = view.getEditTitle()
        val time = view.getTime()

        inOrder.verify(repository).insert(
            Todo(title = title, time = time, chk = false)
        )

        inOrder.verify(view).setEditTitleNull()
        inOrder.verify(view).hideKeyboard()
    }

    @Test
    fun test_insert_todo_null() {

        `when`(view.getEditTitle()).thenReturn("")

        presenter.insertTodo()

        inOrder.verify(view).showEditTitleNotNullMessage()

    }
}