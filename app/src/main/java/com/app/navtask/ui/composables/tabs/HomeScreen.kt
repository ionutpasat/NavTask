package com.app.navtask.ui.composables.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.navtask.ui.theme.md_theme_light_error
import com.app.navtask.ui.theme.typography
import com.app.navtask.R

/**
 * Composable function that represents the home screen of the application.
 */
@Composable
fun HomeScreen(
    onAddTaskButtonClicked : () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = R.string.home),
            style = typography.titleLarge,
            color = md_theme_light_error
        )

        FloatingActionButton(
            onClick = { onAddTaskButtonClicked() },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Task")
        }
    }
}