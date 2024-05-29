package com.app.navtask.ui.model

data class WeatherResponse(
    val daily: Daily
)

data class Daily(
    val temperature_2m_max: List<Double>
)