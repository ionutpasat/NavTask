package com.app.navtask.ui.composables.tabs

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MapScreen(taskVm : TaskViewModel, taskId : String? = null) {
    val context = LocalContext.current
    val romania = LatLng(44.42666, 26.10243)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(romania, 10f)
    }
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var coordinates: LatLng by remember { mutableStateOf(LatLng(44.42666, 26.10243)) }
    var taskLocation by remember { mutableStateOf<Task?>(null) }

    LaunchedEffect(key1 = taskVm) {
        taskLocation = taskVm.getTaskById(taskId?.toInt() ?: 0)
    }

    LaunchedEffect(taskLocation) {
        coordinates = fetchCoordinatesFromAddress(context, taskLocation?.location ?: "") ?: romania
        cameraPositionState.position = CameraPosition.fromLatLngZoom(coordinates, 12f)
    }

    // Geocode address string asynchronously using a listener
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings
        ){
            Marker(
                state = MarkerState(position = coordinates),
                title = "Romania",
                snippet = "Marker in Romania"
            )
        }
        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
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