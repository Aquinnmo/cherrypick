import 'package:flutter/material.dart';
import 'core/app_theme.dart';
import 'features/pages/login.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'CherryPick🍒',
      theme: AppTheme.lightTheme,
      home: const LogInPage(),
    );
  }
}

