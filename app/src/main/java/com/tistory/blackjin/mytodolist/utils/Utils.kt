package com.tistory.blackjin.mytodolist.utils

import java.text.SimpleDateFormat

fun timeFormat(): String {

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    val formatTime = format.format(System.currentTimeMillis())

    return formatTime
}