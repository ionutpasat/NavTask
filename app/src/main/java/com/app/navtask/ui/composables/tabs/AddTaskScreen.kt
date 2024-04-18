package com.app.navtask.ui.composables.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.theme.typography
import com.app.navtask.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskVm: TaskViewModel,
    onAddButtonClicked: () -> Unit)
{
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("0") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = "Add Task", style = TextStyle(
            fontSize = 40.sp,
            fontFamily = FontFamily.Cursive,
            color = Color(0xFF001F26)
        )
        )

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

        ReadonlyTextField(
            value = TextFieldValue(
                text = when(priority) {
                    "1" -> "Low"
                    "2" -> "Medium"
                    "3" -> "High"
                    else -> "Low"
                }
            ),
            onValueChange = { priority = it.text},
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
                    { Text("Low") },
                    onClick = {
                        showDialog = false
                        priority = "1"
                    })
                DropdownMenuItem(
                    { Text("Medium") },
                    onClick = {
                        showDialog = false
                        priority = "2"
                    })
                DropdownMenuItem(
                    { Text("High") },
                    onClick = {
                        showDialog = false
                        priority = "3"
                    })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReadonlyTextField(
            value = TextFieldValue(text = date),
            onValueChange = { date = it.text},
            onClick = {
                showDatePicker = true
            },
            label = {
                Text(text = "Date")
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
                            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).
                            format(Calendar.getInstance().apply {
                                timeInMillis = datePickerState.selectedDateMillis!!
                            }.time)
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) { Text("Cancel") }
                },
            )
            {
                DatePicker(state = datePickerState)
            }
        }

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

@Composable
fun ReadonlyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {

    Box {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = label
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onClick),
        )
    }
}