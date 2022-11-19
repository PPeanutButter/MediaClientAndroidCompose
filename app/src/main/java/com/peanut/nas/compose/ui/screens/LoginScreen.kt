package com.peanut.nas.compose.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.nas.compose.R
import com.peanut.nas.compose.data.Configuration
import com.peanut.nas.compose.data.RequestStore
import com.peanut.nas.compose.model.ConfigurationDataStore
import com.peanut.nas.compose.model.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: SharedViewModel) {
    val loginState = viewModel.loginState
    val scope = rememberCoroutineScope()
    val configurationDataStore = ConfigurationDataStore(LocalContext.current)
    when(loginState.value){
        is RequestStore.Success -> {
            AlbumScreen(viewModel = viewModel){ // onLogout
                viewModel.setConfiguration(Configuration.Empty)
                scope.launch {
                    configurationDataStore.saveConfiguration(Configuration.Empty)
                }
                loginState.value = RequestStore.Empty()
            }
        }
        else -> {
            LoginPanel(viewModel = viewModel, isLogin = loginState.value !is RequestStore.Loading, configurationDataStore = configurationDataStore)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPanel(viewModel: SharedViewModel, isLogin: Boolean, configurationDataStore: ConfigurationDataStore){
    val context = LocalContext.current
    val configuration = viewModel.configuration.collectAsState()
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf(configuration.value.userName) }
    var userPassword by remember { mutableStateOf(configuration.value.userPassword) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var serverHost by remember { mutableStateOf(configuration.value.host) }
    val loginState = viewModel.loginState

    LaunchedEffect(key1 = true){
        viewModel.loadConfiguration(configurationDataStore)
    }

    if (loginState.value is RequestStore.Empty && configuration.value.isValid() && configuration.value != Configuration.Empty){
        Log.d("LoginScreen", "do network login, ${configuration.value}")
        viewModel.userLogin()
        userName = configuration.value.userName
        userPassword = configuration.value.userPassword
        serverHost = configuration.value.host
        loginState.value = RequestStore.Loading()
    }

    Scaffold(
        Modifier.fillMaxSize()
    ) {
        Image(painter = painterResource(id = R.mipmap.login_background), contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Column(
            Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Peanut NAS", color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Card(
                Modifier
                    .weight(1.7f)
                    .padding(8.dp), shape = RoundedCornerShape(32.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    if (loginState.value is RequestStore.Failure){
                        Toast.makeText(context, (loginState.value as RequestStore.Failure).message, Toast.LENGTH_SHORT).show()
                    }
                    Text(text = "Welcome Back!", fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        OutlinedTextField(
                            value = userName,
                            onValueChange = { v -> userName = v },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(text = "Username") },
                            trailingIcon = {
                                if (userName.isNotEmpty())
                                    IconButton(onClick = { userName = "" }) {
                                        Icon(imageVector = Icons.Rounded.Clear, contentDescription = null)
                                    }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                            singleLine = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true, value = userPassword,
                            onValueChange = { v -> userPassword = v },
                            label = { Text(text = "UserPassword") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(imageVector = if (!isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff, contentDescription = null)
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true, value = serverHost,
                            onValueChange = { v -> serverHost = v },
                            label = { Text(text = "ServerHost") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                            trailingIcon = {
                                if (serverHost.isNotEmpty())
                                    IconButton(onClick = { serverHost = "" }) {
                                        Icon(imageVector = Icons.Rounded.Clear, contentDescription = null)
                                    }
                            })
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val c = Configuration(host = serverHost, userName = userName, userPassword = userPassword)
                                viewModel.setConfiguration(c)
                                scope.launch {
                                    configurationDataStore.saveConfiguration(c)
                                    viewModel.userLogin()
                                }
                            }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)
                        ) {
                            if (isLogin){
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 3.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(text = "Log In")
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            TextButton(onClick = { Toast.makeText(context, "Not Implement Yet!", Toast.LENGTH_SHORT).show() }) {
                                Text(text = "Sign Up")
                            }
                            TextButton(onClick = { Toast.makeText(context, "Not Implement Yet!", Toast.LENGTH_SHORT).show() }) {
                                Text(text = "Forgot Password?", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}