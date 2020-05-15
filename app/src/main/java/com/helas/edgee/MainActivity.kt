package com.helas.edgee

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter


const val NUMBER_OF_TABS: Int = 3

class MainActivity : AppCompatActivity() {
    class IntroViewPagerAdapter(supportFragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            if (position == 1)
                return ColorFragment.newInstance()
            if (position == 2)
                return AngleFragment.newInstance()

            return PositionFragment.newInstance()
        }

        override fun getCount(): Int {
            return NUMBER_OF_TABS
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var introViewPagerAdapter = IntroViewPagerAdapter(supportFragmentManager)
        vpPager.adapter = introViewPagerAdapter

        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vpPager))
        vpPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        switch_service.setOnClickListener {
            startActivityForResult(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 0)
        }

        switch_service.isChecked = isAccessServiceEnabled(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.import_settings_menu -> {
                importSettings()
                true
            }
            R.id.export_settings_menu -> {
                exportSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun importSettings() {
        Toast.makeText(this, getString(R.string.function_not_implemented), Toast.LENGTH_LONG).show()
    }

    private fun checkStorageWritePermission(): Boolean {
        return if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true;
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);
            false;
        }
    }

    private fun exportSettings() {
        if (checkStorageWritePermission()) {
            Toast.makeText(this, getString(R.string.function_not_implemented), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.cannot_write_file_permission), Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        switch_service.isChecked = isAccessServiceEnabled(applicationContext)
    }

    private fun isAccessServiceEnabled(context: Context): Boolean {
        val prefString =
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
        if (prefString === null) return false
        return prefString.contains("${context.packageName}/${context.packageName}.${"OverlayService"}")
    }
}