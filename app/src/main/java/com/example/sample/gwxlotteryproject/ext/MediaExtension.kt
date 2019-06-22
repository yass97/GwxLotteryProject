package com.example.sample.gwxlotteryproject.ext

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity

fun createMediaPlayer(activity: AppCompatActivity, raw: Int): Lazy<MediaPlayer> = lazy {
    MediaPlayer.create(activity.applicationContext, raw)
}