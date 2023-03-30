package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getFormattedDatePreviousDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWork(applicationContext: Context, params: WorkerParameters) :
    CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.fetchAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        val WORK_NAME: String = "RefreshingDataWorker"
    }
}

class RemovePreviousDayWork(applicationContext: Context, params: WorkerParameters) :
    CoroutineWorker(applicationContext, params) {
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.removePreviousDay(getFormattedDatePreviousDay())
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        val WORK_NAME: String = "RemovingPreviousDayWorker"
    }
}