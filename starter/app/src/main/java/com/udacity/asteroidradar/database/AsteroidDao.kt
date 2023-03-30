package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Query("select * from $ASTEROID_TABLE order by closeApproachDate")
    fun getAsteroids(): List<AsteroidEntity>

    @Query("select * from $ASTEROID_TABLE where closeApproachDate between :today and :nextSevenDay order by closeApproachDate")
    fun getWeekAsteroids(today: String, nextSevenDay: String): List<AsteroidEntity>

    @Query("select * from $ASTEROID_TABLE where closeApproachDate = :today order by closeApproachDate")
    fun getTodayAsteroids(today: String): List<AsteroidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("delete from $ASTEROID_TABLE where closeApproachDate = :previousDay")
    fun deletePreviousDay(previousDay: String)
}