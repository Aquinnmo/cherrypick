import 'package:flutter/material.dart';
import 'core/app_theme.dart';
import 'features/pages/login.dart';
import 'firebase_options.dart';
import 'package:firebase_core/firebase_core.dart';

void main() async {

  WidgetsFlutterBinding.ensureInitialized();

  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

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

