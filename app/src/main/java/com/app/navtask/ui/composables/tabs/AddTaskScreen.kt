package com.app.navtask.ui.composables.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.theme.typography
import com.app.navtask.ui.viewmodel.TaskViewModel

@Composable
fun AddTaskScreen(taskVm: TaskViewModel,
                  onAddButtonClicked: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp))
    {
        Text(text = "Add Task", style = typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = priority,
            onValueChange = { priority = it },
            label = { Text("Priority") },
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val task = Task(0, title, description, priority.toInt(), location, date)
                taskVm.addTask(task)
                onAddButtonClicked()
            }
        ) {
            Text("Add Task")
        }
    }
}