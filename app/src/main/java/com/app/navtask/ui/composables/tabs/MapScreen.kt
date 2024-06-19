package com.app.navtask.ui.composables.tabs

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.navtask.R
import com.app.navtask.ui.dao.DirectionsResponse
import com.app.navtask.ui.dao.LocationService
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MapScreen(
    taskVm: TaskViewModel,
    taskId: String? = null,
    navController: NavHostController,
    location: Location?,
) {
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
    var task by remember { mutableStateOf<Task?>(null) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    LaunchedEffect(key1 = taskVm) {
        task = taskVm.getTaskById(taskId?.toInt() ?: 0)
    }

    LaunchedEffect(task) {
        coordinates = LatLng(task?.latitude ?: 44.42666, task?.longitude ?: 26.10243)
        cameraPositionState.position = CameraPosition.fromLatLngZoom(coordinates, 12f)
    }

    LaunchedEffect(location) {
        location?.let {
            currentLocation = LatLng(it.latitude, it.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation!!, 12f)
        }
    }

    LaunchedEffect(currentLocation, coordinates) {
        if (currentLocation != null && task != null) {
            // Fetch directions
            val origin = "${currentLocation!!.latitude},${currentLocation!!.longitude}"
            val destination = "${coordinates.latitude},${coordinates.longitude}"

            val call = LocationService.api.getDirections(
                origin = origin,
                destination = destination,
                apiKey = "AIzaSyC7Y4f2-Ju9OpUaj1YWOXlp5infEENFfRY"
            )

            call.enqueue(object : Callback<DirectionsResponse?> {
                override fun onResponse(
                    call: Call<DirectionsResponse?>,
                    response: Response<DirectionsResponse?>
                ) {
                    val routes = response.body()?.routes
                    if (!routes.isNullOrEmpty()) {
                        val points = routes[0].overview_polyline.points
                        polylinePoints = decodePolyline(points)
                    }
                }

                override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
                    Log.e("MapScreen", "Failed to fetch directions", t)
                }
            })
            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation!!, 13f)
        }
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties.copy(isMyLocationEnabled = true),
            uiSettings = uiSettings
        ) {
            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Current Location",
                    snippet = "You are here",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            }
            Marker(
                state = MarkerState(position = coordinates),
                title = task?.title ?: "Destination",
                snippet = "Destination location"
            )
            if (polylinePoints.isNotEmpty()) {
                Polyline(
                    points = polylinePoints,
                    color = Color.Blue,
                    width = 5f
                )
            }
        }
        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            },
            modifier = Modifier.align(Alignment.TopEnd)
        )
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
        }

        IconButton(
            onClick = { openGoogleMapsDirections(context, currentLocation!!, coordinates) },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(Icons.Filled.LocationOn,
                contentDescription = "Go back",
                modifier = Modifier.size(48.dp))
        }
    }
}

fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(
            lat / 1E5,
            lng / 1E5
        )
        poly.add(p)
    }

    return poly
}

fun openGoogleMapsDirections(context: Context, origin: LatLng, destination: LatLng) {
    val uri = Uri.parse("http://maps.google.com/maps?saddr=${origin.latitude},${origin.longitude}&daddr=${destination.latitude},${destination.longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}
