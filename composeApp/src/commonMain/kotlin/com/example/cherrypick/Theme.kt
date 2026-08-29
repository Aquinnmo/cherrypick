package com.example.cherrypick

import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// The Flutter app hardcoded Color.fromARGB(255, 97, 25, 20) = #611914 three times
// (login.dart:18, signup.dart:10, app_theme.dart). Defined once, here only.
private val CherryMaroon = Color(0xFF611914)

// skipped: MaterialKolor full tonal palette, dark theme (Flutter app had lightTheme
// only). Add if the derived expressive tones look wrong, or dark mode is asked for.
@Composable
fun CherryPickTheme(content: @Composable () -> Unit) =
    MaterialExpressiveTheme(
        colorScheme = expressiveLightColorScheme().copy(
            primary = CherryMaroon,
            onPrimary = Color.White,
        ),
        content = content,
    )
