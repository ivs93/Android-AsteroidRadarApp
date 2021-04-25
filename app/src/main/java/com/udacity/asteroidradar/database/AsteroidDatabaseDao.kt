package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao{
    @Query("select * from asteroiddatabasemodel order by closeApproachDate ")
    fun getAsteroids(): LiveData<List<AsteroidDatabaseModel>>

    @Query("select * from asteroiddatabasemodel where closeApproachDate = date('now') order by closeApproachDate")
    fun getTodayAsteroids(): LiveData<List<AsteroidDatabaseModel>>

    @Query("select * from asteroiddatabasemodel where closeApproachDate >= date('now') order by closeApproachDate")
    fun getWeekAsteroids(): LiveData<List<AsteroidDatabaseModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidDatabaseModel)

}