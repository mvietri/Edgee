package com.helas.edgee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AngleFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_angle, container, false)
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
