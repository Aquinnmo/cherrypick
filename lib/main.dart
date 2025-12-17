import 'package:flutter/material.dart';

void main() {
  runApp( MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    String _Email = '';
    String _Password = '';
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
            padding: const EdgeInsets.symmetric(horizontal: 30),
            child: Column(
              children: [

                //Email
                TextField(
                  decoration: InputDecoration(
                    
                    labelText: 'login',
                  ),
                  onChanged: (value) => _Email = value,
                ),

                const SizedBox(height: 40),

                // password
                Password(
                  onChanged: (value) => _Password = value,
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
                      child: const Text('sign up'),
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


class Password extends StatefulWidget {
  final ValueChanged<String>? onChanged;

  const Password({super.key, this.onChanged});

  @override
  State<Password> createState() => _PasswordState();
}

class _PasswordState extends State<Password> {
  bool _obscureText = true;

  @override
  Widget build(BuildContext context) {
    return TextField(
      obscureText: _obscureText,
      decoration: InputDecoration(
        labelText: 'Password',
        suffixIcon: IconButton(
          icon: Icon(
            _obscureText ? Icons.visibility_off : Icons.visibility,
          ),
          onPressed: () {
            setState(() {
              _obscureText = !_obscureText;
            });
          },
        )
      ),
    );
  }
}

class SignUpPage extends StatelessWidget {
  const SignUpPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
    );
  }
}

