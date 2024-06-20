package com.app.navtask.ui.composables.tabs

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.app.navtask.ui.components.ReminderReceiver
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskVm: TaskViewModel,
    onAddButtonClicked: () -> Unit,
    navController: NavHostController
)
{
    val context = LocalContext.current
    var scrollState = rememberScrollState()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("0") }
    var streetAndNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var coordinates: LatLng by remember { mutableStateOf(LatLng(44.42666, 26.10243)) }

    LaunchedEffect(streetAndNumber, city, country) {
        coordinates = fetchCoordinatesFromAddress(context, "$streetAndNumber, $city, $country") ?: LatLng(44.42666, 26.10243)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Add Task", style = TextStyle(
            fontSize = 40.sp,
            fontFamily = FontFamily.Cursive,
            color = MaterialTheme.colorScheme.primary
        )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            singleLine = true,
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = streetAndNumber,
            onValueChange = { streetAndNumber = it },
            label = { Text("Street & No.") },
            singleLine = true,
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            singleLine = true,
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            singleLine = true,
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReadonlyTextField(
            value = TextFieldValue(
                text = when(priority) {
                    "1" -> "Low"
                    "2" -> "Medium"
                    "3" -> "High"
                    else -> "Low"
                },
            ),
            onValueChange = { priority = it.text},
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

        ReadonlyTextField(
            value = TextFieldValue(text = date),
            onValueChange = { date = it.text},
            modifier = Modifier.width(300.dp),
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
                                timeInMillis = if (datePickerState.selectedDateMillis != null)
                                    datePickerState.selectedDateMillis!!
                                else
                                    System.currentTimeMillis()
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
                val fullLocation = "$streetAndNumber, $city, $country"
                val task = Task(0, title, description, priority.toInt(),
                    fullLocation, coordinates.latitude, coordinates.longitude, date)
                taskVm.addTask(task)
                scheduleNotification(context, datePickerState, title)
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
            label = label,
            textStyle = TextStyle(color = when(value.text) {
                "Low" -> Color.Green
                "Medium" -> Color(0xFFffcc00)
                "High" -> Color.Red
                else -> Color.Black
            }),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onClick),
        )
    }
}

suspend fun fetchCoordinatesFromAddress(context: Context, address: String, maxResults: Int = 1): LatLng? {
    return withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context)
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocationName(address, maxResults)
            if (addresses?.isNotEmpty() == true) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                LatLng(latitude, longitude)
            } else {
                null // Address not found
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null // Error occurred
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
fun scheduleNotification(
    context: Context,
    datePickerState: DatePickerState,
    title: String
) {
    val intent = Intent(context.applicationContext, ReminderReceiver::class.java)
    intent.putExtra("title", title)
    val pendingIntent = PendingIntent.getBroadcast(
        context.applicationContext,
        1,
        intent,
        PendingIntent.FLAG_MUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val selectedDate = Calendar.getInstance().apply {
        timeInMillis = if (datePickerState.selectedDateMillis != null)
            datePickerState.selectedDateMillis!!
        else
            System.currentTimeMillis()
    }

    val year = selectedDate.get(Calendar.YEAR)
    val month = selectedDate.get(Calendar.MONTH)
    val day = selectedDate.get(Calendar.DAY_OF_MONTH)

    val calendar = Calendar.getInstance()
    calendar.set(year, month, day, 0, 53)

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )

    Toast.makeText(context, "Reminder set!", Toast.LENGTH_SHORT).show()
}