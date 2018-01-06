package com.example.sample.gwxlotteryproject;

/**
 * くじ引きが終了した際に表示される画面
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

public class EndActivity extends AppCompatActivity implements View.OnClickListener{

    //トップ画面に戻るボタン
    private Button topBack,appEnd;
    //背景画像
    ImageView backImage;
    //各ボイス
    MediaPlayer syuuryousimasita,mouowattyauno,mataasoboune,kidousuruyo,huzakenaideyo,yattane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Log.d("Main4Activity","onCreate()呼び出し");
        //トップに戻るボタン紐付け
        topBack = (Button)findViewById(R.id.topback);
        topBack.setOnClickListener(this);
        //アプリケーション終了ボタン紐付け
        appEnd = (Button)findViewById(R.id.append);
        appEnd.setOnClickListener(this);
        //背景画像取得
        backImage = (ImageView)findViewById(R.id.back);
        //「くじ引き終了です」TextViewへのアニメーションセット
        findViewById(R.id.kujibikiend).startAnimation(AnimationUtils.loadAnimation(this, R.anim.a3));
        //各種ボイスを取得
        yattane = MediaPlayer.create(getApplicationContext(), R.raw.yattane);
        kidousuruyo = MediaPlayer.create(getApplicationContext(), R.raw.kidousuruyo);
        mataasoboune = MediaPlayer.create(getApplicationContext(), R.raw.mataasondene);
        mouowattyauno = MediaPlayer.create(getApplicationContext(), R.raw.areremouowaccyauno);
        huzakenaideyo = MediaPlayer.create(getApplicationContext(), R.raw.tyottohuzakenaideyo);
        syuuryousimasita = MediaPlayer.create(getApplicationContext(), R.raw.syuuryoushimashita);
    }

    //トップ画面へ遷移する際の実行遅延処理
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivityForResult(intent,0);
            finish();
        }
    };
    //終了しましたボイスの実行遅延処理
    private final Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            syuuryousimasita.start();
        }
    };
    //アプリ終了時の実行遅延処理
    private final Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    //トップ画面へ戻るを押下した時の処理
    public void onClick(View v){
        if(v == topBack){
            //起動するよ？ボイス再生
            kidousuruyo.start();
            //ダイアログに関する処理
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("起動するよ？");
            alert.setPositiveButton("Yes!",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Yes!ボタンが押下された時の処理
                    yattane.start();
                    //ボイス再生を再生した1秒後にトップ画面(MainActivity)へ画面遷移する
                    new Handler().postDelayed(runnable,1000);
                }
            });
            alert.setNegativeButton("No",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Noボタンが押下された時の処理
                    //ふざけないでよボイス再生
                    huzakenaideyo.start();
                }
            });
            alert.show();

        //アプリを終了するボタンを押下した際の処理
        }else if(v == appEnd){
            //もう終わっちゃうの？ボイス再生
            mouowattyauno.start();
            //ダイアログに関する処理
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("もう終わっちゃうの？");
            alert.setPositiveButton("Yes!",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Yes!ボタンが押下された時の処理
                    //また遊ぼうねボイス再生
                    mataasoboune.start();
                    //トーストを表示する
                    Toast.makeText(getApplicationContext(),"また遊ぼうね！",Toast.LENGTH_SHORT).show();
                    //また遊ぼうねToastを表示した1.5秒後にfinish()の処理を行う
                    new Handler().postDelayed(runnable3,1500);
                }
            });
            alert.setNegativeButton("No",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Noボタンが押下された時の処理
                    //ふざけないでよボイス再生
                    huzakenaideyo.start();
                }
            });
            alert.setNeutralButton("Cancel",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //キャンセルボタンを押下した時の処理
                    //特に処理はしない
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
    //Activityが再度表示された時の処理
    @Override
    public void onResume(){
        super.onResume();
        Log.d("Main4Activity","onResume()呼び出し");
        //終了しましたボイスをくじ引き終了です表示の0.5秒後に再生
        new Handler().postDelayed(runnable2,500);
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Main4Activity","onPause()呼び出し");
        if(syuuryousimasita.isPlaying()){
            syuuryousimasita.pause();
        }
    }
    //アプリ終了時に実行
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("Main4Activity","onDestroy()呼び出し");
        if(syuuryousimasita != null) {
            syuuryousimasita.release();
            syuuryousimasita = null;
        }
        if(mouowattyauno != null) {
            mouowattyauno.release();
            mouowattyauno = null;
        }
        if(mataasoboune != null) {
            mataasoboune.release();
            mataasoboune = null;
        }
        if(kidousuruyo != null) {
            kidousuruyo.release();
            kidousuruyo = null;
        }
        if(yattane != null) {
            yattane.release();
            yattane = null;
        }
        //ボタンを破棄
        topBack.setOnClickListener(null);
        appEnd.setOnClickListener(null);
        //背景画像を破棄
        backImage.setImageDrawable(null);
    }
}
