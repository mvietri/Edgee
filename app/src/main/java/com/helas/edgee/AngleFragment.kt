package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial

class AngleFragment : Fragment() {
    private lateinit var startAngleSlider: Slider
    private lateinit var endAngleSlider: Slider
    private lateinit var ccwSwitch: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_angle, container, false)

        this.startAngleSlider = view.findViewById(R.id.seekBarStartAngle) as Slider
        this.endAngleSlider = view.findViewById(R.id.seekBarEndAngle) as Slider
        this.ccwSwitch = view.findViewById(R.id.switchCWCCW) as SwitchMaterial

        startAngleSlider.addOnSliderTouchListener(startAngleTouchListener)
        endAngleSlider.addOnSliderTouchListener(endAngleTouchListener)
        ccwSwitch.setOnClickListener { saveMode(ccwSwitch.isChecked) }

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
        ccwSwitch.isChecked = anglePrefs!!.getBoolean(getString(R.string.cw_ccw_setting), false)
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

    private fun saveMode(value: Boolean) {
        val editor = activity?.getSharedPreferences(
            getString(R.string.pref_angle_setting),
            Context.MODE_PRIVATE
        )?.edit()

        if (editor != null) {
            editor.putBoolean(getString(R.string.cw_ccw_setting), value)
            editor.apply()
        }

        notifyChanges()
    }

    private fun saveAngle(settingName: String, value: Float) {
        val editor = activity?.getSharedPreferences(
            getString(R.string.pref_angle_setting),
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
        intent.putExtra(getString(R.string.pref_angle_setting), "")
        context!!.sendBroadcast(intent)
    }

    companion object {
        fun newInstance(): Fragment {
            return AngleFragment()
        }
    }
}
