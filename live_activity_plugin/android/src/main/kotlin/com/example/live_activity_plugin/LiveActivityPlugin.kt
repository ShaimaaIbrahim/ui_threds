package com.example.live_activity_plugin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import org.greenrobot.eventbus.EventBus

/** LiveActivityPlugin */
class LiveActivityPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "live_activity_plugin")
    channel.setMethodCallHandler(this)
  }

  @android.support.annotation.RequiresApi(Build.VERSION_CODES.M)
  override fun onMethodCall(call: MethodCall, result: Result) {
    try {
      Log.e("LiveMethodCallHandler", "Start: LiveMethodCallHandler")
      when (call.method) {
        "startLiveActivity" -> startLiveActivity(call.arguments as Map<*, *>)
        "updateLiveActivity" -> {
          updateLiveActivity()
//                     context = this
//                     context.startService(Intent(context, BackgroundTaskService::class.java))
        }
        //"updateLiveActivity" -> updateLiveActivity(call.arguments as Map<*, *>)
        "stopLiveActivity" -> Log.d("stopLiveActivity", "stopLiveActivity")
        "appOnBackGround" -> {
          val onBackGround = call.arguments as Boolean
          Log.d("appOnBackGround", onBackGround.toString())
        }
        else -> result.notImplemented()
      }
    } catch (e: Exception) {
      Log.e("LiveMethodCallHandler", "Error: ${e.message}")
      result.error("UNEXPECTED_ERROR", "An unexpected error occurred", e.message)
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  private fun startLiveActivity(jsonData: Map<*, *>) {
    try {
      Log.d("startLiveActivity", "startLiveActivity")
      if (!Settings.canDrawOverlays(this)) {
        requestOverlayPermission()
      } else if (!isAccessibilitySettingsOn(this)) {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
      } else if (Settings.canDrawOverlays(this) && isAccessibilitySettingsOn(this)) {
        EventBus.getDefault().post(EventSendNotificationModel(jsonData))
      }
    } catch (e: Exception) {
      Log.e("startLiveActivity", "Error: ${e.message}")
    }
  }

  private fun updateLiveActivity() {
    try {
      Log.e("updateLiveActivity", "Start: updateLiveActivity")
      if (isAccessibilitySettingsOn(this)) {
        Log.d("updateLiveActivity", "updateLiveActivity")
        //EventBus.getDefault().post(EventUpdateNotificationModel(jsonData))
      }
    } catch (e: Exception) {
      Log.e("updateLiveActivity", "Error: ${e.message}")
    }
  }

  private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
    var accessibilityEnabled = 0
    val service = "$packageName/${DynamicIslandService::class.java.canonicalName}"
    return try {
      accessibilityEnabled = Settings.Secure.getInt(
        mContext.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED
      )
      if (accessibilityEnabled == 1) {
        val settingValue = Settings.Secure.getString(
          mContext.applicationContext.contentResolver,
          Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (settingValue != null) {
          val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
          mStringColonSplitter.setString(settingValue)
          while (mStringColonSplitter.hasNext()) {
            val accessibilityService = mStringColonSplitter.next()
            if (accessibilityService.equals(service, ignoreCase = true)) {
              return true
            }
          }
        }
      }
      false
    } catch (e: Settings.SettingNotFoundException) {
      Log.e("isAccessibilitySettingsOn", "Error: ${e.message}")
      false
    }
  }

  private fun requestOverlayPermission() {
    try {
      val permissionIntent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")
      )
      permissionIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      startActivityForResult(permissionIntent, 8)
    } catch (e: Exception) {
      Log.e("requestOverlayPermission", "Error: ${e.message}")
    }
  }
}
