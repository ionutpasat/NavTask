package com.app.navtask.ui.composables.tabs

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.app.navtask.R
import com.app.navtask.ui.dao.WeatherService
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.model.WeatherResponse
import com.app.navtask.ui.theme.md_theme_light_error
import com.app.navtask.ui.theme.typography
import com.app.navtask.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


/**
 * Composable function that represents the list screen of the application.
 */
@Composable
fun ListScreen(
    taskVm : TaskViewModel,
//    weatherVm : WeatherViewModel,
    onMapButtonClicked: (taskId: String) -> Unit
) {
    var taskList by remember { mutableStateOf<List<Task>>(emptyList()) }

    LaunchedEffect(key1 = taskVm) {
        taskList = taskVm.getAllTasks()
    }

    if (taskList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "You have no tasks yet! Click the + button in the Home section to add one",
                style = typography.titleLarge,
                color = md_theme_light_error
            )
        }
    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Adjust the value as needed
        ) {
            items(taskList) { todo ->
                TodoItem(
                    id = todo.id.toString(),
                    title = todo.title,
                    description = todo.description,
                    priority = when (todo.priority) {
                        1 -> Priority.LOW
                        2 -> Priority.MEDIUM
                        3 -> Priority.HIGH
                        else -> Priority.LOW
                    },
                    location = todo.location,
                    date = todo.date,
                    onMapButtonClicked
                )
            }
        }
    }
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

@Composable
fun TodoItem(
    id: String,
    title: String,
    description: String,
    priority: Priority,
    location: String,
    date: String,
    onButtonClicked: (taskId: String) -> Unit)
//    onSwipeAway: (taskId: String) -> Unit
{
    var temp by remember { mutableStateOf("") }
    var offset by remember { mutableStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    val context = LocalContext.current
    val swipeThreshold = 400f
    val sensitivityFactor = 3f

    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            delay(300)
            Toast.makeText(context, "Swipe right  works", Toast.LENGTH_SHORT).show()
            println("Swipe right works")
            dismissRight = false
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            delay(300)
            Toast.makeText(context, "Swipe left works", Toast.LENGTH_SHORT).show()
            println("Swipe left works")
            dismissLeft = false
        }
    }

    LaunchedEffect(temp) {
        val call = WeatherService.instance.getWeather(44.4375.toString(), 26.125.toString(),"temperature_2m_max", date, date)
        call.enqueue(object: Callback<WeatherResponse?> {
            override fun onResponse(call: Call<WeatherResponse?>, response: Response<WeatherResponse?>) {
                try {
                    if (response.isSuccessful) {
                        temp = response.body()?.daily?.temperature_2m_max?.get(0).toString()
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

    Card(
        modifier = Modifier
            .offset { IntOffset(offset.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onDragEnd = {
                    offset = 0f
                }) { change, dragAmount ->

                    offset += (dragAmount / density) * sensitivityFactor
                    when {
                        offset > swipeThreshold -> {
                            dismissRight = true
                        }

                        offset < -swipeThreshold -> {
                            dismissLeft = true
                        }
                    }
                    if (change.positionChange() != Offset.Zero) change.consume()
                }
            }
            .graphicsLayer(
                alpha = 10f - animateFloatAsState(if (dismissRight) 1f else 0f).value,
//                rotationZ = animateFloatAsState(offset / 50).value
            )
            .width(405.dp)
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable(onClick = {
                onButtonClicked(id)
            }),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFC4C8BB),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Round image on the left
            Image(
                painter = painterResource(id = R.drawable.anonymous),
                contentDescription = "Todo Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = MaterialTheme.shapes.small)
                    .clip(RectangleShape)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Title and Description
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = typography.headlineSmall
                )
                Text(
                    text = description,
                    style = typography.bodyMedium
                )
            }

            Column (
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = date,
                    style = typography.bodyMedium
                )
                Text(
                    text = "$temp°C",
                    style = typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(64.dp))

            // Colored circle based on priority
            Canvas(
                modifier = Modifier
                    .size(30.dp)
                    .padding(top = 12.dp)
                    .align(Alignment.CenterVertically)
            ) {
                drawIntoCanvas {
                    val circleColor = when (priority) {
                        Priority.LOW -> Color.Green
                        Priority.MEDIUM -> Color(0xFFffcc00) // Orange
                        Priority.HIGH -> Color.Red
                    }
                    it.drawCircle(
                        radius = size.minDimension / 1.2f,
                        paint = Paint().apply {
                            color = circleColor
                            style = PaintingStyle.Fill
                        },
                        center = Offset.Zero
                    )
                }
            }
        }
    }
}

//fun getWeather(lat : Double, lon : Double, date : String) : String {
//    val temp = mutableStateOf("")
//
//    val call = WeatherService.insta.getWeather(lat.toString(), lon.toString(), "temperature_2m", date, date)
//
//    val map = mapOf(
//        "latitude" to lat.toString(),
//        "longitude" to lon.toString(),
//        "daily" to "temperature_2m_max",
//        "start_date" to date,
//        "end_date" to date
//    )
//
////    val call: Call<WeatherResponse?> = api.getWeather(lat, lon, "temperature_2m_max", date, date)
////    val call: Response<Resp?> = api.getDog()
//    call.enqueue(object: Callback<WeatherResponse?> {
//        override fun onResponse(call: Call<WeatherResponse?>, response: Response<WeatherResponse?>) {
//            try {
//                if (response.isSuccessful) {
//                    Log.d("Main", "TEREMAPULA!" + response.body().toString())
//                    temp.value = response.body()?.current?.temperature_2m.toString()
//                }
//            } catch (e: Exception) {
//                Log.e("Main", "Failed mate " + e.message.toString())
//            }
//        }
//
//        override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
//            Log.e("Main", "Failed mate " + t.message.toString())
//        }
//    })
//
//    return temp.value
//}