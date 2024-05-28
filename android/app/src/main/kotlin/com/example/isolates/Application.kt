package com.example.isolates

import io.flutter.app.FlutterApplication



import android.app.Application
import android.util.Log
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.flutter.view.FlutterMain

class MyApplication : FlutterApplication() {

    companion object {
        lateinit var flutterEngine: FlutterEngine
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("MyApplication>>>>>", "onCreate")

        FlutterMain.startInitialization(this)

        flutterEngine = FlutterEngine(this)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "runOnUiThread_channel").setMethodCallHandler { call, result ->

            if (call.method == "runOnUiThread") {
                //runOnUiThread {
                    result.success("Executed on UI thread")
                //}
            } else {
                result.notImplemented()
            }
        }
    }
}
