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
import com.example.sample.gwxlotteryproject.databinding.ActivityEndBinding
import com.example.sample.gwxlotteryproject.ext.createMediaPlayer

class EndActivity : AppCompatActivity() {

    private val syuRyouShimashitaVoice: MediaPlayer by createMediaPlayer(this, R.raw.syuuryoushimashita)
    private val mouOwattyaunoVoice: MediaPlayer by createMediaPlayer(this, R.raw.mou_owattyauno_voice)
    private val mataAsobouneVoice: MediaPlayer by createMediaPlayer(this, R.raw.mata_asoboune_voice)
    private val kidouSuruyoVoice: MediaPlayer by createMediaPlayer(this, R.raw.kidousuruyo)
    private val huzakenaideyoVoice: MediaPlayer by createMediaPlayer(this, R.raw.huzakenaideyo_voice)
    private val yattaneVoice: MediaPlayer by createMediaPlayer(this, R.raw.yattane)

    private val runnable = Runnable {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivityForResult(intent, 0)
        finish()
    }

    private val runnable2 = Runnable { syuRyouShimashitaVoice.start() }
    private val runnable3 = Runnable { finish() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        title = "クジ終わりング"

        val binding = DataBindingUtil.setContentView<ActivityEndBinding>(this, R.layout.activity_end)

        binding.topReturnButton.setOnClickListener {

            kidouSuruyoVoice.start()

            AlertDialog.Builder(this)
                    .setMessage("起動するよ？")
                    .setPositiveButton("Yes!") { _, _ ->
                        yattaneVoice.start()
                        Handler().postDelayed(runnable, 1000)
                    }.setNegativeButton("No") { _, _ ->
                        huzakenaideyoVoice.start()
                    }.show()
        }

        binding.appEndButton.setOnClickListener {

            mouOwattyaunoVoice.start()

            AlertDialog.Builder(this)
                    .setMessage("もう終わっちゃうの？")
                    .setPositiveButton("Yes!") { _, _ ->
                        mataAsobouneVoice.start()
                        Toast.makeText(applicationContext, "また遊ぼうね！", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed(runnable3, 1500)
                    }.setNegativeButton("No") { _, _ ->
                        huzakenaideyoVoice.start()
                    }.setNeutralButton("Cancel") { _, _ ->
                    }.show()
        }

        binding.kujibikiEndTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.a3))
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
        Handler().postDelayed(runnable2, 500)
    }

    override fun onPause() {
        super.onPause()
        syuRyouShimashitaVoice.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        syuRyouShimashitaVoice.release()
        mouOwattyaunoVoice.release()
        mataAsobouneVoice.release()
        kidouSuruyoVoice.release()
        yattaneVoice.release()
    }
}
