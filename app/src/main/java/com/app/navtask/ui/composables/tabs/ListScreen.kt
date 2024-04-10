package com.app.navtask.ui.composables.tabs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.navtask.R
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.theme.typography
import com.app.navtask.ui.viewmodel.TaskViewModel

/**
 * Composable function that represents the list screen of the application.
 */
@Composable
fun ListScreen(
    taskVm : TaskViewModel,
    onMapButtonClicked: (taskId: String) -> Unit
) {
    var taskList by remember { mutableStateOf<List<Task>>(emptyList()) }

    LaunchedEffect(key1 = taskVm) {
        taskList = taskVm.getAllTasks()
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp) // Adjust the value as needed
    ){
        items(taskList) { todo ->
            TodoItem(
                id = todo.id.toString(),
                title = todo.title,
                description = todo.description,
                priority = when(todo.priority) {
                    1 -> Priority.LOW
                    2 -> Priority.MEDIUM
                    3 -> Priority.HIGH
                    else -> Priority.LOW
                },
                location = todo.location,
                date = todo.date,
                onMapButtonClicked)
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
    onButtonClicked: (taskId: String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = {
                onButtonClicked(id)
            }),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Round image on the left
            Image(
                painter = painterResource(id = R.drawable.round_image_placeholder),
                contentDescription = "Todo Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = MaterialTheme.shapes.small)
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

            Spacer(modifier = Modifier.width(16.dp))

            // Colored circle based on priority
            Canvas(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            ) {
                drawIntoCanvas {
                    val circleColor = when (priority) {
                        Priority.LOW -> Color.Green
                        Priority.MEDIUM -> Color(0xFFffcc00) // Orange
                        Priority.HIGH -> Color.Red
                    }
                    it.drawCircle(
//                        color = circleColor,
                        radius = size.minDimension / 2,
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