import 'package:firebase_auth/firebase_auth.dart';

class UserData {
  final List<String> leagues;
  // ignore: non_constant_identifier_names
  String? UID;

  UserData({required this.leagues}) {
    UID = FirebaseAuth.instance.currentUser?.uid;
  }

  factory UserData.fromFirestore(Map<String, dynamic> data) {
  return UserData(
  // Cast the dynamic list to List<String>
  leagues: List<String>.from(data['leagues'] ?? []),
  );
  }
}
