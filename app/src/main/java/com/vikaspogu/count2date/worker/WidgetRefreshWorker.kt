package com.vikaspogu.count2date.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.vikaspogu.count2date.ui.widget.DetailsWidget
import java.util.Calendar
import java.util.concurrent.TimeUnit

class WidgetRefreshWorker(context: Context,workerParameters: WorkerParameters): CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {
        refreshWidget(applicationContext)
        return Result.success()
    }

    private suspend fun refreshWidget(context: Context) {
        DetailsWidget().updateAll(context)
    }
}

fun scheduleDailyWidgetRefresh(context: Context) {
    // Calculate initial delay to midnight
    val currentTime = Calendar.getInstance()
    val midnightTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 5)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_YEAR, 1) // Set to next midnight
    }

    val delay = midnightTime.timeInMillis - currentTime.timeInMillis

    // Set constraints (optional)
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true) // Example constraint
        .build()

    // Build the periodic work request to refresh widget every 24 hours
    val widgetRefreshRequest = PeriodicWorkRequestBuilder<WidgetRefreshWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    // Enqueue the work request
    WorkManager.getInstance(context).enqueue(widgetRefreshRequest)
}