
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';


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

       debugPrint("fcm token ->>>>>: ${await messaging.getToken()}");


       if (settings.authorizationStatus == AuthorizationStatus.authorized) {
           print('User granted permission');
       } else if (settings.authorizationStatus == AuthorizationStatus.provisional) {
           print('User granted provisional permission');
       } else {
           print('User declined or has not accepted permission');
       }

       FirebaseMessaging.onMessage.listen((RemoteMessage message) async{
           print('Got a message whilst in the foreground!');
           print('Message data: ${message.data}');
           try{
             MethodChannel platformChannel = const MethodChannel('runOnUiThread_channel');
             WidgetsFlutterBinding.ensureInitialized();
             var result = await platformChannel.invokeMethod('runOnUiThread');
             debugPrint("platformChannel >>>>>>>>>> success : $result");

             // Workmanager().registerOneOffTask(
             //   "1",
             //   "simpleTask",
             //   inputData: <String, dynamic>{
             //     'key': 'value',
             //   });
             //
             // debugPrint("Workmanager().registerOneOffTask >>>>>>>>>> success}");

           }catch(e){
             //debugPrint("Workmanager().registerOneOffTask >>>>>>>>>> failed ${e.toString()}");
             debugPrint("platformChannel >>>>>>>>>> failed: ${e.toString()}");
           }
       });
   }

}