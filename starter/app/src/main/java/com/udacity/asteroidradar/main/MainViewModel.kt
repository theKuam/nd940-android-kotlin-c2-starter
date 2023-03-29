package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.getFormattedDateNextSevenDay
import com.udacity.asteroidradar.api.getFormattedDateToday
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : ViewModel() {

    private val asteroidDatabase = AsteroidDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(asteroidDatabase)

    private val _apod = MutableLiveData<PictureOfDay>()
    val apod: LiveData<PictureOfDay>
        get() = _apod

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    init {
        viewModelScope.launch {
            try {
                asteroidRepository.fetchAsteroids()
                _apod.value = asteroidRepository.getPictureOfTheDay()
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }

    fun getWeekAsteroids() {
        viewModelScope.launch {
            _asteroids.value = asteroidRepository.getWeekAsteroids(
                getFormattedDateToday(),
                getFormattedDateNextSevenDay()
            )
        }
    }

    fun getTodayAsteroids() {
        viewModelScope.launch {
            _asteroids.value = asteroidRepository.getTodayAsteroids(
                getFormattedDateToday()
            )
        }
    }

    fun getSavedAsteroids() {
        viewModelScope.launch {
            _asteroids.value =
                asteroidRepository.getSavedAsteroids()
        }
    }
}