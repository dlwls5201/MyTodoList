package com.tistory.blackjin.mytodolist.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tistory.blackjin.mytodolist.R
import com.tistory.blackjin.mytodolist.adapter.TodoAdapter
import com.tistory.blackjin.mytodolist.room.Todo
import com.tistory.blackjin.mytodolist.room.TodoDatabase
import com.tistory.blackjin.mytodolist.utils.schedulers.SchedulerProvider
import com.tistory.blackjin.mytodolist.utils.timeFormat
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainContract.View, TodoAdapter.ItemClickListener {


    private val todoAdapter by lazy {
        TodoAdapter().apply { setItemClickListener(this@MainActivity) }
    }

    private val todoDao by lazy {
        TodoDatabase.getInstance(this).getTodoDao()
    }

    private val imm by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private lateinit var presenter: MainContract.Presenter

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this,
            todoDao,
            compositeDisposable,
            SchedulerProvider.getInstance()
        )

        initButton()
        initRecyclerView()

        presenter.loadTodos()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onItemClickCheckBox(todo: Todo) {
        presenter.changeTodoChk(todo)
    }

    override fun onItemClickDelete(todo: Todo) {
        presenter.deleteTodo(todo)
    }

    private fun initButton() {
        fabActivityMain.setOnClickListener {
            presenter.insertTodo()
        }
    }

    private fun initRecyclerView() {
        with(rvActivityMain) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }
    }

    override fun getEditTitle() = etActivityMain.text.toString()

    override fun getTime() = timeFormat()

    override fun setEditTitleNull() {
        etActivityMain.text = null
    }

    override fun showEditTitleNotNullMessage() {
        toast(getString(R.string.empty_title))
    }

    override fun setTodos(items: List<Todo>) {
        todoAdapter.updateListItems(items.toMutableList())
    }

    override fun hideKeyboard() {
        imm.hideSoftInputFromWindow(etActivityMain.windowToken, 0)
    }

    override fun showEmptyMessage() {
        tvActivityMainEmpty.visibility = View.VISIBLE
    }

    override fun hideEmptyMessage() {
        tvActivityMainEmpty.visibility = View.GONE
    }

    override fun showProgress() {
        pbActivityMain.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pbActivityMain.visibility = View.GONE
    }
}
