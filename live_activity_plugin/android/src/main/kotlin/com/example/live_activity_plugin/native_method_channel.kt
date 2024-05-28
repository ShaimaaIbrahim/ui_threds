package com.example.live_activity_plugin


import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

object NativeMethodChannel {
     const val LIVE_CHANNEL_NAME = "live_native_channel"
    const val CHANNEL_NAME = "android_native_channel"

     lateinit var methodChannel: MethodChannel

    fun configureChannel(flutterEngine: FlutterEngine) {
        //methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_NAME)
    }

}