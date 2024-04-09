package com.app.navtask.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.app.navtask.ui.composables.NavTaskApp
import com.app.navtask.ui.model.Db
import com.app.navtask.ui.viewmodel.UserViewModel
import com.app.navtask.ui.theme.NavTaskTheme
import com.app.navtask.ui.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling `setContentView`, instantiating UI components, and binding data.
     *
     * @param savedInstanceState The previously saved instance state, if any.
     */
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
        setContent {
            NavTaskTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavTaskApp(navController = navController, userVm, taskVm)
                }
            }
        }
    }
}
