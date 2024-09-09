package com.vikaspogu.count2date.ui.utils

import android.icu.util.Calendar
import java.util.concurrent.TimeUnit

fun getDaysLeft(targetTimeInMillis: Long): Long {
    val currentTimeInMillis = System.currentTimeMillis()
    val differenceInMillis = targetTimeInMillis - currentTimeInMillis
    val daysLeft = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
    return daysLeft
}

fun getYesterdaysDate(): Long {
    val cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);
    return cal.timeInMillis;
}