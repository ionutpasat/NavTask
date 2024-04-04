package com.app.navtask.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.navtask.FbViewModel
import com.app.navtask.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onRegisterButtonClicked: () -> Unit = {},
                onMainAppChange: () -> Unit = {},
                vm: FbViewModel) {

    val emty by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorE by remember { mutableStateOf(false) }
    var errorP by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        if (errorE) {
            Text(
                text = "Enter email",
                color = Color.Red
            )
        }
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null) },
            trailingIcon = {
                if (email.isNotEmpty()) {
                    IconButton(onClick = { email = emty}) {
                        Icon(painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = null
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(30.dp))
        if (errorP) {
            Text(
                text = "Enter password",
                color = Color.Red
            )
        }
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null) },
            trailingIcon = {
                if (password.isNotEmpty()) {
                    val visibilityIcon = if (passwordVisibility) {
                        painterResource(id = R.drawable.baseline_visibility_24)
                    } else {
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    }
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(painter = visibilityIcon, contentDescription = null)
                    }
                }
            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(onClick = {
            if (email.isNotEmpty()) {
                errorE = false
                if (password.isNotEmpty()) {
                    errorP = false
                    vm.login(email, password)
                } else {
                    errorP = true
                }
            } else {
                errorE = true
            }
        }) {
            Text(text = "Login")
        }
        Row (
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable(onClick = onRegisterButtonClicked)
        ){
            Text(text = "Don't have an account? ")
            Text(
                text = "Sign up here",
                fontWeight = FontWeight.Bold
            )
        }
        if (vm.signedIn.value) {
            onMainAppChange()
        }
        vm.signedIn.value = false
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (vm.inProgress.value) {
            CircularProgressIndicator()
        }
    }
//    Surface(
//        color = Color.White,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(28.dp)
//    ) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            ClickableText(
//                text = AnnotatedString("Sign up here"),
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(20.dp),
//                onClick = { onRegisterButtonClicked() },
//                style = TextStyle(
//                    fontSize = 16.sp,
//                    fontFamily = FontFamily.Default,
//                    fontWeight = FontWeight.Bold,
//                    textDecoration = TextDecoration.Underline,
//                    color = Color(0xFF001F26)
//                )
//            )
//        }
//        Column(
//            modifier = Modifier.padding(vertical = 100.dp, horizontal = 20.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            val username = remember { mutableStateOf(TextFieldValue()) }
//            val password = remember { mutableStateOf(TextFieldValue()) }
//
//            Text(
//                text = "Login",
//                style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive, color = Color(0xFF001F26))
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//            TextField(
//                colors = TextFieldDefaults.textFieldColors(
//                    focusedIndicatorColor = Color(0xFF001F26),
//                    unfocusedIndicatorColor = Color.Black,
//                    cursorColor = Color(0xFF001F26),
//                    focusedLabelColor = Color(0xFF001F26),
//                ),
//                label = { Text(text = "Username") },
//                value = username.value,
//                onValueChange = { username.value = it })
//
//            Spacer(modifier = Modifier.height(20.dp))
//            TextField(
//                colors = TextFieldDefaults.textFieldColors(
//                    focusedIndicatorColor = Color(0xFF001F26),
//                    unfocusedIndicatorColor = Color.Black,
//                    cursorColor = Color(0xFF001F26),
//                    focusedLabelColor = Color(0xFF001F26),
//                ),
//                label = { Text(text = "Password") },
//                value = password.value,
//                visualTransformation = PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                onValueChange = { password.value = it })
//
//            Spacer(modifier = Modifier.height(20.dp))
//            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
//                Button(
//                    onClick = { onMainAppChange() },
//                    shape = RoundedCornerShape(50.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xFF001F26)),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                ) {
//                    Text(text = "Login")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//            ClickableText(
//                text = AnnotatedString("Forgot password?"),
//                onClick = { },
//                style = TextStyle(
//                    fontSize = 14.sp,
//                    fontFamily = FontFamily.Default
//                )
//            )
//        }
//    }
}