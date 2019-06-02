package com.tistory.blackjin.mytodolist.api

import com.tistory.blackjin.mytodolist.repository.TodoAdapter
import com.tistory.blackjin.mytodolist.room.Todo
import io.reactivex.Flowable

//TODO 서버 통신일 경우 어노테이션을 추가해줍니다.
interface TodoApi : TodoAdapter

