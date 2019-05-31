package com.tistory.blackjin.mytodolist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * 룸 데이터베이스에서 데이터베이스를 생성하거나 버전을 관리합니다.
 */
@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun getTodoDao(): TodoDao

    companion object {

        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {

            if (INSTANCE == null) {
                synchronized(TodoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        TodoDatabase::class.java,
                        "todos.db"
                    ).build()
                }
            }

            return INSTANCE!!
        }

    }
}