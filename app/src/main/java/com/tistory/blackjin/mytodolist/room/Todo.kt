package com.tistory.blackjin.mytodolist.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 엔티티는 데이터베이스에 저장할 데이터의 형식을 정의합니다
 */
@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val time: String,
    val chk: Boolean
)