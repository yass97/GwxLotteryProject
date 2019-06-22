package com.example.sample.gwxlotteryproject.ui

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import com.example.sample.gwxlotteryproject.enums.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import com.example.sample.gwxlotteryproject.entity.EachColorEdit
import com.example.sample.gwxlotteryproject.entity.EachColorTotal
import com.example.sample.gwxlotteryproject.R
import com.example.sample.gwxlotteryproject.ext.createMediaPlayer
import com.example.sample.gwxlotteryproject.databinding.ActivityLotteryBinding
import com.google.gson.Gson
import java.util.*

class LotteryActivity : AppCompatActivity() {

    private val girlVoice: MediaPlayer by createMediaPlayer(this, R.raw.girlvoice1)
    private val backMusic: MediaPlayer by createMediaPlayer(this, R.raw.main2music)
    private val kujiStickTouchSound: MediaPlayer  by createMediaPlayer(this, R.raw.cutetouch)
    private val kujiStickMovingSound: MediaPlayer by createMediaPlayer(this, R.raw.cuteextend)
    private val fingerUpSound: MediaPlayer by createMediaPlayer(this, R.raw.jumpup)

    private var whenTouchedKujiStickX: Int = 0
    private var whenTouchedKujiStickY: Int = 0
    private var moveKujiStickLocalX: Int = 0
    private var moveKujiStickLocalY: Int = 0

    private lateinit var allMembersList: ArrayList<Int>

    private var resultColor: Color? = null

    private val runnable = Runnable {
        val intent = Intent(applicationContext, ColorsActivity::class.java)
        intent.putExtra(RESULT_COLOR, resultColor)
        intent.putIntegerArrayListExtra(PREFER_ALL_MEMBERS, allMembersList)
        startActivityForResult(intent, 0)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottery)

        title = "クジを引くがよい"

        backMusic.isLooping = true

        val binding = DataBindingUtil.setContentView<ActivityLotteryBinding>(this, R.layout.activity_lottery)

        allMembersList = intent.getIntegerArrayListExtra(PREFER_ALL_MEMBERS)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()

        val ecEditStr = preferences.getString("each_color_edit", "")
        val eachColorEdit = gson.fromJson(ecEditStr, EachColorEdit::class.java)

        val ecTotal = preferences.getString("each_color_total", "")
        val eachColorTotal = gson.fromJson(ecTotal, EachColorTotal::class.java)
        val redMemberDecision = Integer.parseInt(preferences?.getString("red_member_decision", ""))

        val remainingSeatMaxValue = eachColorEdit.getList().max()

        binding.moveKujiStick.setOnTouchListener { v, event ->

            //タッチした場所の絶対座標を取得
            val newDx = event.rawX.toInt()
            val newDy = event.rawY.toInt()

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    kujiStickTouchSound.start()

                    // 初期のくじ引き棒の位置を代入
                    moveKujiStickLocalX = binding.moveKujiStick.left
                    moveKujiStickLocalY = binding.moveKujiStick.top

                    whenTouchedKujiStickX = newDx
                    whenTouchedKujiStickY = newDy
                }

                MotionEvent.ACTION_MOVE -> {

                    kujiStickMovingSound.start()

                    moveKujiStickLocalX -= (whenTouchedKujiStickX - newDx)
                    moveKujiStickLocalY -= (whenTouchedKujiStickY - newDy)

                    //画像の位置を設定する
                    binding.moveKujiStick.layout(
                            moveKujiStickLocalX,
                            moveKujiStickLocalY,
                            moveKujiStickLocalX + binding.moveKujiStick.width,
                            moveKujiStickLocalY + binding.moveKujiStick.height)

                    whenTouchedKujiStickX = newDx
                    whenTouchedKujiStickY = newDy
                }

