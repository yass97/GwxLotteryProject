package com.example.sample.gwxlotteryproject.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.Toast
import com.example.sample.gwxlotteryproject.*
import com.example.sample.gwxlotteryproject.databinding.ActivityConfigBinding
import com.example.sample.gwxlotteryproject.entity.EachColorEdit
import com.example.sample.gwxlotteryproject.entity.EachColorTotal
import com.example.sample.gwxlotteryproject.ext.createMediaPlayer
import com.google.gson.Gson

class ConfigActivity : AppCompatActivity() {

    private val datawoYomikomiMasukaVoice: MediaPlayer by createMediaPlayer(this, R.raw.datawoyomikomimasuka)
    private val huzakeNaideyoVoice: MediaPlayer by createMediaPlayer(this, R.raw.huzakenaideyo_voice)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        title = ACTION_BAR_TITLE

        val binding = DataBindingUtil.setContentView<ActivityConfigBinding>(this, R.layout.activity_config)

        binding.totalNumberOfPeopleEdit.isFocusable = false

        val editList = listOf(
                binding.redEdit,
                binding.blueEdit,
                binding.yellowEdit,
                binding.greenEdit,
                binding.purpleEdit,
                binding.blackEdit,
                binding.orangeEdit,
                binding.pinkEdit,
                binding.whiteEdit
        )

        binding.calcButton.setOnClickListener {

            if (!editList.none { it.text.isEmpty() }) {
                Toast.makeText(applicationContext, TOAST_NO_INPUT_MSG, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val colorsValTotal = editList.sumBy { Integer.parseInt(it.text.toString()) }
            binding.totalNumberOfPeopleEdit.setText(colorsValTotal.toString())
        }

        binding.loadPreferenceButton.setOnLongClickListener {

            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val ecEditStr = preferences.getString(EACH_COLOR_EDIT, "")

            val gson = Gson()
            val eachColorEdit = gson.fromJson(ecEditStr, EachColorEdit::class.java)

            binding.also {
                it.redEdit.setText(eachColorEdit.redInputVal)
                it.blueEdit.setText(eachColorEdit.blueInputVal)
                it.pinkEdit.setText(eachColorEdit.pinkInputVal)
                it.greenEdit.setText(eachColorEdit.greenInputVal)
                it.blackEdit.setText(eachColorEdit.blackInputVal)
                it.whiteEdit.setText(eachColorEdit.whiteInputVal)
                it.yellowEdit.setText(eachColorEdit.yellowInputVal)
                it.purpleEdit.setText(eachColorEdit.purpleInputVal)
                it.orangeEdit.setText(eachColorEdit.orangeInputVal)
            }

            Toast.makeText(this, BEFORE_DATA_READ_MSG, Toast.LENGTH_SHORT).show()

            true
        }

        binding.lotteryStartButton.setOnClickListener {

            datawoYomikomiMasukaVoice.start()

            AlertDialog.Builder(this)
                    .setMessage(CHECK_DATA_READ_MSG)
                    .setPositiveButton("Yes!") { _, _ ->

                        if (!editList.none { it.text.isEmpty() }) {
                            Toast.makeText(applicationContext, TOAST_NO_INPUT_MSG, Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }

                        val eachColorEdit = EachColorEdit(
                                redInputVal = binding.redEdit.text.toString(),
                                blueInputVal = binding.blueEdit.text.toString(),
                                pinkInputVal = binding.pinkEdit.text.toString(),
                                greenInputVal = binding.greenEdit.text.toString(),
                                blackInputVal = binding.blackEdit.text.toString(),
                                whiteInputVal = binding.whiteEdit.text.toString(),
                                yellowInputVal = binding.yellowEdit.text.toString(),
                                purpleInputVal = binding.purpleEdit.text.toString(),
                                orangeInputVal = binding.orangeEdit.text.toString()
                        )

                        val rbTotal = Integer.parseInt(binding.redEdit.text.toString()) + Integer.parseInt(binding.blueEdit.text.toString())
                        val rbyTotal = rbTotal + Integer.parseInt(binding.yellowEdit.text.toString())
                        val rbygTotal = rbyTotal + Integer.parseInt(binding.greenEdit.text.toString())
                        val rbygpTotal = rbygTotal + Integer.parseInt(binding.purpleEdit.text.toString())
                        val rbygpbTotal = rbygpTotal + Integer.parseInt(binding.blackEdit.text.toString())
                        val rbygpboTotal = rbygpbTotal + Integer.parseInt(binding.orangeEdit.text.toString())
                        val rbygpbopTotal = rbygpboTotal + Integer.parseInt(binding.pinkEdit.text.toString())
                        val rbygpbopwTotal = rbygpbopTotal + Integer.parseInt(binding.whiteEdit.text.toString())

                        val eachColorTotal = EachColorTotal(
                                rbTotal = rbTotal,
                                rbyTotal = rbyTotal,
                                rbygTotal = rbygTotal,
                                rbygpTotal = rbygpTotal,
                                rbygpbTotal = rbygpbTotal,
                                rbygpboTotal = rbygpboTotal,
                                rbygpbopTotal = rbygpbopTotal,
                                rbygpbopwTotal = rbygpbopwTotal
                        )

                        val allMembersList = arrayListOf<Int>()
                        for (i in 1..rbygpbopwTotal) {
                            allMembersList.add(i)
                        }

                        allMembersList.shuffle()

                        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

                        val gson = Gson()
                        preferences.edit().also {
                            it.putString(EACH_COLOR_EDIT, gson.toJson(eachColorEdit))
                            it.putString(EACH_COLOR_TOTAL, gson.toJson(eachColorTotal))
                            it.putString(RED_MEMBER_DECISION, binding.redEdit.text.toString())
                        }.apply()

                        val intent = Intent(applicationContext, LotteryActivity::class.java)
                        intent.putIntegerArrayListExtra(ALL_COLORS, allMembersList)
                        startActivityForResult(intent, 0)
                        finish()

                    }.setNegativeButton("No") { _, _ ->
                        huzakeNaideyoVoice.start()
                        Toast.makeText(applicationContext, "やり直し‼︎‼︎‼︎", Toast.LENGTH_SHORT).show()
                    }.show()
        }

        binding.bulkButton.setOnClickListener {
            val collectiveSetVal = binding.numberOfPeopleBulkEdit.text.toString()
            editList.forEach { it.setText(collectiveSetVal) }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN
                && event.keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        datawoYomikomiMasukaVoice.release()
        huzakeNaideyoVoice.release()
    }

    companion object {
        private const val ACTION_BAR_TITLE = "人数設定"
        private const val TOAST_NO_INPUT_MSG = "未入力項目があります"
        private const val BEFORE_DATA_READ_MSG = "前回データの続きを読み込みました"
        private const val CHECK_DATA_READ_MSG = "このデータを読み込みますか？"
        private const val EACH_COLOR_EDIT = "each_color_edit"
        private const val EACH_COLOR_TOTAL = "each_color_total"
        private const val RED_MEMBER_DECISION = "red_member_decision"
        private const val ALL_COLORS = "all_colors"
    }
}
