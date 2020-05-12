package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.os.BatteryManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import androidx.core.content.ContextCompat


/**
 * TODO: document your custom view class.
 */
class IndicatorView : View {
    private var startAngle = 0f
    private var endAngle = 360f

    private var radius = 30f

    private var strokeWidth = 10f

    private var xPosition = 0f
    private var yPosition = 0f

    private var isChargingAnimationOn: Boolean = false;

    fun getStrokeWidth(): Float {
        return strokeWidth
    }

    fun getArcAngle(): Float {
        return startAngle
    }

    fun enableAnimation() {
        startAnimation(ChargingAnimation(0f, 360f, 2500 ))
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
    }

    fun setAngles(startAngle: Float, endAngle: Float) {
        this.startAngle = startAngle
        this.endAngle = endAngle
    }

    fun setPosition(x: Float, y: Float) {
        this.xPosition = x
        this.yPosition = y
    }

    fun setRadius(r: Float) {
        this.radius = r
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }


    inner class ChargingAnimation(startAngle: Float, sweepAngle: Float, duration: Long) :
        Animation() {
        var mStartAngle: Float
        var mSweepAngle: Float
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            val currAngle =
                mStartAngle + (mSweepAngle * interpolatedTime)
            this@IndicatorView.startAngle = -currAngle //negative for counterclockwise animation.
            invalidate()
        }

        init {
            mStartAngle = startAngle.toFloat()
            mSweepAngle = sweepAngle.toFloat()
            setDuration(duration)
            interpolator = LinearInterpolator()
            setRepeatCount(Animation.INFINITE)
            setRepeatMode(Animation.RESTART)
        }
    }

    override fun onDraw(canvas: Canvas) {
        Log.i("EE-LEVEL",  "onDraw()")
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        //canvas?.drawCircle(100f, 20f, 70f, paint)
var bat = this.getBatteryPercentage(getContext())

        if (bat >= 70 && !this.isChargingAnimationOn) {
         //   startAnimation(ChargingAnimation(0, 10, 100000))
            this.isChargingAnimationOn = true;
        }

       bat = (((bat * this.endAngle) / 100).toInt())

        Log.i("EE-LEVEL",  "$bat%")

        val width = width.toFloat()
        val height = height.toFloat()

        val paint = Paint()
        paint.color = Color.parseColor("#eb4034")
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE

        val center_x: Float
        val center_y: Float
        val oval = RectF()
        paint.style = Paint.Style.STROKE

        center_x = xPosition
        center_y = yPosition

        oval[(center_x - radius), (center_y - radius), center_x + radius] = center_y + radius

          /*val colors = intArrayOf(
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step1),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0)
          )

          var positions = floatArrayOf(0.0f, 0.1f, 0.2f, 0.3f, 0.35f, 0.45f, 0.50f, 0.75f, 0.9f, 0.9f, 1.0f)*/

        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.progress_color_step4),
            ContextCompat.getColor(context, R.color.progress_color_step5),
            ContextCompat.getColor(context, R.color.progress_color_step3),
            ContextCompat.getColor(context, R.color.progress_color_step6),
            ContextCompat.getColor(context, R.color.progress_color_step2)
        )

        var positions = floatArrayOf(0f, 20f, 55f, 70f, 200f)

          var sweepGradient : SweepGradient? = null

        sweepGradient = SweepGradient(0f,1000.0f, colors,  positions)

        paint.shader = sweepGradient


        canvas.drawArc(oval, this.startAngle, bat.toFloat(), false  , paint)
    }

    fun getBatteryPercentage(context: Context): Int {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, iFilter)
        val level = batteryStatus?.getIntExtra(
            BatteryManager.EXTRA_LEVEL,
            -1
        ) ?: -1
        val scale = batteryStatus?.getIntExtra(
            BatteryManager.EXTRA_SCALE,
            -1
        ) ?: -1
        val batteryPct = level / scale.toFloat()
        return (batteryPct * 100).toInt()
    }
}
