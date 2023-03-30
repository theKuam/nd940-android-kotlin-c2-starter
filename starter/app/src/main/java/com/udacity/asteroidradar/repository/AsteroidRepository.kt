package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asEntity
import com.udacity.asteroidradar.network.asModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {

    suspend fun fetchAsteroids() = withContext(Dispatchers.IO) {
        val response = AsteroidApi.retrofitService.getAsteroidsAsync().await()
        val body = response.string()
        try {
            val jsonObject = JSONObject(body)
            val asteroids = parseAsteroidsJsonResult(jsonObject)
            asteroidDatabase.asteroidDao.insertAll(*asteroids.asEntity())
        } catch (e: JSONException) {
            Timber.e(e.message.toString())
        }
    }

    suspend fun fetchAsteroidsNextSevenDays() = withContext(Dispatchers.IO) {
        val response = AsteroidApi.retrofitService.getAsteroidsNextSevenDaysAsync().await()
        val body = response.string()
        try {
            val jsonObject = JSONObject(body)
            val asteroids = parseAsteroidsJsonResult(jsonObject)
            asteroidDatabase.asteroidDao.insertAll(*asteroids.asEntity())
        } catch (e: JSONException) {
            Timber.e(e.message.toString())
        }
    }

    suspend fun getSavedAsteroids() = withContext(Dispatchers.IO) {
        asteroidDatabase.asteroidDao.getAsteroids().asModel()
    }

    suspend fun getWeekAsteroids(theNextDay: String, nextSevenDay: String) =
        withContext(Dispatchers.IO) {
            asteroidDatabase.asteroidDao.getWeekAsteroids(theNextDay, nextSevenDay).asModel()
        }

    suspend fun getTodayAsteroids(today: String) = withContext(Dispatchers.IO) {
        asteroidDatabase.asteroidDao.getTodayAsteroids(today).asModel()
    }

    suspend fun getPictureOfTheDay(): PictureOfDay = withContext(Dispatchers.IO) {
        AsteroidApi.retrofitService.getPictureOfTheDayAsync().await()
    }

    suspend fun removePreviousDay(previousDay: String) = withContext(Dispatchers.IO) {
        asteroidDatabase.asteroidDao.deletePreviousDay(previousDay)
    }
}