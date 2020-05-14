package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    class IntroViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            if (position == 1)
                return ColorFragment.newInstance("Position$position")
            if (position == 2)
                return AngleFragment.newInstance("Position$position")

            return PositionFragment.newInstance("Position$position")
        }
        override fun getCount(): Int {
            return 3
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var introViewPagerAdapter = IntroViewPagerAdapter(supportFragmentManager)
        vpPager.adapter = introViewPagerAdapter

        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vpPager))
        vpPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        switch_service.setOnClickListener{
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivityForResult(intent, 0)
        }

        switch_service.isChecked = isAccessServiceEnabled(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        switch_service.isChecked = isAccessServiceEnabled(applicationContext)
    }

    private fun isAccessServiceEnabled(context: Context): Boolean {
        val prefString =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        if (prefString === null) return false
        return prefString.contains("${context.packageName}/${context.packageName}.${"OverlayService"}")
    }
}