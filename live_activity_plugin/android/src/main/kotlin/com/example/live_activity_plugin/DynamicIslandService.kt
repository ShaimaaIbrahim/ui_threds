package com.example.live_activity_plugin


import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sulfah.LaunchFlutterWorker
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@RequiresApi(Build.VERSION_CODES.DONUT)
class DynamicIslandService : AccessibilityService() {

    private lateinit var floatingView: View
    private lateinit var customStepperView: View
    private val eventBus = EventBus.getDefault()
    private lateinit var windowManager: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private var inBackground: Boolean = false

    private var steps : List<View> = emptyList<View>()
    private lateinit var  stepOne : ImageView
    private lateinit var  stepTwo : ImageView
    private lateinit var  stepThree : ImageView
    private lateinit var  stepFour : ImageView
    private var notificationJsonModel : Map<*,*>? = null
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
    }

    override fun onInterrupt() {}

    override fun onCreate() {
        super.onCreate()
        eventBus.register(this)
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onServiceConnected() {
        super.onServiceConnected()
        showTheIsland()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun showTheIsland() {
        floatingView = LayoutInflater.from(this).inflate(R.layout.view_dynamic_island, null)
        customStepperView = LayoutInflater.from(this).inflate(R.layout.custom_stepper_layout, null)

        stepOne = customStepperView.findViewById<ImageView>(R.id.progress_step1)
        stepTwo = customStepperView.findViewById<ImageView>(R.id.progress_step2)
        stepThree = customStepperView.findViewById<ImageView>(R.id.progress_step3)
        stepFour = customStepperView.findViewById<ImageView>(R.id.progress_step4)


        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.y = 50

         windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
         windowManager.addView(floatingView, params)

       // _resetStepperValues()

        //liveActivityView handle views..
        val liveActivityView = floatingView.findViewById<View>(R.id.live_activity)
        //liveActivityView click double click..
        liveActivityView.setOnLongClickListener {
            floatingView.let {
                windowManager.removeView(it)
                params.y = 100
                windowManager.addView(customStepperView, params)
            }
            true
        }
        //liveActivityView click single one..
        liveActivityView.setOnClickListener{
            _openFlutterApp()
        }

        //floatingImageView handling appearance.
        val floatingImageView = floatingView.findViewById<ImageView>(R.id.notification_icon)
        Glide.with(this)
            .load(R.drawable.ic_launcher) // Replace with your image resource or URL
            .apply(RequestOptions.circleCropTransform())
            .into(floatingImageView)

        //stepper view handle clicks.
         customStepperView.setOnClickListener {
             _openFlutterApp()
         }

        //stepperImageView handling appearance.
        val stepperImageView = customStepperView.findViewById<ImageView>(R.id.notification_icon)
        Glide.with(this)
            .load(R.drawable.ic_launcher) // Replace with your image resource or URL
            .apply(RequestOptions.circleCropTransform())
            .into(stepperImageView)

        _upDateUi(notificationJsonModel)
    }

    private fun _openFlutterApp() {
        val workRequest = OneTimeWorkRequestBuilder<LaunchFlutterWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun handleStepperViewValues() {

        //notificationTitle handling appearance.
        val  floatingViewTime = floatingView.findViewById<TextView>(R.id.time)
        floatingViewTime.text = notificationJsonModel!!["time"].toString()

        //notificationTitle handling appearance.
        val notificationTitle = customStepperView.findViewById<TextView>(R.id.notification_title)
        notificationTitle.text = notificationJsonModel!!["title"].toString()

        //notificationContent handling appearance.
        val notificationContent = customStepperView.findViewById<TextView>(R.id.notification_content)
        notificationContent.text = notificationJsonModel!!["description"].toString()

        //time handling appearance.
        val time = customStepperView.findViewById<TextView>(R.id.time)
        time.text = notificationJsonModel!!["time"].toString()

        //handle progressView colors steps.
        _handleProgressViewColors(notificationJsonModel!!["stepPercentage"].toString().toInt())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendNotificationModel(event: EventSendNotificationModel) {
        _upDateUi(event.jsonData)
         Log.d("sendNotificationModel", notificationJsonModel.toString())
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun updateNotificationModel(event: EventUpdateNotificationModel) {
        Log.d("updateNotificationModel", notificationJsonModel.toString())
        Handler(Looper.getMainLooper()).post {
             Log.d("updateNotification", notificationJsonModel.toString())
            _upDateUi(event.jsonData)
        }
    }

//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    fun onChangeBackground(event: EventCheckBackground) {
//         inBackground = event.inBackground
//        Log.d("onChangeBackground", inBackground.toString())
//    }

    //custom stepper colors
    private fun _handleProgressViewColors(step: Int){
        when(step){
            0 -> {
                stepOne.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepTwo.setBackgroundResource(R.drawable.custom_progress_bar)
                stepThree.setBackgroundResource(R.drawable.custom_progress_bar)
                stepFour.setBackgroundResource(R.drawable.custom_progress_bar)
            }
            1 -> {
                stepOne.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepTwo.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepThree.setBackgroundResource(R.drawable.custom_progress_bar)
                stepFour.setBackgroundResource(R.drawable.custom_progress_bar)
            }
            2 -> {
                stepOne.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepTwo.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepThree.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepFour.setBackgroundResource(R.drawable.custom_progress_bar)
            }
            3 -> {
                stepOne.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepTwo.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepThree.setBackgroundResource(R.drawable.enabled_progress_bar)
                stepFour.setBackgroundResource(R.drawable.enabled_progress_bar)
            }
        }
        windowManager.updateViewLayout(customStepperView, params)
    }
    private fun _upDateUi(jsonData: Map<*, *>?){
        notificationJsonModel = jsonData
       if (notificationJsonModel!=null) handleStepperViewValues()
    }

}

