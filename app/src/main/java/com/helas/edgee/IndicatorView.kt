package com.helas.edgee

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation

class IndicatorView : View {
    private var startAngle = 0f
    private var endAngle = 360f

    private var radius = 30f

    private var strokeWidth = 10f

    private var xPosition = 0
    private var yPosition = 0

    private var onColor = Color.GREEN
    private var offColor = Color.RED
    private var bgColor = Color.BLACK

    private var currentBatteryLevel: Int = -1

    private var paintBackground: Paint = Paint()
    private var paintOnBattery: Paint = Paint()
    private var paintOffBattery: Paint = Paint()

    private var oval: RectF = RectF()

    fun enableAnimation() {
        startAnimation(ChargingAnimation(0f, 360f, 2500))
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
    }

    fun setAngles(startAngle: Float, endAngle: Float) {
        this.startAngle = startAngle
        this.endAngle = endAngle
    }

    fun setPosition(x: Int, y: Int) {
        this.xPosition = x
        this.yPosition = y
    }

    fun setRadius(r: Float) {
        this.radius = r
    }

    fun setColors(onColor: Int, offColor: Int, bgColor: Int) {
        this.onColor = onColor
        this.offColor = offColor
        this.bgColor = bgColor
    }

    fun setBatteryLevel(batteryLevel: Int) {
        currentBatteryLevel = ((batteryLevel * this.endAngle) / 100).toInt()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { }

    fun initView() {
        paintBackground = Paint()
        paintBackground.strokeWidth = strokeWidth
        paintBackground.style = Paint.Style.STROKE
        paintBackground.color = bgColor

        paintOnBattery = Paint()
        paintOnBattery.strokeWidth = strokeWidth
        paintOnBattery.style = Paint.Style.STROKE
        paintOnBattery.color = onColor

        paintOffBattery = Paint()
        paintOffBattery.strokeWidth = strokeWidth
        paintOffBattery.style = Paint.Style.STROKE
        paintOffBattery.color = offColor

        oval = RectF()
        oval[((xPosition - radius)), ((yPosition - radius)), (xPosition + radius)] =
            (yPosition + radius)
    }

    inner class ChargingAnimation(startAngle: Float, sweepAngle: Float, duration: Long) :
        Animation() {
        private var mStartAngle: Float = startAngle
        private var mSweepAngle: Float = sweepAngle

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            val currAngle = mStartAngle + (mSweepAngle * interpolatedTime)
            this@IndicatorView.startAngle = currAngle
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

        canvas.save()

        // optional background
        if (bgColor != Color.TRANSPARENT) canvas.drawArc(oval, 0f, 360f, false, paintBackground)

        // arc width (from start angle to end)
        canvas.drawArc(oval, startAngle, endAngle, false, paintOffBattery)

        // fill it from start o current battery level
        canvas.drawArc(oval, startAngle, currentBatteryLevel.toFloat(), false, paintOnBattery)

        canvas.restore()
    }
}


