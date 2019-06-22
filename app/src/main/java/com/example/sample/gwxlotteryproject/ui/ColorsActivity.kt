package com.example.sample.gwxlotteryproject.ui

import android.content.Intent
import android.content.res.TypedArray
import android.databinding.DataBindingUtil
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.sample.gwxlotteryproject.R
import com.example.sample.gwxlotteryproject.databinding.ActivityColorsBinding
import com.example.sample.gwxlotteryproject.entity.EachColorEdit
import com.example.sample.gwxlotteryproject.enums.Color
import com.google.gson.Gson

class ColorsActivity : AppCompatActivity() {

    private lateinit var colorArray: TypedArray
    private lateinit var kujibikiResultColorVoice: MediaPlayer
    private lateinit var kujibikiResultImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors)

        title = "クジの結果!!!"

        val binding = DataBindingUtil.setContentView<ActivityColorsBinding>(this, R.layout.activity_colors)

        val resultColor = intent.getSerializableExtra(RESULT_COLOR) as Color

        val allColors = intent.getIntegerArrayListExtra(ALL_COLORS)

        binding.kujibikiRestartButton.setOnLongClickListener {

            if (allColors.isNotEmpty()) {
                val intent = Intent(applicationContext, LotteryActivity::class.java)
                intent.putIntegerArrayListExtra(ALL_COLORS, allColors)
                startActivityForResult(intent, 0)
            } else {
                val intent = Intent(applicationContext, EndActivity::class.java)
                startActivityForResult(intent, 0)
            }

            finish()

            true
        }

        allColors.removeAt(0)

        when (resultColor) {
            Color.RED -> {
                colorArray = resources.obtainTypedArray(R.array.red)
                kujibikiResultImage = binding.redImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.girlaka)
            }
            Color.BLUE -> {
                colorArray = resources.obtainTypedArray(R.array.blue)
                kujibikiResultImage = binding.blueImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.girlblue)
            }
            Color.YELLOW -> {
                colorArray = resources.obtainTypedArray(R.array.yellow)
                kujibikiResultImage = binding.yellowImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.girlyellow)
            }
            Color.GREEN -> {
                colorArray = resources.obtainTypedArray(R.array.green)
                kujibikiResultImage = binding.greenImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.girlgreen)
            }
            Color.PURPLE -> {
                colorArray = resources.obtainTypedArray(R.array.purple)
                kujibikiResultImage = binding.purpleImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.girlpurple)
            }
            Color.BLACK -> {
                colorArray = resources.obtainTypedArray(R.array.black)
                kujibikiResultImage = binding.blackImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.black)
            }
            Color.ORANGE -> {
                colorArray = resources.obtainTypedArray(R.array.orange)
                kujibikiResultImage = binding.orangeImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.orange)
            }
            Color.PIKN -> {
                colorArray = resources.obtainTypedArray(R.array.pink)
                kujibikiResultImage = binding.pinkImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.pink)
            }
            else -> {
                colorArray = resources.obtainTypedArray(R.array.white)
                kujibikiResultImage = binding.whiteImage
                kujibikiResultColorVoice = MediaPlayer.create(applicationContext, R.raw.white)
            }
        }

        val drawable = colorArray.getDrawable(0)
        kujibikiResultImage.also {
            it.setImageDrawable(drawable)
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.a3))
        }

        kujibikiResultColorVoice.start()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val eachColorEdit = preferences.getString("each_color_edit", null)
        val gson = Gson()
        val colorTextVal = gson.fromJson(eachColorEdit!!, EachColorEdit::class.java)
        val colorCount = when (resultColor) {
            Color.RED -> Integer.parseInt(colorTextVal.redInputVal) - 1
            Color.BLUE -> Integer.parseInt(colorTextVal.blueInputVal) - 1
            Color.GREEN -> Integer.parseInt(colorTextVal.greenInputVal) - 1
            Color.YELLOW -> Integer.parseInt(colorTextVal.yellowInputVal) - 1
            Color.PURPLE -> Integer.parseInt(colorTextVal.purpleInputVal) - 1
            Color.PIKN -> Integer.parseInt(colorTextVal.pinkInputVal) - 1
            Color.ORANGE -> Integer.parseInt(colorTextVal.orangeInputVal) - 1
            Color.BLACK -> Integer.parseInt(colorTextVal.blackInputVal) - 1
            Color.WHITE -> Integer.parseInt(colorTextVal.whiteInputVal) - 1
        }

        preferences
                .edit()
                .putString(resultColor.color, colorCount.toString())
                .apply()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN
                && event.keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onResume() {
        super.onResume()
        kujibikiResultColorVoice.start()
    }

    override fun onPause() {
        super.onPause()
        kujibikiResultColorVoice.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        kujibikiResultColorVoice.release()
    }

    companion object {
        private const val RESULT_COLOR = "result_color"
        private const val ALL_COLORS = "all_colors"
    }
}
