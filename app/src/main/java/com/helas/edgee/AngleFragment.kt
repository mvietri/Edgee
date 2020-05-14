package com.helas.edgee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import kotlin.math.roundToInt

class AngleFragment : Fragment() {
    private lateinit var startAngleSlider: Slider
    private lateinit var endAngleSlider: Slider

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_angle, container, false)

        this.startAngleSlider = view.findViewById(R.id.seekBarStartAngle) as Slider
        this.endAngleSlider = view.findViewById(R.id.seekBarEndAngle) as Slider

        startAngleSlider.addOnSliderTouchListener(startAngleTouchListener)
        endAngleSlider.addOnSliderTouchListener(endAngleTouchListener)

        return view
    }

    private val startAngleTouchListener: Slider.OnSliderTouchListener = object :
        Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveAngle("StartAngle", slider.value.roundToInt()) }
    }

    private val endAngleTouchListener: Slider.OnSliderTouchListener = object :
        Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) { }
        override fun onStopTrackingTouch(slider: Slider) { saveAngle("EndAngle", slider.value.roundToInt()) }
    }


    fun saveAngle(settingName: String, value: Int) {
        val editor = activity?.getSharedPreferences("AngleSetting", Context.MODE_PRIVATE)?.edit()

        if (editor != null) {
            editor.putInt(settingName, value)
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
