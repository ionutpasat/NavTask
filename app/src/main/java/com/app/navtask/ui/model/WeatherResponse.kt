package com.app.navtask.ui.model

import androidx.compose.runtime.MutableState

data class WeatherResponse(
    val daily: Daily
)

data class Daily(
    val temperature_2m_max: List<Double>
)