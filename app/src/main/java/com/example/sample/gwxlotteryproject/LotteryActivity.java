package com.example.sample.gwxlotteryproject;

/**
 * くじ引きを行う画面
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class LotteryActivity extends AppCompatActivity implements View.OnTouchListener{

    //動作させる棒画像
    ImageView stickImage,boukujiImage,backImage,characterImage,gwxrogoImage,mejirusiImage;
    //座標関係の変数
    private int newDx,newDy = 0;
    private int screenX,screenY;
    //くじ引き棒の画像位置座標
    private int kujibikiBouLocalX,kujibikiBouLocalY;
    //各ボイス
    MediaPlayer girlVoice,backMusic,touchMusic,moveMusic,upMusic;
    //参加人数を格納したリスト
    ArrayList<Integer> allMembersList;
    Intent intent;
    //各色の加算数を代入する変数
    int rbTotal,rbyTotal,rbygTotal,rbygpTotal,rbygpbTotal,
            rbygpboTotal,rbygpbopTotal,rbygpbopwTotal,redMemberDecision;
    //allMembersListの0から取得した値
    int kujiResult;
    //各色の席数を代入する変数
    int redMember,blueMember,yellowMember,greenMember,purpleMember,
                            blackMember,orangeMember,pinkMember,whiteMember;
    //席数の最大数
    int maxValue;
    //ColorsActivityに渡す色
    String resultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("Main2Activity","onCreate()呼び出し");

        //ConfigAcitvityより渡された値を取得する
        try {
            intent = getIntent();
        }catch(Exception e){
            Log.e("Main2Activity","getIntent()がnullです");
        }

        //参加人数リストを取得
        allMembersList = intent.getIntegerArrayListExtra("allMembersList");

        //プリファレンンスに保存された色の判定で使用する値を数値へ変換
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LotteryActivity.this);
        //プリファレンンスに保存された各色の値を変換
        redMember = Integer.parseInt(preferences.getString("redMember",null));
        blueMember = Integer.parseInt(preferences.getString("blueMember",null));
        pinkMember = Integer.parseInt(preferences.getString("pinkMember",null));
        greenMember = Integer.parseInt(preferences.getString("greenMember",null));
        blackMember = Integer.parseInt(preferences.getString("blackMember",null));
        whiteMember = Integer.parseInt(preferences.getString("whiteMember",null));
        yellowMember = Integer.parseInt(preferences.getString("yellowMember",null));
        purpleMember = Integer.parseInt(preferences.getString("purpleMember",null));
        orangeMember = Integer.parseInt(preferences.getString("orangeMember",null));

        rbTotal = Integer.parseInt(preferences.getString("rbTotal",null));
        rbyTotal = Integer.parseInt(preferences.getString("rbyTotal",null));
        rbygTotal = Integer.parseInt(preferences.getString("rbygTotal",null));
        rbygpTotal = Integer.parseInt(preferences.getString("rbygpTotal",null));
        rbygpbTotal = Integer.parseInt(preferences.getString("rbygpbTotal",null));
        rbygpboTotal = Integer.parseInt(preferences.getString("rbygpboTotal",null));
        rbygpbopTotal = Integer.parseInt(preferences.getString("rbygpbopTotal",null));
        rbygpbopwTotal = Integer.parseInt(preferences.getString("rbygpbopwTotal",null));
        redMemberDecision = Integer.parseInt(preferences.getString("redMemberDecision",null));

        //ImageViewの紐付け
        backImage = (ImageView)findViewById(R.id.back);
        stickImage = (ImageView)findViewById(R.id.stick);
        stickImage.setOnTouchListener(this);
        boukujiImage = (ImageView)findViewById(R.id.boukuji);
        gwxrogoImage = (ImageView)findViewById(R.id.gwxrogo);
        mejirusiImage = (ImageView)findViewById(R.id.mejirusi);
        characterImage = (ImageView)findViewById(R.id.character72);

        //効果音
        upMusic = MediaPlayer.create(getApplicationContext(), R.raw.jumpup);
        girlVoice = MediaPlayer.create(getApplicationContext(), R.raw.girlvoice1);
        backMusic = MediaPlayer.create(getApplicationContext(), R.raw.main2music);
        backMusic.setLooping(true);
        touchMusic = MediaPlayer.create(getApplicationContext(), R.raw.cutetouch);
        moveMusic = MediaPlayer.create(getApplicationContext(), R.raw.cuteextend);

        //現在の残席最大値を調べる
        maxValue = 0;
        if(redMember > blueMember){
            maxValue = redMember;
        }else{
            maxValue = blueMember;
        }
        if(yellowMember > maxValue){
            maxValue = yellowMember;
        }
        if(greenMember > maxValue){
            maxValue = greenMember;
        }
        if(purpleMember > maxValue){
            maxValue = purpleMember;
        }
        if(blackMember > maxValue){
            maxValue = blackMember;
        }
        if(orangeMember > maxValue){
            maxValue = orangeMember;
        }
        if(pinkMember > maxValue){
            maxValue = pinkMember;
        }
        if(whiteMember > maxValue){
            maxValue = whiteMember;
        }
    }
    //実行遅延処理
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            intent = new Intent(getApplicationContext(), ColorsActivity.class);
            intent.putExtra("resultColor",resultColor);
            intent.putIntegerArrayListExtra("allMembersList", allMembersList);
            startActivityForResult(intent,0);
            finish();
        }
    };
    //タッチイベントの処理
    @Override
    public boolean onTouch(View v,MotionEvent event) {

        //タッチした場所の絶対座標を取得
        newDx = (int) event.getRawX();
        newDy = (int) event.getRawY();

        switch (event.getAction()) {
            /* 棒にタッチした際の処理 */
            case MotionEvent.ACTION_DOWN:
                //タッチした際の効果音
                touchMusic.start();
                //初期のくじ引き棒の位置を代入
                kujibikiBouLocalX = stickImage.getLeft();
                kujibikiBouLocalY = stickImage.getTop();
                //くじ引き棒に触れた時の座標を代入
                screenX = newDx;
                screenY = newDy;
                //Log.d("onTouch", "ACTION_DOWN :newDx=" + newDx + ",newDy=" + newDy);
                break;

            /* 棒にタッチしたまま動かした時の処理 */
            case MotionEvent.ACTION_MOVE:
                //棒を移動させた際の効果音
                moveMusic.start();
                int diffX = screenX - newDx;
                int diffY = screenY - newDy;
                kujibikiBouLocalX -= diffX;
                kujibikiBouLocalY -= diffY;
                //画像の位置を設定する
                stickImage.layout(kujibikiBouLocalX, kujibikiBouLocalY,
                        kujibikiBouLocalX + stickImage.getWidth(), kujibikiBouLocalY + stickImage.getHeight());
                screenX = newDx;
                screenY = newDy;
                break;

            /* 指を離した時 */
            case MotionEvent.ACTION_UP:
                //指を離した際の効果音
                upMusic.start();
                //目印となる画像のY軸上部からの距離+画像の高さを合計
                int mejirusiTop = mejirusiImage.getTop() + mejirusiImage.getHeight();
                //くじ引き棒を動作させ、画像より指を話した時のY軸上部からの距離
                int kujibikibouReaching = stickImage.getTop();
                //動作させた棒が目印画像の底辺より上へ移動した場合画面遷移する
                if (mejirusiTop > kujibikibouReaching) {
                    if (allMembersList.size() != 0) {
                        while (true) {
                            Collections.shuffle(allMembersList);
                            kujiResult = allMembersList.get(0);

                            if (0 < kujiResult && kujiResult <= redMemberDecision) {
                                //赤
                                if(maxValue == redMember) {
                                    resultColor = "redMember";
                                    break;
                                }

                            } else if (redMemberDecision < kujiResult && kujiResult <= rbTotal) {
                                //青
                                if(maxValue == blueMember) {
                                    resultColor = "blueMember";
                                    break;
                                }

                            } else if (rbTotal < kujiResult && kujiResult <= rbyTotal) {
                                //黄
                                if(maxValue == yellowMember) {
                                    resultColor = "yellowMember";
                                    break;
                                }

                            } else if (rbyTotal < kujiResult && kujiResult <= rbygTotal) {
                                //緑
                                if(maxValue == greenMember) {
                                    resultColor = "greenMember";
                                    break;
                                }

                            } else if (rbygTotal < kujiResult && kujiResult <= rbygpTotal) {
                                //紫
                                if(maxValue == purpleMember) {
                                    resultColor = "purpleMember";
                                    break;
                                }

                            } else if (rbygpTotal < kujiResult && kujiResult <= rbygpbTotal) {
                                //黒
                                if(maxValue == blackMember) {
                                    resultColor = "blackMember";
                                    break;
                                }

                            } else if (rbygpbTotal < kujiResult && kujiResult <= rbygpboTotal) {
                                //オレンジ
                                if(maxValue == orangeMember) {
                                    resultColor = "orangeMember";
                                    break;
                                }

                            } else if (rbygpboTotal < kujiResult && kujiResult <= rbygpbopTotal) {
                                //ピンク
                                if(maxValue == pinkMember) {
                                    resultColor = "pinkMember";
                                    break;
                                }

                            } else if (rbygpbopTotal < kujiResult && kujiResult <= rbygpbopwTotal) {
                                //白
                                if(maxValue == whiteMember) {
                                    resultColor = "whiteMember";
                                    break;
                                }
                            }
                        }
                    } else {
                        //終了画面へ遷移
                        intent = new Intent(this, EndActivity.class);
                    }
                    new Handler().postDelayed(runnable,250);
                } else {
                    //棒が目印画像よりも上部へ移動しなかった場合の処理
                }
                break;
        }
        return true;
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

        @Override
        public void onResume () {
            super.onResume();
            Log.d("Main2Activity","onResume()呼び出し");
            //女の子の声再生
            girlVoice.start();
            //BGM再生
            backMusic.start();
        }
        //画面が非表示になった場合の処理
        @Override
        protected void onPause () {
            super.onPause();
            Log.d("Main2Activity", "onPause()呼び出し");
            //再生中かの判定
            if (girlVoice.isPlaying()){
                //一時停止
                girlVoice.pause();
            }
            if(backMusic.isPlaying()){
                backMusic.pause();
            }
        }
        //Activity画面が遷移した際に呼び出される処理
        @Override
        protected void onDestroy () {
            super.onDestroy();
            Log.d("Main2Activity", "onDestroy()呼び出し");

            if(girlVoice != null) {
                //効果音リソース解放
                girlVoice.release();
                //MediaPlayerを破棄
                girlVoice = null;
            }
            if(backMusic != null) {
                backMusic.release();
                backMusic = null;
            }
            if(touchMusic != null) {
                touchMusic.release();
                touchMusic = null;
            }
            if(moveMusic != null) {
                moveMusic.release();
                moveMusic = null;
            }
            if(upMusic != null) {
                upMusic.release();
                upMusic = null;
            }
            //表示されている画像を破棄
            stickImage.setImageDrawable(null);
            characterImage.setImageDrawable(null);
            gwxrogoImage.setImageDrawable(null);
            mejirusiImage.setImageDrawable(null);
            boukujiImage.setImageDrawable(null);
            backImage.setImageDrawable(null);
        }
    }
