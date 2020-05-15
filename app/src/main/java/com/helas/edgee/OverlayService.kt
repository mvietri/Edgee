package com.helas.edgee

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.BatteryManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import kotlinx.android.synthetic.main.activity_indicator.view.*

class OverlayService: AccessibilityService() {
    private lateinit var lastCustomView: View

    private var batteryLevel: Int = -1
    private var batterySatus: Int = -1
    private var batteryIsCharging: Boolean = false

    private var ACTION_EDGEE_CHANGED = ""

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent) {
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            batterySatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            batteryIsCharging = (batterySatus == BatteryManager.BATTERY_STATUS_CHARGING || batterySatus == BatteryManager.BATTERY_STATUS_FULL)
            updateIndicator(false)
        }
    }

    private val mEdgeeSettingsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateIndicator(true)
        }
    }

    fun updateIndicator(reloadConfig: Boolean) {
        val inflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

        val positionPrefs = getSharedPreferences(getString(R.string.pref_position_setting), Context.MODE_PRIVATE)
        val colorPrefs = getSharedPreferences(getString(R.string.pref_color_setting), Context.MODE_PRIVATE)
        val anglePrefs = getSharedPreferences(getString(R.string.pref_angle_setting), Context.MODE_PRIVATE)

        var startAngle = anglePrefs.getFloat(getString(R.string.start_angle_setting), 0f)
        var endAngle = anglePrefs.getFloat(getString(R.string.end_angle_setting), 360f)

        var xPosition = positionPrefs.getInt(getString(R.string.position_x_setting), 0)
        var yPosition = positionPrefs.getInt(getString(R.string.position_y_setting), 0)

        var strokeWidth = colorPrefs.getFloat(getString(R.string.stroke_width_setting), 10f)
        var radius = colorPrefs.getFloat(getString(R.string.radius_setting), 30f)
        var onColor = colorPrefs.getInt(getString(R.string.on_color_setting), Color.GREEN)
        var offColor = colorPrefs.getInt(getString(R.string.off_color_setting), Color.RED)
        var bgColor = colorPrefs.getInt(getString(R.string.bg_color_setting), Color.BLACK)

        customView.indicator.setAngles(startAngle, endAngle)
        customView.indicator.setPosition(xPosition, yPosition)
        customView.indicator.setStrokeWidth(strokeWidth)
        customView.indicator.setRadius(radius)
        customView.indicator.setColors(onColor, offColor, bgColor)
        customView.indicator.setBatteryLevel(batteryLevel)
        customView.indicator.initView()

        if (batteryIsCharging)
           customView.indicator.enableAnimation()

        wm.addView(customView, localLayoutParams)

        try {
            wm.removeView(lastCustomView)
        } catch (e: Exception) {
        }

        lastCustomView = customView
    }

    override fun onInterrupt() { }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(mBatInfoReceiver)
        this.unregisterReceiver(mEdgeeSettingsReceiver)
    }

    override fun onServiceConnected() {
        ACTION_EDGEE_CHANGED = getString(R.string.action_edgee_changed)

        this.registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        this.registerReceiver(mEdgeeSettingsReceiver, IntentFilter(ACTION_EDGEE_CHANGED))
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }
}