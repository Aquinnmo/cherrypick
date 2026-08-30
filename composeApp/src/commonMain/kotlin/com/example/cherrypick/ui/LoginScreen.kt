package com.example.cherrypick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch

// Port of lib/features/pages/login.dart, minus its three bugs:
// 1. login.dart:15 nested a second MaterialApp inside the root -- there's only one
//    theme/NavHost here (App.kt), so that can't happen.
// 2. login.dart:13-14 declared email/password as locals inside build(), discarded
//    every rebuild -- rememberSaveable below survives both recomposition and rotation.
// 3. password_field.dart never wired onChanged -- wired below.
@Composable
fun LoginScreen(onLoggedIn: () -> Unit, onNavigateToSignUp: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Welcome to CherryPick")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") },
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "🙈" else "👁")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        )

        errorMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }

        // Schematic shows Log In and Sign Up as the two ways into the app --
        // ButtonGroup is the M3E fit for that pairing.
        ButtonGroup(
            modifier = Modifier.padding(top = 32.dp),
            overflowIndicator = {},
        ) {
            clickableItem(
                onClick = {
                    errorMessage = null
                    coroutineScope.launch {
                        runCatching {
                            Firebase.auth.signInWithEmailAndPassword(email, password)
                        }.onSuccess {
                            onLoggedIn()
                        }.onFailure {
                            errorMessage = it.message ?: "Log in failed"
                        }
                    }
                },
                enabled = email.isNotBlank() && password.isNotBlank(),
                label = "Log In",
            )
            clickableItem(onClick = onNavigateToSignUp, label = "Sign Up")
        }
    }
}
