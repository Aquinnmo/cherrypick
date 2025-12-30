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
    bool signUp = true;
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
                          color: Colors.black,
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

                        Align(
                          alignment: Alignment.centerRight,
                          child: ElevatedButton(
                            onPressed: null,    // null is temporary
                            child: const Text('Submit'),
                          ),
                        ),

                        Padding(
                          padding: const EdgeInsets.symmetric(vertical: 20),
                        ),

                        ElevatedButton(
                          onPressed: (){
                            signUp ? signUp = false : signUp = true;
                          },
                          child: signUp ? const Text('Have an account? Log In!') :
                          const Text('New to CherryPick? Sign Up!'),
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