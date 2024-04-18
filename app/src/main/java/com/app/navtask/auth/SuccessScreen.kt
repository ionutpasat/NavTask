package com.app.navtask.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.navtask.ui.viewmodel.FbViewModel
import com.app.navtask.R

@Composable
fun SuccessScreen(vm: FbViewModel, onGoToLoginClicked: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "You have been " +
                    "signed in successfully",
            modifier = Modifier.padding(16.dp)
                .align(Alignment.CenterHorizontally),
            style = TextStyle(
            fontSize = 25.sp,
            fontFamily = FontFamily.Cursive,
            color = Color(0xFF001F26)
        )
        )

        Button(onClick = { onGoToLoginClicked() }) {
            Text(text = "Go Back To Login Page")
        }
    }
}