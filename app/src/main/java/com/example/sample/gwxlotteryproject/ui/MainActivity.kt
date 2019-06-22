package com.example.sample.gwxlotteryproject.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.sample.gwxlotteryproject.R
import com.example.sample.gwxlotteryproject.databinding.ActivityMainBinding
import com.example.sample.gwxlotteryproject.ext.createMediaPlayer

class MainActivity : AppCompatActivity() {

    private val bgm: MediaPlayer by createMediaPlayer(this, R.raw.bgm)
    private val mouOwattyaunoVoice: MediaPlayer by createMediaPlayer(this, R.raw.mou_owattyauno_voice)
    private val huzakenaideyoVoice: MediaPlayer by createMediaPlayer(this, R.raw.huzakenaideyo_voice)
    private val mataAsobouneVoice: MediaPlayer by createMediaPlayer(this, R.raw.mata_asoboune_voice)
    private val clickSound: MediaPlayer by createMediaPlayer(this, R.raw.click)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.moveKujibikiImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.kujibiki_image_anim))
        bgm.isLooping = true

        binding.lotteryStartButton.setOnClickListener {
            clickSound.start()
            Handler().postDelayed({
                val intent = Intent(this, ConfigActivity::class.java)
                startActivityForResult(intent, 0)
                finish()
            }, START_DELAY_TIME)
        }

        binding.lotteyEndButton.setOnClickListener {

            mouOwattyaunoVoice.start()

            AlertDialog.Builder(this)
                    .setMessage(END_CHECK_MSG)
                    .setPositiveButton(AlertDialog.BUTTON_POSITIVE) { _, _ ->
                        mataAsobouneVoice.start()
                        Toast.makeText(applicationContext, END_MSG, Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({ finish() }, FINISH_DELAY_TIME)
                    }.setNegativeButton(AlertDialog.BUTTON_NEGATIVE) { _, _ ->
                        huzakenaideyoVoice.start()
                    }.setNeutralButton(AlertDialog.BUTTON_NEUTRAL) { _, _ ->
                    }.show()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if ((event.action == KeyEvent.ACTION_DOWN)
                && (event.keyCode == KeyEvent.KEYCODE_BACK)) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onResume() {
        super.onResume()
        bgm.start()
    }

    override fun onPause() {
        super.onPause()
        if (bgm.isPlaying) {
            bgm.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bgm.release()
        clickSound.release()
    }

    companion object {
        private const val END_CHECK_MSG = "もう終わっちゃうの？"
        private const val END_MSG = "また遊ぼうね！"
        private const val FINISH_DELAY_TIME: Long = 1500
        private const val START_DELAY_TIME: Long = 1000
    }
}
