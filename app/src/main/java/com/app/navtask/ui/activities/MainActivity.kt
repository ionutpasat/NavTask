package com.app.navtask.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.app.navtask.ui.composables.NavTaskApp
import com.app.navtask.ui.model.Db
import com.app.navtask.ui.viewmodel.UserViewModel
import com.app.navtask.ui.theme.NavTaskTheme
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling `setContentView`, instantiating UI components, and binding data.
     *
     * @param savedInstanceState The previously saved instance state, if any.
     */

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    object PreferenceKeys {
        val IS_DARK_THEME_ENABLED = booleanPreferencesKey("is_dark_theme_enabled")
    }

    private val db by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = Db::class.java,
            name = "NavTaskDb"
        ).build()
    }

    private val userVm by viewModels<UserViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return UserViewModel(db.userDao) as T
                }
            }
        }
    )

    private val taskVm by viewModels<TaskViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(db.taskDao) as T
                }
            }
        }
    )


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current

            var location by remember { mutableStateOf<Location?>(null) }
            val permissionState = remember { mutableStateOf(false) }
            var darkTheme by remember { mutableStateOf(false) }
            var darkThemeChanged by remember { mutableStateOf(false) }

            LaunchedEffect(darkTheme) {
                darkTheme = getFeatureEnabled(context)
            }

            if(darkThemeChanged) {
                darkThemeChanged = false
                darkTheme = !darkTheme
                LaunchedEffect(Unit) {
                    saveFeatureEnabled(darkTheme, context)
                }
            }

            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                permissionState.value = isGranted
                if (isGranted) {
                    fetchLocation(context, lifecycleOwner) {
                        location = it
                    }
                }
            }
            LaunchedEffect(Unit) {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) -> {
                        permissionState.value = true
                        fetchLocation(context, lifecycleOwner) {
                            location = it
                            Log.i("tag", "${location?.latitude}: ${location?.longitude}")
                        }
                    }
                    else -> {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
            NavTaskTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavTaskApp(navController, userVm, taskVm, location, onThemeButtonClicked = {
                        darkThemeChanged = true
                    })
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation(context: Context, lifecycleOwner: LifecycleOwner, callback: (Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                callback(location)
            }
    }

    // Write the boolean value
    private suspend fun saveFeatureEnabled(enabled: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_DARK_THEME_ENABLED] = enabled
        }
    }

    // Read the boolean value
    private suspend fun getFeatureEnabled(context: Context): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[PreferenceKeys.IS_DARK_THEME_ENABLED] ?: false
    }
}


