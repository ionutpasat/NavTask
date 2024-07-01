package com.app.navtask

import com.app.navtask.ui.dao.WeatherService
import com.app.navtask.ui.model.Daily
import com.app.navtask.ui.model.WeatherResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var service: WeatherService

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        service = Retrofit.Builder()
            .baseUrl(server.url("https://api.open-meteo.com/v1/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun testGetWeather() = runBlocking {
        val weatherResp = WeatherResponse(
            daily = Daily(
                temperature_2m_max = listOf(1.0, 2.0, 3.0),
                precipitation_probability_max = listOf(10, 20, 30)
            )
        )

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(weatherResp.toString())
        server.enqueue(mockResponse)

        val response = service.getWeather(44.4375.toString(), 26.125.toString(),"temperature_2m_max", "2024-05-01", "2024-05-01").execute()

        assertTrue(response.isSuccessful)
        assertTrue(response.body()!!.daily.temperature_2m_max.isNotEmpty())
    }
}