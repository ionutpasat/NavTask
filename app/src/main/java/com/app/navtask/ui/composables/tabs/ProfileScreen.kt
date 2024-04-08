package com.app.navtask.ui.composables.tabs

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.app.navtask.FbViewModel
import com.app.navtask.FsViewModel
import com.app.navtask.R
import com.app.navtask.ui.model.User

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileScreen(
    vm: FbViewModel,
    db: FsViewModel,
    onLogoutButtonClicked: () -> Unit = {}
) {
    val email = vm.getSignedInUser()?.email ?: "default@email.com"
    db.fetchUserByEmail(email)
    val user by db.user.collectAsState(null)

    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        val updatedUser = user?.copy(profileImageUri = uri.toString()) ?: User(email = email, profileImageUri = uri.toString())
        db.updateUser(updatedUser)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Logout,
                contentDescription = "Logout",
                modifier = Modifier.clickable { onLogoutButtonClicked() }
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier.size(120.dp)
        ) {
            val imageUri = remember { mutableStateOf<Uri?>(null) }
            imageUri.value = Uri.parse(user?.profileImageUri)

            Image(
                painter = if (imageUri != null)
                    rememberImagePainter(data = imageUri.value)
                else
                    painterResource(id = R.drawable.profile_image_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { pickImageLauncher.launch("image/*") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
        ) {
            Text(
                text = "User Profile",
                color = Color.Black,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
        ) {
            Column() {
                Text(
                    text = "Name: ${user?.name ?: ""}",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Email: ${vm.getSignedInUser()?.email}",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}