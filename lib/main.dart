import 'dart:async';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:live_activity_plugin/live_activity_plugin.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:workmanager/workmanager.dart';

import 'BackgroundTask.dart';
import 'FirebaseMessagingClass.dart';

// const platform = MethodChannel('runOnUiThread_channel');

//
// Future<void> _printBackgroundMessage() async {
//   String message;
//   debugPrint("_printBackgroundMessage called");
//   try {
//     final String result = await platform.invokeMethod('runOnUiThread');
//     message = 'message is $result .';
//     debugPrint("_printBackgroundMessage message : $message");
//   } on PlatformException catch (e) {
//     message = "Failed to get message : '${e.message}'.";
//     debugPrint("_printBackgroundMessage failed : $message");
//   }
//   print(message);
// }

@pragma('vm:entry-point')
Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  await Firebase.initializeApp();
  try{
    LiveActivityPlugin.startService({
      "title": "titleeeee"
    });
  }catch(e){
    debugPrint("platformChannel >>>>>>>>>> failed: ${e.toString()}");
  }
}

// @pragma('vm:entry-point') // Mandatory if the App is obfuscated or using Flutter 3.1+
// void callbackDispatcher() {
//   Workmanager().executeTask((task, inputData) async{
//     String message;
//     debugPrint("_printBackgroundMessage called");
//     try {
//       final String result = await platform.invokeMethod('runOnUiThread');
//       message = 'message is $result .';
//       debugPrint("_printBackgroundMessage message : $message");
//     } on PlatformException catch (e) {
//       message = "Failed to get message : '${e.message}'.";
//       debugPrint("_printBackgroundMessage failed : $message");
//     }
//     return Future.value(true);
//   });
// }

void main() async{
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();

  // Workmanager().initialize(
  //     callbackDispatcher, // The top level function, aka callbackDispatcher
  //     isInDebugMode: true // If enabled it will post a notification whenever the task is running. Handy for debugging tasks
  // );

  FirebaseMessagingClass().inIt();

  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final int _counter = 0;

  void _incrementCounter() {
    //runOnUiThread(_printBackgroundMessage);
  }

  @override
  void initState() {
    super.initState();
   _startService();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      )
    );
  }
}

Future<void> _startService() async{
  MethodChannel platformChannel = const MethodChannel('runOnUiThread_channel');
  WidgetsFlutterBinding.ensureInitialized();
  var result = await platformChannel.invokeMethod('startService');
}
