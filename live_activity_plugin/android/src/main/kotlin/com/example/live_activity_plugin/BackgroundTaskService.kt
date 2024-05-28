package com.example.live_activity_plugin


import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import org.greenrobot.eventbus.EventBus

class BackgroundTaskService : Service() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            //Perform background task here
            //Toast.makeText(applicationContext, "Background task running", Toast.LENGTH_SHORT).show()
            updateLiveActivity(mapOf(
                "time" to "04:30 PM",
                "stepPercentage" to "1",
                "title" to "yyyyyy",
                "description" to "jkkjkjk"
            ))
            //handler!!.postDelayed(this, 10000) // Repeat task every 10 seconds
        }
        handler!!.post(runnable as Runnable)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handler!!.post(runnable!!)
        return START_STICKY
    }

    override fun onDestroy() {
        handler!!.removeCallbacks(runnable!!)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun updateLiveActivity(jsonData: Map<*, *>) {
        try {
            Log.e("updateLiveActivity", "Start: updateLiveActivity")
            if (isAccessibilitySettingsOn(this)) {
                Log.d("updateLiveActivity", "updateLiveActivity")
                EventBus.getDefault().post(EventUpdateNotificationModel(jsonData))
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
            Log.e("isAccessibilitySet", "Error: ${e.message}")
            false
        }
    }
}

