package com.app.navtask.ui.composables.tabs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.navtask.ui.dao.WeatherService
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.model.WeatherResponse
import com.app.navtask.ui.theme.md_theme_dark_primary
import com.app.navtask.ui.theme.md_theme_dark_secondary
import com.app.navtask.ui.theme.md_theme_light_error
import com.app.navtask.ui.theme.typography
import com.app.navtask.ui.viewmodel.FbViewModel
import com.app.navtask.ui.viewmodel.TaskViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Composable function that represents the home screen of the application.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    taskVm: TaskViewModel,
    fbVm: FbViewModel,
    onAddTaskButtonClicked: () -> Unit
) {
    var task by remember { mutableStateOf<Task?>(null) }
    val email = fbVm.getSignedInUser()?.email ?: "default@email.com"

    LaunchedEffect(key1 = taskVm) {
        task = taskVm.getFirstTask(email)
    }

    Scaffold(
        modifier = Modifier.padding(top = 16.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 8.dp, end = 20.dp),
                title = { Text("Home", fontWeight = FontWeight.Bold) },
                actions = {
                    FloatingActionButton(
                        onClick = { onAddTaskButtonClicked() },
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Task")
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
                .padding(bottom = 64.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            task?.let {
                TaskDetailCard(task = it)
            } ?: run {
                Text(
                    modifier = Modifier.padding(top = 300.dp),
                    text = "You have no tasks yet! \nClick the + button to add one",
                    style = typography.titleLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.25f),
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TaskDetailCard(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(start = 16.dp)
            .padding(end = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            var temp by remember { mutableStateOf("") }
            var precip by remember { mutableStateOf("") }

            LaunchedEffect(temp) {
                val call = WeatherService.instance.getWeather(
                    task.latitude.toString(),
                    task.longitude.toString(),
                    "temperature_2m_max,precipitation_probability_max",
                    task.date,
                    task.date
                )
                call.enqueue(object : Callback<WeatherResponse?> {
                    override fun onResponse(call: Call<WeatherResponse?>, response: Response<WeatherResponse?>) {
                        try {
                            if (response.isSuccessful) {
                                println("Weather response: ${response.body()}")
                                temp = response.body()?.daily?.temperature_2m_max?.get(0).toString()
                                precip = response.body()?.daily?.precipitation_probability_max?.get(0).toString()
                            }
                        } catch (e: Exception) {
                            Log.e("Main", "Failed mate " + e.message.toString())
                        }
                    }

                    override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                        Log.e("Main", "Failed mate " + t.message.toString())
                    }
                })
            }

            Text(
                text = "Focus Task",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)

            SectionTitle(title = "Title")
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider(color = Color.DarkGray, thickness = 1.dp)
            SectionTitle(title = "Description")
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider(color = Color.DarkGray, thickness = 1.dp)
            SectionTitle(title = "Location")
            Text(
                text = task.location,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider(color = Color.DarkGray, thickness = 1.dp)
            SectionTitle(title = "Priority")
            Text(
                text = when (task.priority) {
                    1 -> "Low"
                    2 -> "Medium"
                    3 -> "High"
                    else -> "Low"
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                color = when (task.priority) {
                    1 -> Color.Green
                    2 -> Color.Yellow
                    3 -> Color.Red
                    else -> Color.Green
                }
            )
            Divider(color = Color.DarkGray, thickness = 1.dp)
            SectionTitle(title = "Date")
            Text(
                text = task.date,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider(color = Color.DarkGray, thickness = 1.dp)
            SectionTitle(title = "Weather Forecast")
            Row {
                Text(
                    text = if (temp.isNotEmpty()) "$temp°C" else "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = if (precip.isNotEmpty()) " | $precip% chance of rain" else " | Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}