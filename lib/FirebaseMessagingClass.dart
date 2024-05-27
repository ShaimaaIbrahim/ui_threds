
import 'package:firebase_messaging/firebase_messaging.dart';


class FirebaseMessagingClass {

   inIt() async{
       FirebaseMessaging messaging = FirebaseMessaging.instance;

       NotificationSettings settings = await messaging.requestPermission(
           alert: true,
       announcement: false,
       badge: true,
       carPlay: false,
       criticalAlert: false,
       provisional: false,
       sound: true);

       if (settings.authorizationStatus == AuthorizationStatus.authorized) {
           print('User granted permission');
       } else if (settings.authorizationStatus == AuthorizationStatus.provisional) {
           print('User granted provisional permission');
       } else {
           print('User declined or has not accepted permission');
       }

       FirebaseMessaging.onMessage.listen((RemoteMessage message) {
           print('Got a message whilst in the foreground!');
           print('Message data: ${message.data}');

           if (message.notification != null) {
               print('Message also contained a notification: ${message.notification}');
           }
       });
   }

}