package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.api.getFormattedDateTheNextDay
import com.udacity.asteroidradar.api.getFormattedDateToday
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroidsAsync(
        @Query("start_date") startDate: String = getFormattedDateToday(),
        @Query("api_key") apiKey: String = API_KEY,
    ): Deferred<ResponseBody>

    @GET("neo/rest/v1/feed")
    fun getAsteroidsNextSevenDaysAsync(
        @Query("start_date") startDate: String = getFormattedDateTheNextDay(),
        @Query("api_key") apiKey: String = API_KEY,
    ): Deferred<ResponseBody>

    @GET("planetary/apod")
    fun getPictureOfTheDayAsync(
        @Query("api_key") apiKey: String = API_KEY,
    ): Deferred<PictureOfDay>
}

object AsteroidApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}