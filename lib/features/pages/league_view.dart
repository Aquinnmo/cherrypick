import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import '../../core/app_theme.dart';

class LeagueView extends StatefulWidget {
  final ValueChanged<String>? onChanged;

  const LeagueView({super.key, this.onChanged});

  @override
  State<LeagueView> createState() => _LeagueViewState();
}

class _LeagueViewState extends State<LeagueView> {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          backgroundColor: AppTheme.primaryColor,
          title: const Text(
            'CherryPick 🍒',
            style: TextStyle(
              color: Colors.white,
              fontSize: 20,
            ),
          ),
          centerTitle: true,
          actions: [
            ElevatedButton(
              onPressed: ()  async {
                try {
                  await FirebaseAuth.instance.signOut();
                }
                on FirebaseAuthException catch (e) {
                  if (context.mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text(
                          e.message ?? 'An error occurred')));
                  }
                }
              },
              child: const Text('Log Out'),
            )
          ],
        ),
        body: Column(
          children: [
          Align(
          alignment: Alignment.topCenter,
          child: Container(
              margin: const EdgeInsets.only(top: 50),
              child: const Text(
                'You made it to the league view!\nCongratulations!! 🥳🥳🥳',
                textAlign: TextAlign.center,
                style: TextStyle(
                  color: Colors.black,
                  fontSize: 25,
                ),
              )),
        ),
        ]),),
      );
  }
}