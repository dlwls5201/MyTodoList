package com.tistory.blackjin.mytodolist.utils

import androidx.recyclerview.widget.DiffUtil
import com.tistory.blackjin.mytodolist.room.Todo

//https://developer.android.com/reference/android/support/v7/util/DiffUtil.Callback
class TodoDiffCallback(
    private val oldList: MutableList<Todo>,
    private val newList: MutableList<Todo>
) : DiffUtil.Callback() {

    //Returns the size of the old list.
    override fun getOldListSize() = oldList.size

    //Returns the size of the new list
    override fun getNewListSize() = newList.size

    /**
     *  Called by the DiffUtil to decide whether two object represent the same Item.
     *  For example, if your items have unique ids, this method should check their id equality.
     *
     *  두 아이템이 서로 같은지 확인합니다. 고유 ID 값이 있는 경우 이를 사용해 비교해 줍니다.
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
        = oldList[oldItemPosition].id == newList[newItemPosition].id

    /**
     *  Called by the DiffUtil when it wants to check whether two items have the same data.
     *  DiffUtil uses this information to detect if the contents of an item has changed.
     *
     *  두 아이템안에 있는 데이터가 같은지를 확인해 줍니다.
     *  객체의 equals 메소드를 오버라드 하여 자바에서는 equals()를 사용하면 됩니다.
     *  코틀린 에서는 data class 를 만들면 자동으로 equals를 만들어 주기 때문에 아래와 같이 == 로 처리하면 됩니다.
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}