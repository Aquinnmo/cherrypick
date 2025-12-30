import 'package:flutter/material.dart';

class AppTheme {
  static const primaryColor = Color.fromARGB(255, 97, 25, 20);

  static ThemeData get lightTheme => ThemeData(
    appBarTheme: const AppBarTheme(
      backgroundColor: primaryColor,
      centerTitle: true,
      titleTextStyle: TextStyle(color: Colors.white, fontSize: 20),
    ),
  );
}