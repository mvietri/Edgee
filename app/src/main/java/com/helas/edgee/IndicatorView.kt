package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.BatteryManager
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation

class IndicatorView : View {
    private var startAngle = 0
    private var endAngle = 360

    private var radius = 30

    private var strokeWidth = 10

    private var xPosition = 0
    private var yPosition = 0

    private var onColor = Color.GREEN.toInt()
    private var offColor = Color.RED.toInt()
    private var bgColor = Color.BLACK.toInt()

    private var isChargingAnimationOn: Boolean = false;

    fun getStrokeWidth(): Int {
        return strokeWidth
    }

    fun getArcAngle(): Int {
        return startAngle
    }

    fun enableAnimation() {
        startAnimation(ChargingAnimation(0, 360, 2500 ))
    }

    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = strokeWidth
    }

    fun setAngles(startAngle: Int, endAngle: Int) {
        this.startAngle = startAngle
        this.endAngle = endAngle
    }

    fun setPosition(x: Int, y: Int) {
        this.xPosition = x
        this.yPosition = y
    }

    fun setRadius(r: Int) {
        this.radius = r
    }

    fun setColors(onColor: Int, offColor: Int, bgColor: Int) {
        this.onColor = onColor
        this.offColor = offColor
        this.bgColor = bgColor
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { }

    inner class ChargingAnimation(startAngle: Int, sweepAngle: Int, duration: Long) :
        Animation() {
        var mStartAngle: Int = startAngle
        var mSweepAngle: Int = sweepAngle

        override fun applyTransformation(interpolatedTime: Float,  t: Transformation?) {
            val currAngle = mStartAngle + (mSweepAngle * interpolatedTime)
            this@IndicatorView.startAngle =  (-currAngle).toInt()
            invalidate()
        }

        init {
            setDuration(duration)
            interpolator = LinearInterpolator()
            repeatCount = INFINITE
            repeatMode = RESTART
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        var bat = this.getBatteryPercentage(context)

        if (bat >= 70 && !this.isChargingAnimationOn) {
            this.isChargingAnimationOn = true;
        }

       bat = ((bat * this.endAngle) / 100)

        val paintBlack = Paint()
        paintBlack.strokeWidth = strokeWidth.toFloat()
        paintBlack.style = Paint.Style.STROKE
        paintBlack.color = bgColor

        val paint = Paint()
        paint.strokeWidth = strokeWidth.toFloat()
        paint.style = Paint.Style.STROKE
        paint.color =offColor

        val paintAvailable = Paint()
        paintAvailable.strokeWidth = strokeWidth.toFloat()
        paintAvailable.style = Paint.Style.STROKE
        paintAvailable.color =onColor

        val oval = RectF()

        val centerX: Int = xPosition
        val centerY: Int = yPosition

        oval[((centerX - radius).toFloat()), ((centerY - radius).toFloat()), (centerX + radius).toFloat()] = (centerY + radius).toFloat()

        canvas.save()

        // optional background
        if (bgColor != Color.TRANSPARENT) canvas.drawArc(oval, 0f, 360f, false, paintBlack)

        // arc width (from start angle to end)
        canvas.drawArc(oval, startAngle.toFloat(), endAngle.toFloat(), false, paint)

        // fill it from start o current battery level
        canvas.drawArc(oval, startAngle.toFloat(),  bat.toFloat(),false, paintAvailable)

        canvas.restore()
    }

    private fun getBatteryPercentage(context: Context): Int {
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


