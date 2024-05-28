package com.example.isolates

import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler


class MainActivity : FlutterActivity(), FlutterPlugin, MethodCallHandler, ActivityAware {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        Log.d("MainActivity>>>>>", "configureFlutterEngine")

    }
    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        Log.d("MainActivity>>>>>", "onAttachedToEngine")
        // Saving FlutterPluginBinding to static
        flutterPluginBinding = binding

        if(methodChannel==null){
            // Init Flutter Plugin First Time Only
            initPlugin(flutterPluginBinding!!.binaryMessenger)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Log.d("MainActivity>>>>>", "onDetachedFromEngine")
    }

    private fun initPlugin(binaryMessenger: BinaryMessenger) {
        methodChannel = MethodChannel(binaryMessenger, METHOD_CHANNEL)
        methodChannel?.setMethodCallHandler(this)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Log.d("MainActivity>>>>>", "onAttachedToActivity")
        flutterPluginBinding?.binaryMessenger?.let {
            // Reinitialize MethodChannel Forcefully from MainIsolate
            initPlugin(it)
        }
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        Log.d("MainActivity>>>>>", "onDetachedFromActivity")
        methodChannel = null
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        Log.d("MainActivity>>>>>", "onMethodCall")
             if (call.method == "runOnUiThread") {
                 Log.d("MainActivity>>>>>", "runOnUiThread")
                //startService(Intent(this, MyBackgroundService::class.java))
            }
             else if (call.method == "startService") {
                // result.success("shaimaa")
                /// startService(Intent(this, MyFirebaseMessagingService::class.java))
            }
            else {
                Log.d("MainActivity>>>>>", "notImplementedEngine")
                result.notImplemented()
            }
    }


    companion object {
        const val METHOD_CHANNEL = "runOnUiThread_channel"
        // Method Channel instance
        private var methodChannel : MethodChannel?=null
        // Cache flutterPluginBinding
        private var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null
    }

}











//class MainActivity: FlutterActivity(), FlutterPlugin, MethodCallHandler, ActivityAware {
//
//    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
//        // Saving FlutterPluginBinding to static
//        flutterPluginBinding = binding
//
//        if(methodChannel==null){
//            // Init Flutter Plugin First Time Only
//            initPlugin(flutterPluginBinding!!.binaryMessenger)
//        }
//    }
//
//    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
//        TODO("Not yet implemented")
//    }
//
//    private fun initPlugin(binaryMessenger: BinaryMessenger) {
//        methodChannel = MethodChannel(binaryMessenger, "runOnUiThread_channel")
//        methodChannel?.setMethodCallHandler(this)
//    }
//
//    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
//        flutterPluginBinding?.binaryMessenger?.let {
//            // Reinitialize MethodChannel Forcefully from MainIsolate
//            initPlugin(it)
//        }
//    }
//
//    override fun onDetachedFromActivityForConfigChanges() {
//        TODO("Not yet implemented")
//    }
//
//    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onDetachedFromActivity() {
//        methodChannel = null
//    }
//
//    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
//                 if (call.method == "runOnUiThread") {
//                 result.success("shaimaa")
//            } else {
//                result.notImplemented()
//            }
//    }
//
//
//    companion object {
//        const val METHOD_CHANNEL = "runOnUiThread_channel"
//        // Method Channel instance
//        private var methodChannel : MethodChannel?=null
//        // Cache flutterPluginBinding
//         var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding? = null
//    }
//
//}





//
//class MainActivity: FlutterActivity(){
//    companion object {
//        const val METHOD_CHANNEL = "runOnUiThread_channel"
//        private var methodChannel : MethodChannel?=null
//        var engine: FlutterEngine? = null
//    }
//
//    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//        super.configureFlutterEngine(flutterEngine)
//        GeneratedPluginRegistrant.registerWith(flutterEngine)
//        Log.d("MainActivity>>>>>", "configureFlutterEngine")
//       engine = flutterEngine
//       methodChannel= MethodChannel(flutterEngine.dartExecutor.binaryMessenger, METHOD_CHANNEL)
//        methodChannel?.setMethodCallHandler { call, result ->
//            Log.d("MainActivity>>>>>", "setMethodCallHandler")
////            if (call.method == "runOnUiThread") {
////                  result.success("shaimaa")
////                //startService(Intent(this, MyBackgroundService::class.java))
////            }
//             if (call.method == "startService") {
//                // result.success("shaimaa")
//                startService(Intent(this, MyFirebaseMessagingService::class.java))
//                 Log.d("MainActivity>>>>>", "MyFirebaseMessagingService")
//            }
//            else {
//                Log.d("MainActivity>>>>>", "notImplementedEngine")
//                result.notImplemented()
//            }
//        }
//    }
//
//    override fun detachFromFlutterEngine() {
//        super.detachFromFlutterEngine()
//        Log.d("MainActivity>>>>>", "detachFromFlutterEngine")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d("MainActivity>>>>>", "onResume")
//        try {
//          methodChannel= MethodChannel(engine!!.dartExecutor.binaryMessenger, METHOD_CHANNEL)
//        }catch (e: Exception){
//            Log.d("MainActivity>>>>>", "onResume: ${e.message}")
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        Log.d("MainActivity>>>>>", "onCreate")
//        startService(Intent(this, MyBackgroundService::class.java))
//        try {
//           methodChannel= MethodChannel(engine!!.dartExecutor.binaryMessenger, METHOD_CHANNEL)
//       }catch (e: Exception){
//           Log.d("MainActivity>>>>>", "onCreate: ${e.message}")
//       }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d("MainActivity>>>>>", "onPause")
//        Log.d("MainActivity>>>>>Engine", engine!!.toString())
//        methodChannel= MethodChannel(engine!!.dartExecutor.binaryMessenger, METHOD_CHANNEL)
//        methodChannel?.setMethodCallHandler { call, result ->
//            Log.d("MainActivity>>>>>", "onPauseCall")
//            if (call.method == "runOnUiThread") {
//                // result.success("shaimaa")
//                startService(Intent(this, MyBackgroundService::class.java))
//            } else {
//                Log.d("MainActivity>>>>>", "notImplementedPause")
//                result.notImplemented()
//            }
//        }
//    }
//}
