package com.vikaspogu.count2date.ui.utils

import android.icu.util.Calendar
import java.util.concurrent.TimeUnit

fun getDaysLeft(targetTimeInMillis: Long): Long {
    val currentTimeInMillis = getSystemTimeInMillsAtMidNight()
    val differenceInMillis = targetTimeInMillis - currentTimeInMillis
    val daysLeft = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
    return daysLeft
}

fun getSystemTimeInMillsAtMidNight(): Long {
    val rightNow: Calendar = Calendar.getInstance()
    // offset to add since we're not UTC
    val offset: Int = rightNow.get(Calendar.ZONE_OFFSET) +
            rightNow.get(Calendar.DST_OFFSET)
    val sinceMidnight: Long = (rightNow.getTimeInMillis() + offset) %
            (24 * 60 * 60 * 1000)
    return System.currentTimeMillis() - sinceMidnight
}