package com.helas.edgee

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.BatteryManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import com.helas.edgee.R


class OverlayService: AccessibilityService() {
    private lateinit var lastCustomView: View

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            Log.i("EE-Event",  "$level%")

            // update circle


                  val inflater =
            baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

       val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val customView = inflater.inflate(R.layout.activity_notch, null)


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

            wm.addView(customView, localLayoutParams)


            try {
                wm.removeView(lastCustomView)
            } catch (e: Exception) {
            }

            lastCustomView = customView
        }
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

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val source = event!!.source ?: return
        Log.i("EE-Event", event.toString() + "")
        //Log.i("EE-Source", source.toString() + "")

    }
}