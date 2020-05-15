package com.helas.edgee

import android.content.Context
import android.content.Intent
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

    private var bgColor: Int = -1
    private var onColor: Int = -1
    private var offColor: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_color, container, false)

        this.strokeWidthSlider = view.findViewById(R.id.seekBarStrokeWidth) as Slider
        this.radiusSlider = view.findViewById(R.id.seekBarRadius) as Slider
        this.bgColorButton = view.findViewById(R.id.btnBackgroundColor) as Button
        this.offColorButton = view.findViewById(R.id.btnNotAvailableColor) as Button
        this.onColorButton = view.findViewById(R.id.btnRemainingColor) as Button

        strokeWidthSlider.addOnSliderTouchListener(strokeWidthTouchListener)
        radiusSlider.addOnSliderTouchListener(radiusTouchListener)

        onColorButton.setOnClickListener { showColorPickerFor(getString(R.string.on_color_setting), onColor) }
        offColorButton.setOnClickListener { showColorPickerFor(getString(R.string.off_color_setting), offColor) }
        bgColorButton.setOnClickListener { showColorPickerFor(getString(R.string.bg_color_setting), bgColor) }

        this.loadSettings()

        return view
    }

    private fun loadSettings() {
        val colorPrefs = activity?.getSharedPreferences(
            getString(R.string.pref_color_setting),
            Context.MODE_PRIVATE
        )

        strokeWidthSlider.value = colorPrefs!!.getFloat(getString(R.string.stroke_width_setting), 10f)
        radiusSlider.value = colorPrefs!!.getFloat(getString(R.string.radius_setting), 30f)

        bgColor = colorPrefs.getInt(getString(R.string.bg_color_setting), -1)
        offColor = colorPrefs.getInt(getString(R.string.off_color_setting), -1)
        onColor = colorPrefs.getInt(getString(R.string.on_color_setting), -1)
    }

    private fun showColorPickerFor(colorName: String, initialColor: Int) {
        ColorPickerDialogBuilder
            .with(context)
            .setTitle(getString(R.string.pick_color_dialog_title))
            .initialColor(initialColor)
            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
            .density(12)
            .setOnColorSelectedListener { }
            .setPositiveButton(getString(R.string.ok_button)) { _, color, _ ->
                saveSetting(
                    colorName,
                    color
                )
            }
            .setNegativeButton(getString(R.string.cancel_button)) { _, _ -> }
            .build()
            .show()
    }

    private val strokeWidthTouchListener: OnSliderTouchListener = object : OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {}
        override fun onStopTrackingTouch(slider: Slider) {
            saveStrokeWidth(slider.value)
        }
    }

    private val radiusTouchListener: OnSliderTouchListener = object : OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {}
        override fun onStopTrackingTouch(slider: Slider) {
            saveRadius(slider.value)
        }
    }

    fun saveRadius(radius: Float) {
        saveSetting(getString(R.string.radius_setting), radius)
    }

    fun saveStrokeWidth(strokeWidth: Float) {
        saveSetting(getString(R.string.stroke_width_setting), strokeWidth)
    }

    private fun saveSetting(settingName: String, value: Int) {
        when (settingName) {
            getString(R.string.bg_color_setting) -> bgColor = value
            getString(R.string.off_color_setting) -> offColor = value
            getString(R.string.on_color_setting) -> onColor = value
        }

        val editor = activity?.getSharedPreferences(
            getString(R.string.pref_color_setting),
            Context.MODE_PRIVATE
        )?.edit()

        if (editor != null) {
            editor.putInt(settingName, value)
            editor.apply()
        }

        notifyChanges()
    }

    private fun saveSetting(settingName: String, value: Float) {
        val editor = activity?.getSharedPreferences(
            getString(R.string.pref_color_setting),
            Context.MODE_PRIVATE
        )?.edit()

        if (editor != null) {
            editor.putFloat(settingName, value)
            editor.apply()
        }

        notifyChanges()
    }

    private fun notifyChanges() {
        val intent = Intent(getString(R.string.action_edgee_changed))
        intent.putExtra(getString(R.string.pref_color_setting), "")
        context!!.sendBroadcast(intent)
    }

    companion object {
        fun newInstance(): Fragment {
            return ColorFragment()
        }
    }
}
