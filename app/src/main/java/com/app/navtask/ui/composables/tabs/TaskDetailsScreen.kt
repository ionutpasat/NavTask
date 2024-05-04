package com.app.navtask.ui.composables.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel

@Composable
fun TaskDetailsScreen(taskVm : TaskViewModel,
                      taskId : String? = null,
                      navController : NavHostController,
                      onMapButtonClicked: (taskId: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var task by remember { mutableStateOf<Task?>(null) }

        LaunchedEffect(key1 = taskVm) {
            task = taskVm.getTaskById(taskId?.toInt() ?: 0)
        }
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
        }
        Text(text = "Title: ${task?.title}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Description: ${task?.description}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Location: ${task?.location}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Priority: ${task?.priority}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date: ${task?.date}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onMapButtonClicked(taskId ?: "0") }){
            Text(text = "Go to Map")
        }
    }
}