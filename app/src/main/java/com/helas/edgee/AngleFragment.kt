package com.helas.edgee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider

class AngleFragment : Fragment() {
    private lateinit var startAngleSlider: Slider
    private lateinit var endAngleSlider: Slider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_angle, container, false)

        this.startAngleSlider = view.findViewById(R.id.seekBarStartAngle) as Slider
        this.endAngleSlider = view.findViewById(R.id.seekBarEndAngle) as Slider

        startAngleSlider.addOnSliderTouchListener(startAngleTouchListener)
        endAngleSlider.addOnSliderTouchListener(endAngleTouchListener)

        this.loadSettings()

        return view
    }

    private fun loadSettings() {
        val anglePrefs = activity?.getSharedPreferences(
            getString(R.string.pref_angle_setting),
            Context.MODE_PRIVATE
        )

        startAngleSlider.value = anglePrefs!!.getFloat(getString(R.string.start_angle_setting), 0f)
        endAngleSlider.value = anglePrefs!!.getFloat(getString(R.string.end_angle_setting), 360f)
    }

    private val startAngleTouchListener: Slider.OnSliderTouchListener = object :
        Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {}
        override fun onStopTrackingTouch(slider: Slider) {
            saveAngle(getString(R.string.start_angle_setting), slider.value)
        }
    }

    private val endAngleTouchListener: Slider.OnSliderTouchListener = object :
        Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {}
        override fun onStopTrackingTouch(slider: Slider) {
            saveAngle(getString(R.string.end_angle_setting), slider.value)
        }
    }


    fun saveAngle(settingName: String, value: Float) {
        val editor = activity?.getSharedPreferences(
            getString(R.string.pref_angle_setting),
            Context.MODE_PRIVATE
        )?.edit()

        if (editor != null) {
            editor.putFloat(settingName, value)
            editor.apply()
        }
    }

    companion object {
        fun newInstance(): Fragment {
            return AngleFragment()
        }
    }
}
