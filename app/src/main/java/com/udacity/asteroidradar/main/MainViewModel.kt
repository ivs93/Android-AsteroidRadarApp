package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.AsteroidDomainModel
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.ApodApi
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _apod = MutableLiveData<PictureOfDay>()

    val apod: LiveData<PictureOfDay>
        get() = _apod

    private val database = getInstance(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val queryType = MutableLiveData(0)

    val asteroids = Transformations.switchMap(queryType){ type->
        when(type){
            0 -> {
               asteroidsRepository.weekAsteroids
            }
            1-> {
                asteroidsRepository.todayAsteroids
            }
            2->{
                asteroidsRepository.asteroids
            }
            else -> MutableLiveData(emptyList())
        }
    }

    init {
        getAPOD()
        refreshAsteroids()
    }

    private fun getAPOD(){
        viewModelScope.launch {
            try{
                val picture = ApodApi.retrofitService.getAPOD()
                if(picture.mediaType == "image"){
                   _apod.value = picture
                }
            }catch (e: Exception){}
        }

    }

    fun getWeekAsteroids(){
        queryType.value = 0
    }

    fun getTodayAsteroids(){
        queryType.value = 1
    }

    fun getSavedAsteroids(){
        queryType.value = 2
    }

    private fun refreshAsteroids(){
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            queryType.value = queryType.value
        }
    }


    private val _navigateToAsteroidDetail = MutableLiveData<AsteroidDomainModel>()
    val navigateToAsteroidDetail
        get() = _navigateToAsteroidDetail

    fun displayAsteroidDetails(asteroid: AsteroidDomainModel){
        _navigateToAsteroidDetail.value = asteroid
    }

    fun displayAsteroidDetailsComplete(){
        _navigateToAsteroidDetail.value = null
    }
}