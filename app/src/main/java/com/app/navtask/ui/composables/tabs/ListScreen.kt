package com.app.navtask.ui.composables.tabs

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.navtask.R
import com.app.navtask.ui.dao.WeatherService
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.model.WeatherResponse
import com.app.navtask.ui.theme.md_theme_light_error
import com.app.navtask.ui.theme.typography
import com.app.navtask.ui.viewmodel.FbViewModel
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.app.navtask.ui.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    userVm: UserViewModel,
    fbVm: FbViewModel,
    onTaskButtonClicked: (taskId: String, temp: String, precip: String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var taskList by remember { mutableStateOf<List<Task>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            taskList = taskVm.getAllTasks()
        }
    }

    if (taskList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "You have no tasks yet! \nClick the + button in the Home section to add one",
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
    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 80.dp) // Adjust the value as needed
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
                    latitude = todo.latitude.toString(),
                    longitude = todo.longitude.toString(),
                    date = todo.date,
                    type = todo.type,
                    onTaskButtonClicked,
                    taskVm,
                    userVm,
                    fbVm,
                    onTaskCompleted = {
                        coroutineScope.launch {
                            taskList = taskVm.getAllTasks()
                        }
                    }
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
    latitude: String,
    longitude: String,
    date: String,
    type: String,
    onButtonClicked: (taskId: String, temp: String, precip: String) -> Unit,
    taskVm: TaskViewModel,
    userVm: UserViewModel,
    fbVm: FbViewModel,
    onTaskCompleted: () -> Unit
) {
    var temp by remember { mutableStateOf("Loading...") }
    var precip by remember { mutableStateOf("Loading...") }
    var offset by remember { mutableFloatStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val email = fbVm.getSignedInUser()?.email ?: "default@email.com"
    val density = LocalDensity.current.density
    val context = LocalContext.current
    val swipeThreshold = 400f
    val sensitivityFactor = 3f
    val showCompletionDialog = remember { mutableStateOf(false) }
    var taskType by remember { mutableStateOf(type) }

    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            delay(300)
            showCompletionDialog.value = true
            dismissRight = false
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            delay(300)
            showCompletionDialog.value = true
            dismissLeft = false
        }
    }

    if (showCompletionDialog.value) {
        AlertDialog(
            onDismissRequest = { showCompletionDialog.value = false },
            title = { Text("Task Completion") },
            text = { Text("Was this task completed?", modifier = Modifier.fillMaxWidth()) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTaskCompleted()
                        taskVm.deleteTaskById(id.toInt())
                        userVm.incrementTasksCompleted(email)
                        showCompletionDialog.value = false
                        Toast.makeText(context, "Task marked as completed!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            onTaskCompleted()
                            taskVm.deleteTaskById(id.toInt())
                            userVm.decrementTasksInProgress(email)
                            showCompletionDialog.value = false
                            Toast.makeText(context, "Task marked as not completed!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("No")
                    }
                    TextButton(
                        onClick = { showCompletionDialog.value = false }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }

    LaunchedEffect(temp) {
        val call = WeatherService.instance.getWeather(latitude, longitude, "temperature_2m_max,precipitation_probability_max", date, date)
        call.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(call: Call<WeatherResponse?>, response: Response<WeatherResponse?>) {
                try {
                    if (response.isSuccessful) {
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (offset > swipeThreshold || offset < -swipeThreshold) {
                    Color.Red
                } else {
                    Color.Transparent
                }
            )
    ) {
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
                    alpha = 10f - animateFloatAsState(
                        if (dismissRight) 1f else 0f,
                        label = ""
                    ).value,
                )
                .fillMaxWidth() // Ensure all cards have the same width
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .clickable(onClick = {
                    onButtonClicked(id, temp, precip)
                })
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp))
            {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1, // Limit to 1 line
                    overflow = TextOverflow.Ellipsis // Show ellipsis if text is too long,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Dropdown menu for icon selection
                    Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(
                                imageVector = when (taskType) {
                                    "Work" -> Icons.Default.Work
                                    "Academic" -> Icons.Default.School
                                    "Personal" -> Icons.Default.Person
                                    "Social" -> Icons.Default.Group
                                    else -> Icons.Default.Work
                                },
                                contentDescription = "Select Icon",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Work") },
                                onClick = {
                                    taskVm.updateTaskType(id.toInt(), "Work")
                                    taskType = "Work"
                                    onTaskCompleted()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Work,
                                        contentDescription = "Work Icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Academic") },
                                onClick = {
                                    taskVm.updateTaskType(id.toInt(), "Academic")
                                    taskType = "Academic"
                                    onTaskCompleted()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "School Icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Personal") },
                                onClick = {
                                    taskVm.updateTaskType(id.toInt(), "Personal")
                                    taskType = "Personal"
                                    onTaskCompleted()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Person Icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Social") },
                                onClick = {
                                    taskVm.updateTaskType(id.toInt(), "Social")
                                    taskType = "Social"
                                    onTaskCompleted()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = "Group Icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Title and Description
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2, // Limit to 2 lines
                            overflow = TextOverflow.Ellipsis // Show ellipsis if text is too long
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (temp.isEmpty() || precip.isEmpty()) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "$temp°C",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
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
    }
}