                MotionEvent.ACTION_UP -> {

                    fingerUpSound.start()

                    //目印となる画像のY軸上部からの距離+画像の高さを合計
                    val minLineMarker = binding.minLineMark.top + binding.minLineMark.height

                    //くじ引き棒を動作させ、画像より指を話した時のY軸上部からの距離
                    val kujiStickReaching = binding.moveKujiStick.top

                    // 高さの目印の底辺より、移動させたくじ引きの上部の距離が遠い場合、くじ引きを元の位置に戻す
                    if (minLineMarker > kujiStickReaching) {

                        if (allMembersList.size != 0) {

                            while (true) {

                                allMembersList.shuffle()
                                val kujiResult = allMembersList.first()

                                if ((0 < kujiResult) && (kujiResult <= redMemberDecision)) {
                                    //赤
                                    if (remainingSeatMaxValue == eachColorEdit.redInputVal) {
                                        resultColor = Color.RED
                                        break
                                    }

                                } else if ((redMemberDecision < kujiResult) && (kujiResult <= eachColorTotal.rbTotal)) {
                                    //青
                                    if (remainingSeatMaxValue == eachColorEdit.blueInputVal) {
                                        resultColor = Color.BLUE
                                        break
                                    }

                                } else if ((eachColorTotal.rbTotal < kujiResult) && (kujiResult <= eachColorTotal.rbyTotal)) {
                                    //黄
                                    if (remainingSeatMaxValue == eachColorEdit.yellowInputVal) {
                                        resultColor = Color.YELLOW
                                        break
                                    }

                                } else if ((eachColorTotal.rbyTotal < kujiResult) && (kujiResult <= eachColorTotal.rbygTotal)) {
                                    //緑
                                    if (remainingSeatMaxValue == eachColorEdit.greenInputVal) {
                                        resultColor = Color.GREEN
                                        break
                                    }

                                } else if ((eachColorTotal.rbygTotal < kujiResult) && (kujiResult <= eachColorTotal.rbygpTotal)) {
                                    //紫
                                    if (remainingSeatMaxValue == eachColorEdit.purpleInputVal) {
                                        resultColor = Color.PURPLE
                                        break
                                    }

                                } else if ((eachColorTotal.rbygpTotal < kujiResult) && (kujiResult <= eachColorTotal.rbygpbTotal)) {
                                    //黒
                                    if (remainingSeatMaxValue == eachColorEdit.blackInputVal) {
                                        resultColor = Color.BLACK
                                        break
                                    }

                                } else if ((eachColorTotal.rbygpbTotal < kujiResult) && (kujiResult <= eachColorTotal.rbygpboTotal)) {
                                    //オレンジ
                                    if (remainingSeatMaxValue == eachColorEdit.orangeInputVal) {
                                        resultColor = Color.ORANGE
                                        break
                                    }

                                } else if ((eachColorTotal.rbygpboTotal < kujiResult) && (kujiResult <= eachColorTotal.rbygpbopTotal)) {
                                    //ピンク
                                    if (remainingSeatMaxValue == eachColorEdit.pinkInputVal) {
                                        resultColor = Color.PIKN
                                        break
                                    }

                                } else if ((eachColorTotal.rbygpbopTotal < kujiResult) && (kujiResult <= eachColorTotal.rbygpbopwTotal)) {
                                    //白
                                    if (remainingSeatMaxValue == eachColorEdit.whiteInputVal) {
                                        resultColor = Color.WHITE
                                        break
                                    }
                                }
                            }

                        } else {
                            val intent = Intent(this, EndActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        return@setOnTouchListener false
                    }

                    Handler().postDelayed(runnable, 250)
                }
            }

            true
        }
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
        girlVoice.start()
        backMusic.start()
    }

    override fun onPause() {
        super.onPause()
        if (girlVoice.isPlaying) {
            girlVoice.pause()
        }
        if (backMusic.isPlaying) {
            backMusic.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        girlVoice.release()
        backMusic.release()
        kujiStickTouchSound.release()
        kujiStickMovingSound.release()
        fingerUpSound.release()
    }

    companion object {
        private const val PREFER_ALL_MEMBERS = "all_colors"
        private const val RESULT_COLOR = "result_color"
    }
}

class CustomView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
