package com.app.navtask.ui.dao

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse?>

    companion object {
        private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

        val api: LocationService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocationService::class.java)
        }
    }
}

// Data classes for the Directions API response
data class DirectionsResponse(val routes: List<Route>)
data class Route(val overview_polyline: Polyline)
data class Polyline(val points: String)