package com.app.navtask.ui.composables.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    var title by remember { mutableStateOf("") }
    var initialTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var initialDescription by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var initialLocation by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("1") }
    var initialPriority by remember { mutableStateOf("1") }
    var date by remember { mutableStateOf("") }
    var initialDate by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    LaunchedEffect(key1 = taskVm) {
        task = taskVm.getTaskById(taskId?.toInt() ?: 0)
        task?.let {
            title = it.title
            initialTitle = it.title
            description = it.description
            initialDescription = it.description
            location = it.location
            initialLocation = it.location
            priority = it.priority.toString()
            initialPriority = it.priority.toString()
            date = it.date
            initialDate = it.date
        }
    }

    val isSaveEnabled = title != initialTitle || description != initialDescription ||
            location != initialLocation || priority != initialPriority || date != initialDate

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
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier.fillMaxWidth()
                        )

                        SectionTitle(title = "Description")
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth()
                        )

                        SectionTitle(title = "Location")
                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            modifier = Modifier.fillMaxWidth()
                        )

                        SectionTitle(title = "Priority")
                        ReadonlyTextField(
                            value = TextFieldValue(
                                text = when (priority) {
                                    "1" -> "Low"
                                    "2" -> "Medium"
                                    "3" -> "High"
                                    else -> "Low"
                                }
                            ),
                            onValueChange = { priority = it.text },
                            modifier = Modifier.width(300.dp),
                            onClick = {
                                showDialog = true
                            },
                            label = {
                                Text(text = "Priority")
                            }
                        )

                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center
                        ) {
                            DropdownMenu(
                                modifier = Modifier.width(300.dp),
                                expanded = showDialog,
                                onDismissRequest = { showDialog = false },
                            ) {
                                DropdownMenuItem(
                                    { Text("Low", color = Color.Green) },
                                    onClick = {
                                        showDialog = false
                                        priority = "1"
                                    })
                                DropdownMenuItem(
                                    { Text("Medium", color = Color(0xFFffcc00)) },
                                    onClick = {
                                        showDialog = false
                                        priority = "2"
                                    })
                                DropdownMenuItem(
                                    { Text("High", color = Color.Red) },
                                    onClick = {
                                        showDialog = false
                                        priority = "3"
                                    })
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitle(title = "Date")
                        TextField(
                            value = date,
                            onValueChange = { date = it },
                            modifier = Modifier.width(300.dp),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Filled.DateRange, contentDescription = "Select Date")
                                }
                            }
                        )

                        if (showDatePicker) {
                            DatePickerDialog(
                                onDismissRequest = {
                                    showDatePicker = false
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            showDatePicker = false
                                            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                                Calendar.getInstance().apply {
                                                    timeInMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                                                }.time
                                            )
                                        }
                                    ) { Text("OK") }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDatePicker = false }) {
                                        Text("Cancel")
                                    }
                                },
                            ) {
                                DatePicker(state = datePickerState)
                            }
                        }

                        SectionTitle(title = "Weather Forecast")
                        Text(
                            text = if (!temp.isNullOrEmpty()) "$temp°C" else "Loading...",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { onMapButtonClicked(taskId ?: "0") },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text(text = "Go to Map")
                            }
                            Button(
                                onClick = {
                                    taskVm.updateTask(
                                        id = it.id,
                                        title = title,
                                        description = description,
                                        location = location,
                                        priority = priority.toIntOrNull() ?: 1,
                                        date = date
                                    )
                                    navController.popBackStack()
                                },
                                modifier = Modifier.padding(top = 16.dp),
                                enabled = isSaveEnabled // Enable button only when fields are modified
                            ) {
                                Text(text = "Save Changes")
                            }
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