package com.app.navtask.ui.composables.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    taskVm: TaskViewModel,
    taskId: String? = null,
    temp: String? = null,
    navController: NavHostController,
    onMapButtonClicked: (taskId: String) -> Unit
) {
    var task by remember { mutableStateOf<Task?>(null) }

    LaunchedEffect(key1 = taskVm) {
        task = taskVm.getTaskById(taskId?.toInt() ?: 0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            task?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        SectionTitle(title = "Title")
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)

                        SectionTitle(title = "Description")
                        Text(
                            text = it.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)

                        SectionTitle(title = "Location")
                        Text(
                            text = it.location,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)

                        SectionTitle(title = "Priority")
                        Text(
                            text = when (it.priority) {
                                1 -> "Low"
                                2 -> "Medium"
                                3 -> "High"
                                else -> "Low"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp),
                            color = when (it.priority) {
                                1 -> Color.Green
                                2 -> Color.Yellow
                                3 -> Color.Red
                                else -> Color.Green
                            }
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)

                        SectionTitle(title = "Date")
                        Text(
                            text = it.date,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)

                        SectionTitle(title = "Weather Forecast")
                        Text(
                            text = temp ?: "Loading...",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)

                        Button(
                            onClick = { onMapButtonClicked(taskId ?: "0") },
                            modifier = Modifier.padding(top = 16.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Go to Map")
                        }
                    }
                }
            } ?: run {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}