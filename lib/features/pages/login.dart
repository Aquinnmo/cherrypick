import 'package:flutter/material.dart';
import '../../core/app_theme.dart';
import '../widgets/password_field.dart';
import 'package:firebase_auth/firebase_auth.dart';

class LogInPage extends StatefulWidget {
  const LogInPage({super.key});

  @override
  State<LogInPage> createState() => _LogInPageState();
}

class _LogInPageState extends State<LogInPage> {
  bool signUp = false;
  String userEmail = '';
  String userPassword = '';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
            appBar: AppBar(
              backgroundColor: AppTheme.primaryColor,
              title: const Text(
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
                      margin: const EdgeInsets.only(top: 75),
                      child: const Text(
                        'Welcome to CherryPick, the NHL pick \'em game!',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Colors.black,
                          fontSize: 16,
                        ),
                      )),
                ),
                const SizedBox(height: 40),
                Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 30),
                    child: Column(
                      children: [
                        //Email
                        TextField(
                          decoration: InputDecoration(
                            labelText: signUp ? 'Enter your email:' : 'Login:',
                          ),
                          onChanged: (value) => userEmail = value,
                        ),

                        const SizedBox(height: 40),

                        // password
                        Password(
                          onChanged: (value) => userPassword = value,
                        ),

                        const SizedBox(height: 20),

                        Builder(builder: (context) =>
                          Align(
                            alignment: Alignment.centerRight,
                            child: ElevatedButton(
                              onPressed: () async {
                                try
                                {if (signUp) {
                                    await FirebaseAuth.instance
                                        .createUserWithEmailAndPassword(
                                        email: userEmail,
                                        password: userPassword);
                                  }
                                  else
                                    {
                                      await FirebaseAuth.instance.signInWithEmailAndPassword(
                                          email: userEmail,
                                          password: userPassword);
                                    }
                                }
                                on FirebaseAuthException catch (e)
                                {
                                  if (context.mounted) {
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      SnackBar(content: Text(
                                          e.message ?? 'An error occurred')),
                                    );
                                  }
                                }
                              },
                              child: const Text('Submit'),
                            ),
                          ),
                        ),

                        const Padding(
                          padding: EdgeInsets.symmetric(vertical: 20),
                        ),

                        ElevatedButton(
                          onPressed: () {
                            setState(() {
                              signUp = !signUp;
                            });
                          },
                          child: signUp
                              ? const Text('Have an account? Log In!')
                              : const Text('New to CherryPick? Sign Up!'),
                        ),
                      ],
                    ))
              ],
            )));
  }
}