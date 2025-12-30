import 'package:flutter/material.dart';
import '../../core/app_theme.dart';
import '../widgets/password_field.dart';
import 'signup.dart';

class LogInPage extends StatelessWidget
{
  const LogInPage({super.key});

  @override
  Widget build(BuildContext context)
  {
    String userEmail = '';
    String userPassword = '';
    return MaterialApp(
        home: Scaffold(
            appBar: AppBar(
              backgroundColor: AppTheme.primaryColor,
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
                    padding: const EdgeInsets.symmetric(horizontal: 30),
                    child: Column(
                      children: [

                        //Email
                        TextField(
                          decoration: InputDecoration(

                            labelText: 'Login:',
                          ),
                          onChanged: (value) => userEmail = value,
                        ),

                        const SizedBox(height: 40),

                        // password
                        Password(
                          onChanged: (value) => userPassword = value,
                        ),

                        const SizedBox(height: 20),

                        Row(
                          mainAxisAlignment: MainAxisAlignment.start,
                          children: [
                            ElevatedButton(
                              onPressed: null,    // null is temporary
                              child: const Text('Login'),
                            ),

                            const SizedBox(width: 20),

                            ElevatedButton(
                              onPressed: (){
                                Navigator.push(context, MaterialPageRoute(builder: (context) => const SignUpPage()));
                              },
                              child: const Text('Sign Up'),
                            ),
                          ],
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