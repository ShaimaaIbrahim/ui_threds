
import 'package:flutter/services.dart';

import 'live_activity_plugin_platform_interface.dart';

class LiveActivityPlugin {

  static const MethodChannel _channel = MethodChannel('live_activity_plugin');


  Future<String?> getPlatformVersion() {
    return LiveActivityPluginPlatform.instance.getPlatformVersion();
  }

  static Future<void> startService(Map<String, dynamic> data) async {
    try {
      await _channel.invokeMethod('startLiveActivity', data);
    } on PlatformException catch (e) {
      print("Failed to start service: '${e.message}'.");
    }
  }

  static Future<void> updateService(Map<String, dynamic> data) async {
    try {
      await _channel.invokeMethod('updateLiveActivity', data);
    } on PlatformException catch (e) {
      print("Failed to start service: '${e.message}'.");
    }
  }
}
