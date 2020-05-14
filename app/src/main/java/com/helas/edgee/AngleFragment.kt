package com.helas.edgee

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider

class AngleFragment : Fragment() {
    private lateinit var startAngleSlider: Slider
    private lateinit var endAngleSlider: Slider

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_angle, container, false)

        this.startAngleSlider = view.findViewById(R.id.seekBarStartAngle) as Slider
        this.endAngleSlider = view.findViewById(R.id.seekBarEndAngle) as Slider

        startAngleSlider.addOnSliderTouchListener(startAngleTouchListener)
        endAngleSlider.addOnSliderTouchListener(endAngleTouchListener)

        this.loadSettings()

        return view
    }

    private fun loadSettings() {
        val anglePrefs = activity?.getSharedPreferences("AngleSetting", Context.MODE_PRIVATE)

        startAngleSlider.value = anglePrefs!!.getFloat("StartAngle", 0f)
        endAngleSlider.value = anglePrefs!!.getFloat("EndAngle", 360f)
    }

    private val startAngleTouchListener: Slider.OnSliderTouchListener = object :
        Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveAngle("StartAngle", slider.value) }
    }

    private val endAngleTouchListener: Slider.OnSliderTouchListener = object :
        Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveAngle("EndAngle", slider.value) }
    }


    fun saveAngle(settingName: String, value: Float) {
        val editor = activity?.getSharedPreferences("AngleSetting", Context.MODE_PRIVATE)?.edit()

        if (editor != null) {
            editor.putFloat(settingName, value)
            editor.apply()
        }
    }

    companion object {
    fun newInstance(title: String): Fragment {
        val fragment = AngleFragment()
        val args = Bundle()
        args.putString("title", title)
        fragment.arguments = args
        return fragment
    }}
}
