package com.app.navtask.ui.dao

import com.app.navtask.ui.model.Resp
import com.app.navtask.ui.model.WeatherResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    fun getWeather(
        @Query("latitude") latitude:String,
        @Query("longitude") longitude:String,
        @Query("daily") daily:String,
        @Query("start_date") startDate:String,
        @Query("end_date") endDate:String
//        @FieldMap params: Map<String, String>
    ): Call<WeatherResponse?>

    @GET("random")
    fun getDog(): Call<Resp?>

    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/v1/"
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherService::class.java)
        }
    }
}