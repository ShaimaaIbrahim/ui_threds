package com.example.isolates

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterMain
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

class MyBackgroundService : Service() {

    private lateinit var flutterEngine: FlutterEngine
    private val CHANNEL = "runOnUiThread_channel"

    override fun onCreate() {
        super.onCreate()

        Log.d("MyBackgroundService>>>>", "MyBackgroundService");
        // Initialize Flutter engine
        flutterEngine = FlutterEngine(this)
        FlutterMain.startInitialization(this)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        Handler(Looper.getMainLooper()).post {
            // This code will run on the main thread
            // Register the MethodChannel for communication
            MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
                Log.d("MyBackgroundService>>>>", "setMethodCallHandler");
                if (call.method == "runOnUiThread") {
                    Log.d("MyBackgroundService>>>>", "runOnUiThread");
                    result.success("Executed on UI thread")
                } else {
                    result.notImplemented()
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle the service start
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // This example does not support binding
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterEngine.destroy()
    }
}
