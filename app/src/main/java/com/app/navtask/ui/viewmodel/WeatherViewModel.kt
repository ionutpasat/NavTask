//package com.app.navtask.ui.viewmodel
//
//import android.util.Log
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.app.navtask.ui.dao.WeatherService
//import com.app.navtask.ui.model.Daily
//import com.app.navtask.ui.model.WeatherResponse
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Inject
//
//@HiltViewModel
//class WeatherViewModel @Inject constructor(private val weatherService: WeatherService) : ViewModel() {
//    private var temp = mutableStateOf("")
//
//    suspend fun getWeather(lat : Double, lon : Double, date : String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.open-meteo.com/v1/forecast?")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val api = retrofit.create(WeatherService::class.java)
//
//        val call: Call<WeatherResponse?> = api.getWeather(lat, lon, "temperature_2m_max", date, date)
//
//        call.enqueue(object: Callback<WeatherResponse?> {
//            override fun onResponse(call: Call<WeatherResponse?>, response: Response<WeatherResponse?>) {
//                if(response.isSuccessful) {
//                    Log.d("Main", "success!" + response.body().toString())
//                    temp.value = response.body()!!.daily.temperature_2m_max.toString()
//                }
//            }
//
//            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
//                Log.e("Main", "Failed mate " + t.message.toString())
//            }
//        })
//    }
//}