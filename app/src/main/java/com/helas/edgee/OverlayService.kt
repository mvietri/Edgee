package com.helas.edgee

import android.accessibilityservice.AccessibilityService
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.BatteryManager

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import kotlinx.android.synthetic.main.activity_indicator.view.*


class OverlayService: AccessibilityService() {
    private lateinit var lastCustomView: View

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
            Log.i("EE-Event Level", "$level%")
            Log.i("EE-Event AC", "$isCharging")
            updateIndicator(isCharging)
        }
    }

    fun updateIndicator(isCharging: Boolean = false) {
        Log.i("EE-Event", "Redraw")

        val inflater =
            baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val customView = inflater.inflate(R.layout.activity_indicator, null)


        val localLayoutParams = WindowManager.LayoutParams()
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        localLayoutParams.gravity = Gravity.TOP
        localLayoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or  // Draws over status bar
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        localLayoutParams.height  =  WindowManager.LayoutParams.WRAP_CONTENT
        localLayoutParams.format = PixelFormat.TRANSPARENT

        val prefs = getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE)

        var startAngle = prefs.getFloat("StartAngle", 0f)
        var endAngle = prefs.getFloat("EndAngle", 360f)
        var xPosition = prefs.getFloat("XPosition", 0f)
        var yPosition = prefs.getFloat("YPosition", 0f)
        var strokeWidth = prefs.getFloat("StrokeWidth", 10f)
        var radius = prefs.getFloat("Radius", 30f)
        var onColor = prefs.getInt("OnColor", Color.GREEN.toInt())
        var offColor = prefs.getInt("OffColor", Color.RED.toInt())
        var bgColor = prefs.getInt("BgColor", Color.BLACK.toInt())

        customView.indicator.setAngles(startAngle, endAngle)
        customView.indicator.setPosition(xPosition, yPosition)
        customView.indicator.setStrokeWidth(strokeWidth)
        customView.indicator.setRadius(radius)
        customView.indicator.setColors(onColor, offColor, bgColor)

//        if (isCharging)
//            customView.indicator.enableAnimation()

        wm.addView(customView, localLayoutParams)

        try {
            wm.removeView(lastCustomView)
        } catch (e: Exception) {
        }

        lastCustomView = customView
    }

    override fun onInterrupt() {
        Log.i("EE-Event",  "Service Interrumped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("EE-Event",  "Service Destroyed")
        this.unregisterReceiver(mBatInfoReceiver)
    }

    override fun onServiceConnected() {
        this.registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        Log.i("EE-Event",  "Service Connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.i("GOT Event", event.eventType.toString())
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (event.packageName != null && event.className != null) {
                val componentName = ComponentName(
                    event.packageName.toString(),
                    event.className.toString()
                )

                if (componentName.flattenToShortString() == "com.helas.edgee/android.widget.TextView") {
                    updateIndicator()
                }

//                val activityInfo = tryGetActivity(componentName)
//                val isActivity = activityInfo != null
//                if (isActivity)
//                    if (componentName.flattenToShortString() == "com.helas.edgee/.MainActivity")
//                        updateIndicator()
            }
        }
    }

    private fun tryGetActivity(componentName: ComponentName): ActivityInfo? {
        try {
            return packageManager.getActivityInfo(componentName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }

    }
}