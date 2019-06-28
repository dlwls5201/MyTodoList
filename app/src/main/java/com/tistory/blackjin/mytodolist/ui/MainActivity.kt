package com.tistory.blackjin.mytodolist.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tistory.blackjin.mytodolist.adapter.TodoAdapter
import com.tistory.blackjin.mytodolist.extensions.runOnIoScheduler
import com.tistory.blackjin.mytodolist.operator.plusAssign
import com.tistory.blackjin.mytodolist.room.Todo
import com.tistory.blackjin.mytodolist.room.TodoDatabase
import com.tistory.blackjin.mytodolist.utils.Dlog
import com.tistory.blackjin.mytodolist.utils.timeFormat
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.BufferedInputStream
import java.net.URL
import android.graphics.drawable.Drawable
import java.io.InputStream
import org.jsoup.Jsoup
import java.util.regex.Pattern
import android.R.attr
import io.reactivex.Single
import java.util.concurrent.TimeUnit


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

        getData(intent)

        initButton()
        initRecyclerView()

        loadData()
    }

    override fun onNewIntent(intent: Intent) {
        Dlog.e("onNewIntent")
        getData(intent)
        super.onNewIntent(intent)
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

    private fun getData(intent: Intent) {

        val action = intent.action
        Dlog.d("action : $action")

        if (action == Intent.ACTION_SEND) {
            val type = intent.type
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            Dlog.d("type : $type , text : $text")

            if(text.isNotEmpty()) {
                val url = getUrlFromText(text)
                Dlog.d("url :  $url")

                getImageFromURL(url)
            }
        }
    }

    //https://jizard.tistory.com/173
    private fun getImageFromURL(url: String) {

        compositeDisposable += Single.fromCallable {
            val doc = Jsoup.connect(url).get()
            //Dlog.d("doc : $doc")

            //오픈 그래프(og)
            //https://brunch.co.kr/@jiyeonsongofnt/11
            val title = doc.select("meta[property=og:title]").first().attr("content")
            val description = doc.select("meta[property=og:description]")[0].attr("content")
            val imageUrl = doc.select("meta[property=og:image]")[0].attr("content")

            Dlog.e("title : $title")
            Dlog.e("description : $description")
            Dlog.e("imageUrl : $imageUrl")

            imageUrl
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ imageUrl ->

                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .into(ivActivityMain)

            }){

            }

    }

    private fun getUrlFromText(text: String): String {
        val urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"

        //Pattern.CASE_INSENSITIVE -> 대소문자 구분 없에 패턴을 매칭 시킨
        val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher = pattern.matcher(text)

        return if(urlMatcher.find()) {
            text.substring(urlMatcher.start(), urlMatcher.end())
        } else {
            ""
        }

        /*val urls = arrayListOf<String>()

        while (urlMatcher.find())
        {
            urls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)))
        }

        val str = StringBuffer()
        urls.forEach {
            str.append(it)
        }

        return str.toString()*/
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
