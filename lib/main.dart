import 'package:flutter/material.dart';

void main() {
  runApp( MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          backgroundColor: const Color.fromARGB(255, 97, 25, 20),
          title: Text(
            'CherryPick🍒',
            style: TextStyle(
              color: Colors.white,
              fontSize: 20,
            ),
            ),
            centerTitle: true,
          ),

        body: Column(
          children: [
            Align(
                alignment: Alignment.topCenter,
                child: Container(
                  margin: EdgeInsets.only(top: 75),
                  child: const Text(
                  'Welcome to CherryPick, the NHL pick \'em game!',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: Color.fromARGB(255, 0, 0, 0),
                    fontSize: 16,
                  ),
                )
              ),
            ),

            const SizedBox(height: 40),

          Padding(
            padding: EdgeInsets.symmetric(horizontal: 30),
            child: Column(
              children: [
                //Email
                TextField(
                  decoration: InputDecoration(
                    labelText: 'login',
                  ),
                ),

                const SizedBox(height: 60),

                // password
                TextField(
                  obscureText: true,
                  decoration: InputDecoration(
                    labelText: 'Password',
                  ),
                ),
              ],
            )
            )

          ],
        )


      )
    );
  }
}
