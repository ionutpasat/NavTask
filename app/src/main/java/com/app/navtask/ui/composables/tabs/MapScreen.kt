package com.app.navtask.ui.composables.tabs

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.navtask.ui.dao.DirectionsResponse
import com.app.navtask.ui.dao.Leg
import com.app.navtask.ui.dao.LocationService
import com.app.navtask.ui.dao.TravelDistance
import com.app.navtask.ui.dao.TravelDuration
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var mode by remember { mutableStateOf("driving") }
    var duration by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }

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

    LaunchedEffect(currentLocation, coordinates, mode) {
        if (currentLocation != null && task != null) {
            fetchDirections(currentLocation!!, coordinates, mode, snackbarHostState, coroutineScope, onDirectionsFetched = { points, timeInfo: Leg ->
                polylinePoints = points
                duration = timeInfo.duration.text ?: ""
                distance = timeInfo.distance.text ?: ""
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

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(top = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row{
                Text("$duration -", color = MaterialTheme.colorScheme.scrim)
                Text("- $distance", color = MaterialTheme.colorScheme.scrim)
            }
            Row {
                IconButton(onClick = { mode = "walking" },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.scrim),
                    modifier = Modifier.size(64.dp)) {
                    Icon(Icons.AutoMirrored.Filled.DirectionsWalk, contentDescription = "Walk")
                }
                IconButton(onClick = { mode = "transit" },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.scrim),
                    modifier = Modifier.size(64.dp)) {
                    Icon(Icons.Default.DirectionsBus, contentDescription = "Transit")
                }
                IconButton(onClick = { mode = "driving" },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.scrim),
                    modifier = Modifier.size(64.dp)) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = "Drive")
                }
            }
        }

        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp)
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart)
                .padding(start = 48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.scrim
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
        }

        IconButton(
            onClick = { openGoogleMapsDirections(context, currentLocation!!, coordinates) },
            modifier = Modifier.align(Alignment.BottomStart)
                .size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.scrim
            )
        ) {
            Icon(Icons.Filled.LocationOn,
                contentDescription = "Go back",
                modifier = Modifier.size(48.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

fun fetchDirections(
    origin: LatLng,
    destination: LatLng,
    mode: String,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onDirectionsFetched: (List<LatLng>, Leg) -> Unit
) {
    val call = LocationService.api.getDirections(
        origin = "${origin.latitude},${origin.longitude}",
        destination = "${destination.latitude},${destination.longitude}",
        apiKey = "AIzaSyCU0agCz5nU7BbeT7NIcez4LZcJrZ16NvE",
        mode = mode
    )

    call.enqueue(object : Callback<DirectionsResponse?> {
        override fun onResponse(
            call: Call<DirectionsResponse?>,
            response: Response<DirectionsResponse?>
        ) {
            println("Response: $response")
            val routes = response.body()?.routes
            if (!routes.isNullOrEmpty()) {
                val points = routes[0].overview_polyline.points
                onDirectionsFetched(decodePolyline(points), routes[0].legs.firstOrNull()!!)
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "No route found between start and destination",
                        duration = SnackbarDuration.Short
                    )
                }
                onDirectionsFetched(emptyList(), Leg(TravelDuration("", 0), TravelDistance("", 0)))
            }
        }

        override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
            Log.e("MapScreen", "Failed to fetch directions", t)
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "Failed to fetch directions",
                    duration = SnackbarDuration.Short
                )
            }
            onDirectionsFetched(emptyList(), Leg(TravelDuration("", 0), TravelDistance("", 0)))
        }
    })
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