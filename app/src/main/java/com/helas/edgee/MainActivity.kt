package com.helas.edgee

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


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
        try {
            var file: File = getAppSpecificAlbumStorageDir(applicationContext, "Settings")
            val file1 = File("$file/exported.json")

            val contents = file1.readText()

            val gson: Gson = Gson()
            val obj2: EdgeeSettings = gson.fromJson(contents, EdgeeSettings::class.java)

            val editor = this.getSharedPreferences(
                getString(R.string.pref_position_setting),
                Context.MODE_PRIVATE
            )?.edit()

            if (editor != null) {
                editor.putInt(getString(R.string.position_x_setting), obj2.positionX.toInt())
                editor.putInt(getString(R.string.position_y_setting),  obj2.positionY.toInt())
                editor.apply()
            }

            Toast.makeText(this, getString(R.string.settings_imported), Toast.LENGTH_LONG).show()
        } catch (ex :Exception) {
            Toast.makeText(this, getString(R.string.cannot_write_file_permission), Toast.LENGTH_LONG).show()
        }
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
            try {
                var file: File = getAppSpecificAlbumStorageDir(applicationContext, "Settings")

                /// Serialization
                var obj: EdgeeSettings = EdgeeSettings.newInstance()
                val gson: Gson = Gson()
                val contents: String = gson.toJson(obj)

                val file1 = File("$file/exported.json")

                file1.createNewFile()
                file1.writeText(contents)

                Toast.makeText(this, getString(R.string.settings_exported), Toast.LENGTH_LONG).show()
            } catch (ex :Exception) {
                Toast.makeText(this, getString(R.string.cannot_write_file_permission), Toast.LENGTH_LONG).show()
            }

        } else {
           Toast.makeText(this, getString(R.string.cannot_write_file_permission), Toast.LENGTH_LONG).show()
        }
    }

    private fun getAppSpecificAlbumStorageDir(
        context: Context,
        albumName: String?
    ): File {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS
            ), albumName
        )
        if (file == null || !file.mkdirs()) {
            //Log.e("MainActivity", "Directory not created")
        }
        return file
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