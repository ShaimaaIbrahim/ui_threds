import 'dart:async';
import 'dart:isolate';
import 'dart:ui';
import 'package:flutter/scheduler.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'FirebaseMessagingClass.dart';
import 'LifeCycleEvent.dart';

Future<void> runOnUiThread(Function code) async {
  WidgetsBinding binding = WidgetsBinding.instance;
  // if (binding.debugNeedPause) {
  //   await Future.microtask(() => binding.debugResumeTimers());
  // }
  await binding.endOfFrame;
  code();
}
 const platform = MethodChannel('runOnUiThread_channel');

Future<void> _printBackgroundMessage() async {
  String message;
  try {
    final String result = await platform.invokeMethod('runOnUiThread');
    message = 'message is $result .';
  } on PlatformException catch (e) {
    message = "Failed to get message : '${e.message}'.";
  }
  print(message);
}

Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  await Firebase.initializeApp();
  runOnUiThread(_printBackgroundMessage);
}

void main() {
  FirebaseMessagingClass().inIt();

  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);

  WidgetsBinding.instance.addObserver(
    LifeCycleEvent(
        resumeCallBack: () async {},
        pauseCallBack: () {},
        detachCallBack: () {},
        hiddenCallBack: () async{}
    ),
  );

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
