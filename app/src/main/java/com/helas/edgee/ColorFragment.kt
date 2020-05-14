package com.helas.edgee

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import com.google.android.material.snackbar.Snackbar
import dev.sasikanth.colorsheet.ColorSheet
import kotlin.math.roundToInt


class ColorFragment : Fragment() {
    private lateinit var strokeWidthSlider: Slider
    private lateinit var radiusSlider: Slider

    private lateinit var bgColorButton: Button
    private lateinit var onColorButton: Button
    private lateinit var offColorButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_color, container, false)

        this.strokeWidthSlider = view.findViewById(R.id.seekBarStrokeWidth) as Slider
        this.radiusSlider = view.findViewById(R.id.seekBarRadius) as Slider
        this.bgColorButton = view.findViewById(R.id.btnBackgroundColor) as Button
        this.offColorButton = view.findViewById(R.id.btnNotAvailableColor) as Button
        this.onColorButton = view.findViewById(R.id.btnRemainingColor) as Button

        strokeWidthSlider.addOnSliderTouchListener(strokeWidthTouchListener)
        radiusSlider.addOnSliderTouchListener(radiusTouchListener)

        onColorButton.setOnClickListener {
            val colors =  intArrayOf(Color.parseColor("#264653"), Color.parseColor("#2a9d8f"), Color.parseColor("#e9c46a"), Color.parseColor("#f4a261"), Color.parseColor("#e76f51"))
            fragmentManager?.let {
                ColorSheet().colorPicker(
                    colors = colors,
                    listener = { color -> saveSetting("OnColor", color)  })
                    .show(it)
            }
        }

        offColorButton.setOnClickListener {
            val colors =  intArrayOf(Color.parseColor("#4f000b"), Color.parseColor("#720026"), Color.parseColor("#ce4257"), Color.parseColor("#ff7f51"), Color.parseColor("#ff9b54"))

            fragmentManager?.let { it1 ->
                ColorSheet().colorPicker(
                    colors = colors,
                    listener = { color -> saveSetting("OffColor", color)  })
                    .show(it1)
            }
        }
        
        bgColorButton.setOnClickListener {
            val colors =  intArrayOf(Color.TRANSPARENT, Color.parseColor("#090e0c"), Color.parseColor("#5f6d72"), Color.parseColor("#262b38"), Color.parseColor("#1c222c"), Color.parseColor("#253237"))

            fragmentManager?.let {
                ColorSheet().colorPicker(
                    colors = colors,
                    listener = { color -> saveSetting("BgColor", color)  })
                    .show(it)
            }
        }

        return view
    }

    private val strokeWidthTouchListener: OnSliderTouchListener = object : OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveStrokeWidth(slider.value.roundToInt()) }
    }

    private val radiusTouchListener: OnSliderTouchListener = object : OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveRadius(slider.value.roundToInt()) }
    }

    fun saveRadius(radius: Int) {
        saveSetting("Radius", radius)
    }

    fun saveStrokeWidth(strokeWidth: Int) {
        saveSetting("StrokeWidth", strokeWidth)
    }

    private fun saveSetting(settingName: String, value: Int) {
        val editor = activity?.getSharedPreferences("ColorSetting", Context.MODE_PRIVATE)?.edit()

        if (editor != null) {
            editor.putInt(settingName, value)
            editor.apply()
        }
    }

    companion object {
        fun newInstance(title: String): Fragment {
            val fragment = ColorFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }}
}
