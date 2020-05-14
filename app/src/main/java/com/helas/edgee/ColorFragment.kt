package com.helas.edgee

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener

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


        onColorButton.setOnClickListener { showColorPickerFor("OnColor") }
        offColorButton.setOnClickListener { showColorPickerFor("OffColor") }
        bgColorButton.setOnClickListener { showColorPickerFor("BgColor") }

        this.loadSettings()

        return view
    }

    private fun loadSettings() {
        val colorPrefs = activity?.getSharedPreferences("ColorSetting", Context.MODE_PRIVATE)

        strokeWidthSlider.value = colorPrefs!!.getFloat("StrokeWidth", 10f)
        radiusSlider.value = colorPrefs!!.getFloat("Radius", 30f)
    }

    private fun showColorPickerFor(colorName: String) {
        ColorPickerDialogBuilder
            .with(context)
            .setTitle("Pick a Color")
            .initialColor(Color.GREEN)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorSelectedListener { }
            .setPositiveButton( "OK" ) { _, color, _ ->saveSetting(colorName, color) }
            .setNegativeButton( "Cancel"  ) { _, _ -> }
            .build()
            .show()
    }

    private val strokeWidthTouchListener: OnSliderTouchListener = object : OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveStrokeWidth(slider.value) }
    }

    private val radiusTouchListener: OnSliderTouchListener = object : OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveRadius(slider.value) }
    }

    fun saveRadius(radius: Float) {
        saveSetting("Radius", radius)
    }

    fun saveStrokeWidth(strokeWidth: Float) {
        saveSetting("StrokeWidth", strokeWidth)
    }

    private fun saveSetting(settingName: String, value: Int) {
        val editor = activity?.getSharedPreferences("ColorSetting", Context.MODE_PRIVATE)?.edit()

        if (editor != null) {
            editor.putInt(settingName, value)
            editor.apply()
        }
    }

    private fun saveSetting(settingName: String, value: Float) {
        val editor = activity?.getSharedPreferences("ColorSetting", Context.MODE_PRIVATE)?.edit()

        if (editor != null) {
            editor.putFloat(settingName, value)
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
