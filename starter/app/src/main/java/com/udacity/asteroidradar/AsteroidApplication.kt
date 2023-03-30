package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDataWork
import com.udacity.asteroidradar.work.RemovePreviousDayWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {

    private val applicationCoroutineScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() {
        applicationCoroutineScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val refreshDataPeriodicRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        val removePreviousDayPeriodRequest = PeriodicWorkRequestBuilder<RemovePreviousDayWork>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshDataPeriodicRequest
        )

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RemovePreviousDayWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            removePreviousDayPeriodRequest
        )
    }
}