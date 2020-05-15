package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView

class PositionFragment : Fragment() {
    private lateinit var minusTenXButton: Button
    private lateinit var minusOneXButton: Button
    private lateinit var plusOneXButton: Button
    private lateinit var plusTenXButton: Button

    private lateinit var xPositionTextView: MaterialTextView
    private lateinit var yPositionTextView: MaterialTextView

    private lateinit var minusTenYButton: Button
    private lateinit var minusOneYButton: Button
    private lateinit var plusOneYButton: Button
    private lateinit var plusTenYButton: Button

    private var xPosition: Int = 0
    private var yPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_position, container, false)

        this.minusTenXButton = view.findViewById(R.id.btnPosXMinusTen) as Button
        this.minusOneXButton = view.findViewById(R.id.btnPosXMinusOne) as Button
        this.plusOneXButton = view.findViewById(R.id.btnPosXPlusOne) as Button
        this.plusTenXButton = view.findViewById(R.id.btnPosXPlusTen) as Button

        this.minusTenYButton = view.findViewById(R.id.btnPosYMinusTen) as Button
        this.minusOneYButton = view.findViewById(R.id.btnPosYMinusOne) as Button
        this.plusOneYButton = view.findViewById(R.id.btnPosYPlusOne) as Button
        this.plusTenYButton = view.findViewById(R.id.btnPosYPlusTen) as Button

        this.xPositionTextView = view.findViewById(R.id.x_position_label) as MaterialTextView
        this.yPositionTextView = view.findViewById(R.id.y_position_label) as MaterialTextView

        this.minusTenXButton.setOnClickListener { changePosition(-10, true) }
        this.minusOneXButton.setOnClickListener { changePosition(-1, true) }
        this.plusOneXButton.setOnClickListener { changePosition(1, true) }
        this.plusTenXButton.setOnClickListener { changePosition(10, true) }

        this.minusTenYButton.setOnClickListener { changePosition(-10, false) }
        this.minusOneYButton.setOnClickListener { changePosition(-1, false) }
        this.plusOneYButton.setOnClickListener { changePosition(1, false) }
        this.plusTenYButton.setOnClickListener { changePosition(10, false) }

        this.loadSettings()

        return view
    }

    private fun loadSettings() {
        val positionPrefs = activity?.getSharedPreferences(
            getString(R.string.pref_position_setting),
            Context.MODE_PRIVATE
        )

        xPosition = positionPrefs!!.getInt(getString(R.string.position_x_setting), 0)
        yPosition = positionPrefs!!.getInt(getString(R.string.position_y_setting), 0)

        updateLabels()
    }

    private fun updateLabels() {
        xPositionTextView.text = String.format(getString(R.string.x_position_hint), xPosition)
        yPositionTextView.text = String.format(getString(R.string.y_position_hint), yPosition)
    }

    private fun changePosition(change: Int, isXPosition: Boolean) {
        if (isXPosition) {
            xPosition += change
        } else {
            yPosition += change
        }

        val editor = activity?.getSharedPreferences(
            getString(R.string.pref_position_setting),
            Context.MODE_PRIVATE
        )?.edit()

        if (editor != null) {
            editor.putInt(getString(R.string.position_x_setting), xPosition)
            editor.putInt(getString(R.string.position_y_setting), yPosition)
            editor.apply()
            updateLabels()
        }

        notifyChanges()
    }

    private fun notifyChanges() {
        val intent = Intent(getString(R.string.action_edgee_changed))
        intent.putExtra(getString(R.string.pref_position_setting), "")
        context!!.sendBroadcast(intent)
    }

    companion object {
        fun newInstance(): Fragment {
            return PositionFragment()
        }
    }
}
