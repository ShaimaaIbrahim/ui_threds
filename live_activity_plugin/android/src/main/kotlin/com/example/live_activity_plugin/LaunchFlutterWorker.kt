package com.example.live_activity_plugin

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class LaunchFlutterWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Start the Flutter app here
        startFlutterApp(applicationContext)
        return  Result.success()
    }

    private fun startFlutterApp(context: Context) {
        // Launch the Flutter app from background
//        val intent = Intent(context, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(intent)
    }
}
