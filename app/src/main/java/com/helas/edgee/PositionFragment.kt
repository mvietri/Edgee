package com.helas.edgee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class PositionFragment : Fragment() {
    private lateinit var plusTenButton: Button
    var xPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_position, container, false)
        this.plusTenButton = view.findViewById(R.id.btnPosXPlusTen) as Button

        this.plusTenButton.setOnClickListener {
            xPosition += 10

            val editor = getActivity()?.getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE)?.edit()
            if (editor != null) {
                editor.putFloat("XPosition", xPosition.toFloat())
                editor.apply()
            }
        }

        return view
    }


    companion object {
        fun newInstance(title: String): Fragment {
            val fragment = PositionFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }}
}
