package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.AsteroidsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<AsteroidDomainModel>> =
        Transformations.map(database.asteroidDatabaseDao.getAsteroids()){
            it.asDomainModel()
        }

    val todayAsteroids: LiveData<List<AsteroidDomainModel>> =
    Transformations.map(database.asteroidDatabaseDao.getTodayAsteroids()){
        it.asDomainModel()
    }

    val weekAsteroids: LiveData<List<AsteroidDomainModel>> =
        Transformations.map(database.asteroidDatabaseDao.getWeekAsteroids()){
            it.asDomainModel()
        }

    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            try{
                val asteroidsJSON = AsteroidsApi.retrofitService.getAsteroids(getDate())
                val asteroidsArrayList = parseAsteroidsJsonResult(JSONObject(asteroidsJSON))
                database.asteroidDatabaseDao.insertAll(*NetworkAsteroidContainer(asteroidsArrayList).asDatabaseModel())
            }catch (e: Exception){}
        }
    }

    private fun getDate() : String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return dateFormat.format(calendar.timeInMillis)
    }
}