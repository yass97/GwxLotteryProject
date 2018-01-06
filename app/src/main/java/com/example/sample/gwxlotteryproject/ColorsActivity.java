package com.example.sample.gwxlotteryproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity implements View.OnLongClickListener{

    Intent intent;
    //各色の残数
    int colorCount;
    //くじ引きへ戻るボタン
    Button backButton;
    //各色の画像
    Drawable drawable;
    //arrays.xmlの色配列
    TypedArray colorArray;
    //各色の効果音
    MediaPlayer colorsMusic;
    //背景画像
    ImageView backImage,colorsImage;
    //参加人数リスト
    ArrayList<Integer> allMembersList;
    String resultColor,colorPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        Log.d("ColorsActivity","onCreate()呼び出し");

        //Main2より渡された値を取得する
        try {
            intent = getIntent();
        }catch(Exception e){
            Log.e("ColorsActivity","getIntent()がnullです");
        }

        //色の結果判定
        resultColor = intent.getStringExtra("resultColor");
        //参加人数リストを取得
        allMembersList = intent.getIntegerArrayListExtra("allMembersList");
        //くじ引きへ戻るボタンのid取得
        backButton = (Button)findViewById(R.id.backbutton);
        backButton.setOnLongClickListener(this);
        //背景画像のid取得
        backImage = (ImageView)findViewById(R.id.backimage);
        //参加人数リストの先頭の値を削除
        allMembersList.remove(0);
        //Preferenceより赤のデータ読み込み
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ColorsActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        if(resultColor.equals("redMember")){
            //赤
            colorArray = getResources().obtainTypedArray(R.array.red);
            colorsImage = (ImageView)findViewById(R.id.red);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.girlaka);

        }else if(resultColor.equals("blueMember")){
            //青
            colorArray = getResources().obtainTypedArray(R.array.blue);
            colorsImage = (ImageView)findViewById(R.id.blue);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.girlblue);

        }else if(resultColor.equals("yellowMember")){
            //黄色
            colorArray = getResources().obtainTypedArray(R.array.yellow);
            colorsImage = (ImageView)findViewById(R.id.yellow);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.girlyellow);

        }else if(resultColor.equals("greenMember")){
            //緑
            colorArray = getResources().obtainTypedArray(R.array.green);
            colorsImage = (ImageView)findViewById(R.id.green);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.girlgreen);

        }else if(resultColor.equals("purpleMember")){
            //紫
            colorArray = getResources().obtainTypedArray(R.array.purple);
            colorsImage = (ImageView)findViewById(R.id.purple);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.girlpurple);

        }else if(resultColor.equals("blackMember")){
            //黒
            colorArray = getResources().obtainTypedArray(R.array.black);
            colorsImage = (ImageView)findViewById(R.id.black);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.black);

        }else if(resultColor.equals("orangeMember")){
            //オレンジ
            colorArray = getResources().obtainTypedArray(R.array.orange);
            colorsImage = (ImageView)findViewById(R.id.orange);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.orange);

        }else if(resultColor.equals("pinkMember")){
            //ピンク
            colorArray = getResources().obtainTypedArray(R.array.pink);
            colorsImage = (ImageView)findViewById(R.id.pink);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.pink);

        }else{
            //白
            colorArray = getResources().obtainTypedArray(R.array.white);
            colorsImage = (ImageView)findViewById(R.id.white);
            //結果画像を表示する際の効果音取得
            colorsMusic = MediaPlayer.create(getApplicationContext(), R.raw.white);

        }

        drawable = colorArray.getDrawable(0);
        colorsImage.setImageDrawable(drawable);
        //結果画像にアニメーションを付ける
        colorsImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.a3));
        colorsMusic.start();
        colorPreference = preferences.getString(resultColor,null);
        colorCount = Integer.parseInt(colorPreference);
        colorCount -= 1;
        //変更した値を保存
        editor.putString(resultColor,String.valueOf(colorCount));
        editor.commit();
    }

    //くじ引きへ戻るボタンを押下した際の処理
    public boolean onLongClick(View v){
        if(v == backButton){
            //参加人数リストが0か判定する
            if(allMembersList.size() != 0){
                //0でなければMain2へ値を渡す
                intent = new Intent(getApplicationContext(),LotteryActivity.class);
                intent.putIntegerArrayListExtra("allMembersList",allMembersList);
            }else {
                //0ならば終了画面へ遷移
                intent = new Intent(getApplicationContext(), EndActivity.class);
            }
            startActivityForResult(intent,0);
            finish();
        }
        return true;
    }

    //端末の戻るボタンを無効化
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
    public void onResume(){
        super.onResume();
        Log.d("ColorsActivity","onResume()呼び出し");
        colorsMusic.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("ColorsActivity","onPause()呼び出し");
        if(colorsMusic.isPlaying()){
            colorsMusic.pause();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("RedActivity","onDestroy()呼び出し");
        if(colorsMusic != null){
            colorsMusic.release();
            colorsMusic = null;
        }
        //表示された画像、ボタンを破棄
        backButton.setOnLongClickListener(null);
        backImage.setImageDrawable(null);
        colorsImage.setImageDrawable(null);
    }
}
