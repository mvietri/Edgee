package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var yPosition: Float = 0f
    var xPosition: Float = 0f

    class IntroViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            if (position == 1)
                return ColorFragment.newInstance("Position" + position)
            if (position == 2)
                return AngleFragment.newInstance("Position" + position)

            return PositionFragment.newInstance("Position" + position)
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
//        seekBarStrokeWidth.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val t = findViewById(R.id.textViewSeekbarStrokeWidthValue) as TextView
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
//                editor.putFloat("StrokeWidth", seekBar.progress.toFloat())
//                editor.apply()
//            }
//        })
//
//        seekBarStartAngle.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val t = findViewById(R.id.textViewSeekbarStartAngleValue) as TextView
//                t.text = i.toString() + " degrees"
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                Log.i("tracking bar", "stop")
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("StartAngle", seekBar.progress.toFloat())
//                editor.apply()
//            }
//        })
//
//        seekBarEndAngle.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val t = findViewById(R.id.textViewSeekbarEndAngleValue) as TextView
//                t.text = i.toString() + " degrees"
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                Log.i("tracking bar", "stop")
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("EndAngle", seekBar.progress.toFloat())
//                editor.apply()
//            }
//        })
//
//
//        btnPosYMinusOne.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                yPosition = yPosition - 1
//
//                val t = findViewById(R.id.textViewYValue) as TextView
//                t.text = yPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("YPosition", yPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//
//        btnPosYPlusOne.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                yPosition = yPosition +1
//
//                val t = findViewById(R.id.textViewYValue) as TextView
//                t.text = yPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("YPosition", yPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//        btnRemainingColor.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                val colors =  intArrayOf(Color.GREEN, Color.BLUE, Color.BLACK, Color.WHITE, Color.YELLOW)
//                ColorSheet().colorPicker(
//                    colors = colors,
//                    listener = { color ->
//                        // Handle color
//
//                        val editor =
//                            getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                        editor.putInt("OnColor", color)
//                        editor.apply()
//                    })
//                    .show(supportFragmentManager)
//            }
//        })
//
//
//        btnNotAvailableColor.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                val colors =  intArrayOf(Color.RED, Color.DKGRAY, Color.GRAY, Color.MAGENTA, Color.BLACK)
//                ColorSheet().colorPicker(
//                    colors = colors,
//                    listener = { color ->
//                        // Handle color
//
//                        val editor =
//                            getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                        editor.putInt("OffColor", color)
//                        editor.apply()
//                    })
//                    .show(supportFragmentManager)
//            }
//        })
//
//
//
//        btnBackgroundColor.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                val colors =  intArrayOf(Color.TRANSPARENT, Color.GREEN, Color.BLUE, Color.BLACK,  Color.YELLOW)
//                ColorSheet().colorPicker(
//                    colors = colors,
//                    listener = { color ->
//                        // Handle color
//
//                        val editor =
//                            getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                        editor.putInt("BgColor", color)
//                        editor.apply()
//                    })
//                    .show(supportFragmentManager)
//            }
//        })
//
//
//
//        btnPosYMinusTen.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                yPosition = yPosition - 10
//
//                val t = findViewById(R.id.textViewYValue) as TextView
//                t.text = yPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                 editor.putFloat("YPosition", yPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//
//        btnPosYPlusTen.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                yPosition = yPosition +10
//
//                val t = findViewById(R.id.textViewYValue) as TextView
//                t.text = yPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("YPosition", yPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//
//
//        btnPosXMinusOne.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                xPosition = xPosition - 1
//
//                val t = findViewById(R.id.textViewXValue) as TextView
//                t.text = xPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("XPosition", xPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//
//        btnPosXPlusOne.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                xPosition = xPosition +1
//
//                val t = findViewById(R.id.textViewXValue) as TextView
//                t.text = xPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("XPosition", xPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//
//
//        btnPosXMinusTen.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                xPosition = xPosition - 10
//
//                val t = findViewById(R.id.textViewXValue) as TextView
//                t.text = xPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("XPosition", xPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//
//        btnPosXPlusTen.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                xPosition = xPosition +10
//
//                val t = findViewById(R.id.textViewXValue) as TextView
//                t.text = xPosition.toString()
//
//                val editor =
//                    getSharedPreferences("StartingAngleValue", Context.MODE_PRIVATE).edit()
//                editor.putFloat("XPosition", xPosition.toFloat())
//                editor.apply()
//            }
//        })
//
//        seekBarRadius.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                val t = findViewById(R.id.textViewRadiusValue) as TextView
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
//                editor.putFloat("Radius", seekBar.progress.toFloat())
//                editor.apply()
//            }
//        })


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