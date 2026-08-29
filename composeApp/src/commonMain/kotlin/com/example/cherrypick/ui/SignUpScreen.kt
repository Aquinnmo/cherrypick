package com.example.cherrypick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.launch

// sign_up_schematic.jpg, built out from what login.dart's SignUpScreen currently
// lacks (an AppBar and nothing else): email, password, confirm-password, submit,
// plus the "back to log in" affordance the README notes is missing from both images.
@Composable
fun SignUpScreen(onSignedUp: () -> Unit, onBackToLogin: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val passwordsMatch = password.isNotBlank() && password == confirmPassword
    val canSubmit = email.isNotBlank() && passwordsMatch

    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Sign Up")

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
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("confirm password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPassword.isNotBlank() && !passwordsMatch,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        )

        errorMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }

        Button(
            onClick = {
                errorMessage = null
                coroutineScope.launch {
                    runCatching {
                        Firebase.auth.createUserWithEmailAndPassword(email, password)
                    }.onSuccess {
                        onSignedUp()
                    }.onFailure {
                        errorMessage = it.message ?: "Sign up failed"
                    }
                }
            },
            enabled = canSubmit,
            modifier = Modifier.padding(top = 32.dp),
        ) {
            Text("Sign Up")
        }

        TextButton(onClick = onBackToLogin) {
            Text("Back to log in")
        }
    }
}
