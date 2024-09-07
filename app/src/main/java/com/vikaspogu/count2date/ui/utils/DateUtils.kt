package com.vikaspogu.count2date.ui.utils

import java.util.concurrent.TimeUnit

fun getDaysLeft(targetTimeInMillis: Long): Long {
    val currentTimeInMillis = System.currentTimeMillis()
    val differenceInMillis = targetTimeInMillis - currentTimeInMillis
    val daysLeft = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
    return daysLeft
}