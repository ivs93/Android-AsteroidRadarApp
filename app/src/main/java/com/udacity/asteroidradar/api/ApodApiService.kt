package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = " https://api.nasa.gov/"
private const val API_KEY = "YOUR_API_KEY_HERE"

private  val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(1, TimeUnit.MINUTES)
    .readTimeout(1, TimeUnit.MINUTES)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitAPOD = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

private val retrofitAsteroids = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface ApodApiService{
    @GET("planetary/apod")
    suspend fun getAPOD(@Query("api_key") apiKey: String = API_KEY): PictureOfDay
}

interface AsteroidsApiService{
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("start_date") startDate: String, @Query("api_key") apiKey: String = API_KEY): String
}

object ApodApi {
    val retrofitService : ApodApiService by lazy { retrofitAPOD.create(ApodApiService::class.java) }
}

object AsteroidsApi{
    val retrofitService : AsteroidsApiService by lazy { retrofitAsteroids.create(AsteroidsApiService::class.java) }
}