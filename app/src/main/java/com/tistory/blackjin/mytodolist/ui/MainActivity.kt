package com.tistory.blackjin.mytodolist.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tistory.blackjin.mytodolist.adapter.TodoAdapter
import com.tistory.blackjin.mytodolist.extensions.runOnIoScheduler
import com.tistory.blackjin.mytodolist.operator.plusAssign
import com.tistory.blackjin.mytodolist.room.Todo
import com.tistory.blackjin.mytodolist.room.TodoDatabase
import com.tistory.blackjin.mytodolist.utils.Dlog
import com.tistory.blackjin.mytodolist.utils.timeFormat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), TodoAdapter.ItemClickListener {

    private val todoAdapter by lazy {
        TodoAdapter().apply { setItemClickListener(this@MainActivity) }
    }

    private val todoDao by lazy {
        TodoDatabase.getInstance(this).getTodoDao()
    }

    private val imm by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tistory.blackjin.mytodolist.R.layout.activity_main)

        initButton()
        initRecyclerView()

        loadData()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onItemClickCheckBox(todo: Todo) {
        val tempTodo = todo.copy(chk = !todo.chk)

        compositeDisposable += runOnIoScheduler {
            todoDao.update(tempTodo)
        }
    }

    override fun onItemClickDelete(todo: Todo) {
        compositeDisposable += runOnIoScheduler {
            todoDao.delete(todo)
        }
    }

    private fun initButton() {

        fabActivityMain.setOnClickListener {

            val title = etActivityMain.text.toString()

            if (title.isEmpty()) {
                toast(getString(com.tistory.blackjin.mytodolist.R.string.empty_title))
            } else {

                compositeDisposable += runOnIoScheduler {
                    todoDao.insert(
                        Todo(title = title, time = timeFormat(), chk = false)
                    )
                }

                etActivityMain.text = null
                hideKeyboard()
            }
        }

    }

    private fun initRecyclerView() {

        with(rvActivityMain) {

            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }
    }

    private fun loadData() {

        showProgress()

        compositeDisposable += todoDao.getAllTodo()
            //.delay(3000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Dlog.d("$it")

                if (it.isNullOrEmpty()) {
                    showEmptyMessage()
                } else {
                    hideEmptyMessage()
                }

                todoAdapter.updateListItems(it.toMutableList())

                hideProgress()

            }) {
                Dlog.e(it.message)
            }
    }

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(etActivityMain.windowToken, 0)
    }

    private fun showEmptyMessage() {
        tvActivityMainEmpty.visibility = View.VISIBLE
    }

    private fun hideEmptyMessage() {
        tvActivityMainEmpty.visibility = View.GONE
    }

    private fun showProgress() {
        pbActivityMain.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        pbActivityMain.visibility = View.GONE
    }
}
