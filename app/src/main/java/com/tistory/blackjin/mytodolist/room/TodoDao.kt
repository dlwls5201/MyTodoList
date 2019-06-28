package com.tistory.blackjin.mytodolist.room

import androidx.room.*
import io.reactivex.Flowable

/**
 * 데이터의 삽입, 수정, 삭제 작업이나 저장된 데이터를 불러오는 작업 등을 함수 형태로 정의 합니다.
 */
@Dao
interface TodoDao {

    @Query("SELECT * FROM todos")
    fun getAllTodo(): Flowable<List<Todo>>

    @Query("DELETE FROM todos")
    fun clearAll()

    //해당 데이터를 추가합니다.
    //이미 저장된 항목이 있을 경우 데이터를 덮어씁니다.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg todo: Todo)

    //헤당 데이터를 업데이트 합니다.
    @Update
    fun update(vararg todo: Todo)

    //해당 데이터를 삭제합니다.
    @Delete
    fun delete(vararg todo: Todo)
}