package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var yPosition: Float = 0f
    var xPosition: Float = 0f


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

        seekBarStartAngle.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val t = findViewById(R.id.textViewSeekbarStartAngleValue) as TextView
                t.text = i.toString() + " degrees"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.i("tracking bar", "stop")

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("StartAngle", seekBar.progress.toFloat())
                editor.apply()
            }
        })

        seekBarEndAngle.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val t = findViewById(R.id.textViewSeekbarEndAngleValue) as TextView
                t.text = i.toString() + " degrees"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.i("tracking bar", "stop")

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("EndAngle", seekBar.progress.toFloat())
                editor.apply()
            }
        })


//
//        seekBarX.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val t = findViewById(R.id.textViewXValue) as TextView
//                t.text = i.toString()
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//
//                Log.i("tracking bar", "stop")
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("XPosition", seekBar.progress.toFloat())
//                editor.apply()
//            }
//        })

//
//        seekBarY.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val t = findViewById(R.id.textViewYValue) as TextView
//                t.text = i.toString()
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//
//                Log.i("tracking bar", "stop")
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("YPosition", seekBar.progress.toFloat())
//                editor.apply()
//            }
//        })

        btnPosYMinus.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                yPosition = yPosition - 10

                val t = findViewById(R.id.textViewYValue) as TextView
                t.text = yPosition.toString()

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                 editor.putFloat("YPosition", yPosition.toFloat())
                editor.apply()
            }
        })


        btnPosYPlus.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                yPosition = yPosition +10

                val t = findViewById(R.id.textViewYValue) as TextView
                t.text = yPosition.toString()

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("YPosition", yPosition.toFloat())
                editor.apply()
            }
        })


        btnPosXMinus.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                xPosition = xPosition - 10

                val t = findViewById(R.id.textViewXValue) as TextView
                t.text = yPosition.toString()

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("XPosition", xPosition.toFloat())
                editor.apply()
            }
        })


        btnPosXPlus.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                xPosition = xPosition +10

                val t = findViewById(R.id.textViewXValue) as TextView
                t.text = xPosition.toString()

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("XPosition", xPosition.toFloat())
                editor.apply()
            }
        })

        seekBarRadius.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val t = findViewById(R.id.textViewRadiusValue) as TextView
                t.text = i.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                Log.i("tracking bar", "stop")

                val editor =
                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
                editor.putFloat("Radius", seekBar.progress.toFloat())
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