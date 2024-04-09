package com.app.navtask.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.app.navtask.ui.viewmodel.FbViewModel

@Composable
fun NotificationMessage(vm: FbViewModel) {
    val notifState = vm.popupNotification.value
    val notifMessage = notifState?.getContentIfNotHandled()
    if (notifMessage != null) {
        Toast.makeText(LocalContext.current, notifMessage, Toast.LENGTH_SHORT).show()
    }
}