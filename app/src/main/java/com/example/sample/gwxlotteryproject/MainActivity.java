package com.example.sample.gwxlotteryproject;

/**
 * トップ画面
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //「くじ引きスタート！」ボタン
    private Button startButton;
    //「終了」ボタン
    private Button appEnd;
    //BGM
    private MediaPlayer backMusic;
    //「もう終わっちゃうの」ボイス
    private MediaPlayer mouowattyauno;
    //「ふざけないでよ」ボイス
    private MediaPlayer huzakenaideyo;
    //「また遊ぼうね」ボイス
    private MediaPlayer mataasoboune;
    //ボタンクリック音
    private MediaPlayer click;
    //棒くじ画像。上下に動作している画像
    private ImageView boukujiImage;
    //背景画像
    private ImageView backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","onCreate()呼び出し");
        //棒くじ、背景画像のオブジェクト取得
        boukujiImage = (ImageView)findViewById(R.id.boukuji);
        //背景画像のオブジェクト取得
        backImage = (ImageView)findViewById(R.id.back);
        //棒くじ画像にアニメーションを付与
        boukujiImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.a1));
        //「くじ引きスタート！」ボタンのオブジェクト取得
        startButton = (Button)findViewById(R.id.configbutton);
        startButton.setOnClickListener(this);
        //「終了」ボタンのオブジェクト取得
        appEnd = (Button)findViewById(R.id.append);
        appEnd.setOnClickListener(this);
        //ボイス、クリック音、BGMをセット
        click = MediaPlayer.create(getApplicationContext(), R.raw.click);
        backMusic = MediaPlayer.create(getApplicationContext(), R.raw.mainmusic);
        backMusic.setLooping(true);
        mataasoboune = MediaPlayer.create(getApplicationContext(), R.raw.mataasondene);
        mouowattyauno = MediaPlayer.create(getApplicationContext(), R.raw.areremouowaccyauno);
        huzakenaideyo = MediaPlayer.create(getApplicationContext(), R.raw.tyottohuzakenaideyo);
    }
    //人数設定画面(ConfigActivity)への画面遷移実行遅延処理
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //くじ引きスタート！ボタン押下後、人数設定画面へ画面遷移する処理
            Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
            startActivityForResult(intent,0);
            finish();
        }
    };
    //アプリ終了用の実行遅延処理
    private final Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    public void onClick(View v) {
        //「くじ引きスタート！」ボタン押下時の画面遷移処理
        if (v == startButton) {
            try {
                click.start();
            }catch(IllegalStateException e){
                e.getStackTrace();
            }
            //人数設定画面(ConfigActivity)へ遷移する際、1秒遅延させる
            new Handler().postDelayed(runnable, 1000);
            //「終了」ボタン押下時の処理
        } else if (v == appEnd) {
            try {
                mouowattyauno.start();
            }catch(IllegalStateException e){
                e.getStackTrace();
            }
            //ダイアログに関する処理
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            //ダイアログにメッセージをセット
            alert.setMessage("もう終わっちゃうの？");
            alert.setPositiveButton("Yes!",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Yes!ボタンが押下された時の処理
                    //「また遊ぼうね」ボイス再生
                    mataasoboune.start();
                    //「また遊ぼうね」トーストを表示
                    Toast.makeText(getApplicationContext(),"また遊ぼうね！",Toast.LENGTH_SHORT).show();
                    //Yes!を押下した1.5秒後にfinish()を実行する
                    new Handler().postDelayed(runnable2,1500);
                }
            });
            alert.setNegativeButton("No",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Noボタンが押下された時の処理
                    //「ふざけないでよ」ボイス再生
                    huzakenaideyo.start();
                }
            });
            alert.setNeutralButton("Cancel",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //キャンセルボタンを押下した時の処理
                    //特に処理は行わない
                }
            });
            alert.show();
        }
    }
    //端末の戻るボタンを無効化する処理
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    //ダイアログ表示など特定の処理を行いたい場合はここに記述
                    //親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
    //画面が表示されるたびに実行
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume()呼び出し");
        try {
            //BGM再生
            backMusic.start();
        }catch(IllegalStateException e){
            e.getStackTrace();
        }
    }
    //画面が非表示に実行。MainActivityのみ遷移時onPauseが呼ばれる
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("MainActivity","onPause()呼び出し");
        //再生中かの判定
        if (backMusic.isPlaying()){
            //一時停止
            backMusic.pause();
        }
    }
    //アプリ終了時に実行
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("MainActivity","onDestroy()呼び出し");
        if(backMusic != null) {
            backMusic.release();
            click = null;
        }
        if(click != null) {
            click.release();
            backMusic = null;
        }
        //表示された画像、ボタンを破棄
        try {
            boukujiImage.setImageDrawable(null);
            backImage.setImageDrawable(null);
            startButton.setOnClickListener(null);
        } catch (Exception e) {
            Log.e("Errorを検知", e.getMessage().toString());
        }
    }
}
