package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switch_service.setOnClickListener{
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivityForResult(intent, 0)
        }

        seekBarStrokeWidth.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val t = findViewById(R.id.textViewSeekbarStrokeWidthValue) as TextView
                t.text = i.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                Log.i("tracking bar", "stop")

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("StrokeWidth", seekBar.progress.toFloat())
                editor.apply()
            }
        })

        seekBarAngle.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val t = findViewById(R.id.textViewSeekbarAngleValue) as TextView
                t.text = i.toString() + " degrees"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.i("tracking bar", "stop")

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("Angle", seekBar.progress.toFloat())
                editor.apply()
            }
        })

        switch_service.isChecked = isAccessServiceEnabled(applicationContext)
    }

    override fun onResume() {
        super.onResume()

        switch_service.isChecked = isAccessServiceEnabled(applicationContext)
    }

    fun isAccessServiceEnabled(context: Context): Boolean {
        val prefString =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        if (prefString === null) return false
        return prefString.contains("${context.packageName}/${context.packageName}.${"OverlayService"}")
    }
}