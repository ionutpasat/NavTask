 package com.app.navtask.ui.composables.tabs

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.navtask.R
import com.app.navtask.ui.dao.WeatherService
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.model.WeatherResponse
import com.app.navtask.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 /**
 * Composable function that represents the search screen of the application.
 */
 @Composable
 fun SearchScreen(taskVm: TaskViewModel = viewModel(),
                  onTaskButtonClicked: (taskId: String, temp: String, precip: String) -> Unit) {
     var searchQuery by remember { mutableStateOf("") }
     var searchResults by remember { mutableStateOf(listOf<Task>()) }
     var sortOption by remember { mutableStateOf("Date Ascending") }
     val coroutineScope = rememberCoroutineScope()
     val focusManager = LocalFocusManager.current
     val keyboardController = LocalSoftwareKeyboardController.current

     LaunchedEffect(Unit) {
         keyboardController?.show()
     }

     Column(modifier = Modifier
         .fillMaxSize()
         .padding(16.dp)) {

         Row(
             verticalAlignment = Alignment.CenterVertically,
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(8.dp)
         ) {
             TextField(
                 value = searchQuery,
                 onValueChange = {
                     searchQuery = it
                     coroutineScope.launch {
                         searchResults = taskVm.searchTasks("%$searchQuery%")
                         searchResults = sortTasks(searchResults, sortOption)
                     }
                 },
                 label = { Text(text = stringResource(id = R.string.search)) },
                 leadingIcon = {
                     IconButton(onClick = {
                         focusManager.clearFocus()
                         keyboardController?.show()
                     }) {
                         Icon(Icons.Default.Search, contentDescription = null)
                     }
                 },
                 keyboardOptions = KeyboardOptions(
                     imeAction = ImeAction.Search
                 ),
                 keyboardActions = KeyboardActions(
                     onSearch = {
                         keyboardController?.hide()
                         focusManager.clearFocus()
                     }
                 ),
                 singleLine = true,
                 modifier = Modifier
                     .weight(1f)
                     .onFocusChanged { focusState ->
                         if (focusState.isFocused) {
                             keyboardController?.show()
                         }
                     }
             )
         }


         Spacer(modifier = Modifier.height(16.dp))

         Row(
             modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
         ) {
             SortMenu(
                 selectedOption = sortOption,
                 onOptionSelected = { option ->
                     sortOption = option
                     coroutineScope.launch {
                         searchResults = taskVm.searchTasks("%$searchQuery%")
                         searchResults = sortTasks(searchResults, sortOption)
                     }
                 }
             )
         }

         Spacer(modifier = Modifier.height(16.dp))

         if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
             Text(
                 text = "No tasks found",
                 style = MaterialTheme.typography.titleMedium,
                 color = MaterialTheme.colorScheme.error,
                 modifier = Modifier.align(Alignment.CenterHorizontally)
             )
         } else {
             LazyColumn(modifier = Modifier.fillMaxSize().padding(bottom = 64.dp)) {
                 items(searchResults) { task ->
                     TaskItem(task, onTaskButtonClicked)
                 }
             }
         }
     }
 }

 @Composable
 fun SortMenu(
     selectedOption: String,
     onOptionSelected: (String) -> Unit
 ) {
     var expanded by remember { mutableStateOf(false) }
     val options = listOf("Date Ascending", "Date Descending", "Importance Ascending", "Importance Descending")

     Box {
         Button(onClick = { expanded = true }) {
             Text(text = selectedOption)
         }
         DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
             options.forEach { option ->
                 DropdownMenuItem(
                     text = { Text(option) },
                     onClick = {
                         onOptionSelected(option)
                         expanded = false
                     }
                 )
             }
         }
     }
 }

 fun sortTasks(tasks: List<Task>, sortOption: String): List<Task> {
     return when (sortOption) {
         "Date Ascending" -> tasks.sortedBy { it.date }
         "Date Descending" -> tasks.sortedByDescending { it.date }
         "Importance Ascending" -> tasks.sortedBy { it.priority }
         "Importance Descending" -> tasks.sortedByDescending { it.priority }
         else -> tasks
     }
 }

 @Composable
 fun TaskItem(task: Task,
              onTaskButtonClicked: (taskId: String, temp: String, precip: String) -> Unit) {
     var temp by remember { mutableStateOf("Loading...") }
     var precip by remember { mutableStateOf("Loading...") }

     LaunchedEffect(temp) {
         val call = WeatherService.instance.getWeather(task.latitude.toString(),
             task.longitude.toString(),"temperature_2m_max,precipitation_probability_max", task.date, task.date)
         call.enqueue(object: Callback<WeatherResponse?> {
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

     Card(
         modifier = Modifier
             .fillMaxWidth()
             .padding(vertical = 4.dp)
             .clickable(onClick = {
                 onTaskButtonClicked(task.id.toString(), temp, precip)
             }),
         elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
     ) {
         Column(modifier = Modifier.padding(16.dp)) {
             Text(text = task.title, style = MaterialTheme.typography.titleMedium,
                 maxLines = 1,
                 overflow = TextOverflow.Ellipsis
             )
             Spacer(modifier = Modifier.height(4.dp))
             Text(text = task.description, style = MaterialTheme.typography.bodyMedium,
                 maxLines = 1,
                 overflow = TextOverflow.Ellipsis
             )
             Spacer(modifier = Modifier.height(4.dp))
             Text(text = task.location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
             Spacer(modifier = Modifier.height(4.dp))
             Text(text = "Priority: ${when (task.priority) {
                 1 -> "Low"
                 2 -> "Medium"
                 3 -> "High"
                 else -> "Low"
             }}", style = MaterialTheme.typography.bodySmall, color = when (task.priority) {
                 1 -> Color.Green
                 2 -> Color.Yellow
                 3 -> Color.Red
                 else -> Color.Green
             })
             Spacer(modifier = Modifier.height(4.dp))
             Text(text = "Date: ${task.date}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
         }
     }
 }