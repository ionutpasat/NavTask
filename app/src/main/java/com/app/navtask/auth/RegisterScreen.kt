package com.app.navtask.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.navtask.FbViewModel
import com.app.navtask.FsViewModel
import com.app.navtask.R
import com.app.navtask.ui.components.BottomComponent
import com.app.navtask.ui.components.CheckboxComponent
import com.app.navtask.ui.components.HeadingTextComponent
import com.app.navtask.ui.components.MyTextFieldComponent
import com.app.navtask.ui.components.NormalTextComponent
import com.app.navtask.ui.components.PasswordTextFieldComponent
import com.app.navtask.ui.model.User
import com.app.navtask.ui.model.UserViewModel

@Composable
fun RegisterScreen(onLoginButtonClicked: () -> Unit = {},
                   onMainAppChange: () -> Unit = {},
                   onSuccessRegister: () -> Unit = {},
                   vm: FbViewModel,
                   userVm: UserViewModel
) {
    val emty by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cpassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var cpasswordVisibility by remember { mutableStateOf(false) }
    var errorE by remember { mutableStateOf(false) }
    var errorP by remember { mutableStateOf(false) }
    var errorC by remember { mutableStateOf(false) }
    var errorCP by remember { mutableStateOf(false) }
    var errorF by remember { mutableStateOf(false) }
    var errorL by remember { mutableStateOf(false) }
    var plength by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Create an account",
            style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive, color = Color(0xFF001F26))
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (errorF) {
            Text(
                text = "Enter first name",
                color = Color.Red
            )
        }
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null) },
            trailingIcon = {
                if (firstName.isNotEmpty()) {
                    IconButton(onClick = { firstName = emty}) {
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

        if (errorL) {
            Text(
                text = "Enter last name",
                color = Color.Red
            )
        }
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null) },
            trailingIcon = {
                if (lastName.isNotEmpty()) {
                    IconButton(onClick = { lastName = emty}) {
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
        if (plength) {
            Text(
                text = "Password must be at least 6 characters",
                color = Color.Red
            )
        }
        TextField(
            value = password,
            onValueChange = {
                password = it
                plength = it.length < 6
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
        Spacer(modifier = Modifier.height(30.dp))
        if (errorCP) {
            Text(
                text = "Password doesn't match",
                color = Color.Red
            )
        }
        if (errorC) {
            Text(
                text = "Confirm Password",
                color = Color.Red
            )
        }
        TextField(
            value = cpassword,
            onValueChange = { cpassword = it },
            label = { Text("Confirm Password") },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.baseline_lock_24),
                contentDescription = null) },
            trailingIcon = {
                if (cpassword.isNotEmpty()) {
                    val visibilityIcon = if (cpasswordVisibility) {
                        painterResource(id = R.drawable.baseline_visibility_24)
                    } else {
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    }
                    IconButton(onClick = { cpasswordVisibility = !cpasswordVisibility }) {
                        Icon(painter = visibilityIcon, contentDescription = null)
                    }
                }
            },
            visualTransformation = if (cpasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            if (firstName.isNotEmpty()) {
                errorF = false
                if (lastName.isNotEmpty()) {
                    errorL = false
                    if (email.isNotEmpty()) {
                        errorE = false
                        if (password.isNotEmpty()) {
                            errorP = false
                            if (cpassword.isNotEmpty()) {
                                errorC = false
                                if (password == cpassword) {
                                    errorCP = false
                                    if (!plength) {
                                        vm.onSignup(email, password)
                                    }
                                } else {
                                    errorCP = true
                                }
                            } else {
                                errorC = true
                            }
                        } else {
                            errorP = true
                        }
                    } else {
                        errorE = true
                    }
                } else {
                    errorL = true
                }
            } else {
                errorF = true
            }
        }) {
            Text(text = "Register")
        }
        if (vm.signedIn.value) {
            userVm.addUser(User(email = email, name = "$firstName $lastName"))
            onSuccessRegister()
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
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            NormalTextComponent(value = "Hello there,")
//            HeadingTextComponent(value = "Create an Account")
//            Spacer(modifier = Modifier.height(25.dp))
//
//            Column {
//                MyTextFieldComponent(
//                    labelValue = "First Name",
//                    icon = Icons.Outlined.Person
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                MyTextFieldComponent(
//                    labelValue = "Last Name",
//                    icon = Icons.Outlined.Person
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                MyTextFieldComponent(
//                    labelValue = "Email",
//                    icon = Icons.Outlined.Email
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                PasswordTextFieldComponent(
//                    labelValue = "Password",
//                    icon = Icons.Outlined.Lock
//                )
//                CheckboxComponent()
//                BottomComponent(
//                    textQuery = "Already have an account? ",
//                    textClickable = "Login",
//                    action = "Register",
//                    onLoginButtonClicked,
//                    onMainAppChange
//                )
//            }
//        }
//    }
}