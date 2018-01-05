package com.example.sample.bukujibikimarshmallow;

/**
 * 人数を設定する画面
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    //ボイス
    private MediaPlayer datawoyomikomimasuka,huzakenaideyo;
    //「くじ引きスタート」ボタン
    private Button startButton,ikkatsuButton,goukeiButton,preferenceButton;
    //各色EditText
    private EditText redEdit,blueEdit,yellowEdit,greenEdit,purpleEdit,
                        blackEdit,orangeEdit,pinkEdit,whiteEdit,goukeiEdit,ikkatsuEdit;
    //参加人数を文字列から数値へ変換したものを代入する変数
    int redMember,blueMember,yellowMember,greenMember,
            purpleMember,blackMember,orangeMember,pinkMember,whiteMember,allMembers;
    //各色のString型を格納する変数
    String redMemberSt,blueMemberSt,yellowMemberSt,greenMemberSt,purpleMemberSt,
                blackMemberSt,orangeMemberSt,pinkMemberSt,whiteMemberSt,ikkatsuMemberSt;
    //背景画像
    private ImageView backImage;
    //参加人数のリスト
    ArrayList<Integer> allMembersList;
    //ACTION_UPのif文で使用する各色の加算した値を代入
    //red + blue
    int rbTotal;
    //red + blue + yellow
    int rbyTotal;
    //red + blue + yellow + green
    int rbygTotal;
    //red + blue + yellow + green + purple
    int rbygpTotal;
    //red + blue + yellow + green + purple + black
    int rbygpbTotal;
    //red + blue + yellow + green + purple + black + orange
    int rbygpboTotal;
    //red + blue + yellow + green + purple + black + orange + pink
    int rbygpbopTotal;
    //red + blue + yellow + green + purple + black + orange + pink + white
    int rbygpbopwTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Log.d("ConfigActivity","onCreate()呼び出し");
        //合計表示ボタンオブジェクト取得
        goukeiButton = (Button)findViewById(R.id.goukeibutton);
        goukeiButton.setOnClickListener(this);
        //Preference読み込みボタンのオブジェクト取得
        preferenceButton = (Button)findViewById(R.id.preferencebutton);
        preferenceButton.setOnLongClickListener(this);
        //くじ引きスタートボタンのid取得
        startButton = (Button)findViewById(R.id.startbutton);
        startButton.setOnClickListener(this);
        //人数一括設定ボタンのid取得
        ikkatsuButton = (Button)findViewById(R.id.ikkatsubutton);
        ikkatsuButton.setOnClickListener(this);
        //ボイス読み込み
        datawoyomikomimasuka = MediaPlayer.create(getApplicationContext(), R.raw.datawoyomikomimasuka);
        huzakenaideyo = MediaPlayer.create(getApplicationContext(), R.raw.tyottohuzakenaideyo);

        //各色のEditTextオブジェクト取得。初期値で8を設定
        redEdit = (EditText)findViewById(R.id.redmember);
        redEdit.setText("8");
        blueEdit = (EditText)findViewById(R.id.bluemember);
        blueEdit.setText("8");
        yellowEdit = (EditText)findViewById(R.id.yellowmember);
        yellowEdit.setText("8");
        greenEdit = (EditText)findViewById(R.id.greenmember);
        greenEdit.setText("8");
        purpleEdit = (EditText)findViewById(R.id.purplemember);
        purpleEdit.setText("8");
        blackEdit = (EditText)findViewById(R.id.blackmember);
        blackEdit.setText("8");
        orangeEdit = (EditText)findViewById(R.id.orangemember);
        orangeEdit.setText("8");
        pinkEdit = (EditText)findViewById(R.id.pinkmember);
        pinkEdit.setText("8");
        whiteEdit = (EditText)findViewById(R.id.whitemember);
        whiteEdit.setText("8");
        //人数一括設定のEditTextのid取得
        ikkatsuEdit = (EditText)findViewById(R.id.ikkatsuedit);
        //合計値のオブジェクト取得
        goukeiEdit = (EditText)findViewById(R.id.goukeiedit);
        //書き込み不可
        goukeiEdit.setFocusable(false);

        //各色のEditTextより取得した値をint型に変換
        redMember = Integer.parseInt(redEdit.getText().toString());
        blueMember = Integer.parseInt(blueEdit.getText().toString());
        yellowMember = Integer.parseInt(yellowEdit.getText().toString());
        greenMember = Integer.parseInt(greenEdit.getText().toString());
        purpleMember = Integer.parseInt(purpleEdit.getText().toString());
        blackMember = Integer.parseInt(blackEdit.getText().toString());
        orangeMember = Integer.parseInt(orangeEdit.getText().toString());
        pinkMember = Integer.parseInt(pinkEdit.getText().toString());
        whiteMember = Integer.parseInt(whiteEdit.getText().toString());

        //参加人数の合計値
        allMembers = redMember + blueMember + yellowMember
                        + greenMember+ purpleMember + blackMember
                             + orangeMember + pinkMember + whiteMember;
        //合計値を合計人数のEditTextにセット
        goukeiEdit.setText(String.valueOf(allMembers));
        //背景画像のidを取得
        backImage = (ImageView)findViewById(R.id.back);
    }

    public boolean onLongClick(View v2){
        //Preference読み込みボタン押下時の処理(長押しかつ隠しボタン)
        if(v2 == preferenceButton){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConfigActivity.this);
            //前回終了した時点の各色の残数を表示する
            redEdit.setText(preferences.getString("redMember","NOT"));
            blueEdit.setText(preferences.getString("blueMember","NOT"));
            pinkEdit.setText(preferences.getString("pinkMember","NOT"));
            greenEdit.setText(preferences.getString("greenMember","NOT"));
            blackEdit.setText(preferences.getString("blackMember","NOT"));
            whiteEdit.setText(preferences.getString("whiteMember","NOT"));
            yellowEdit.setText(preferences.getString("yellowMember","NOT"));
            purpleEdit.setText(preferences.getString("purpleMember","NOT"));
            orangeEdit.setText(preferences.getString("orangeMember","NOT"));
            //読み込みましたのメッセージを表示する
            Toast.makeText(getApplicationContext(),"前回データの続きを読み込みました",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void onClick(View v){
        //「くじ引きスタート」ボタン押下時の処理
        if(v == startButton){
            //「データを読み込みますか」ボイス再生
            datawoyomikomimasuka.start();
            //ダイアログを表示する
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("このデータを読み込みますか？");
            alert.setPositiveButton("Yes!",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Yes!ボタンが押下された時の処理
                    //各色の人数を空白入力の判定のためString型で取得
                    redMemberSt = redEdit.getText().toString();
                    blueMemberSt = blueEdit.getText().toString();
                    pinkMemberSt = pinkEdit.getText().toString();
                    greenMemberSt = greenEdit.getText().toString();
                    blackMemberSt = blackEdit.getText().toString();
                    whiteMemberSt = whiteEdit.getText().toString();
                    yellowMemberSt = yellowEdit.getText().toString();
                    purpleMemberSt = purpleEdit.getText().toString();
                    orangeMemberSt = orangeEdit.getText().toString();

                    //入力値が空白かでないか判定
                    if(redMemberSt.length() != 0 && blueMemberSt.length() != 0 &&
                            yellowMemberSt.length() != 0 && greenMemberSt.length() != 0 &&
                                purpleMemberSt.length() != 0 && blackMemberSt.length() != 0 &&
                                    orangeMemberSt.length() != 0 && pinkMemberSt.length() != 0 &&
                                        whiteMemberSt.length() != 0) {

                        //入力された数値を文字列からint型に変換
                        redMember = Integer.parseInt(redMemberSt);
                        blueMember = Integer.parseInt(blueMemberSt);
                        pinkMember = Integer.parseInt(pinkMemberSt);
                        greenMember = Integer.parseInt(greenMemberSt);
                        blackMember = Integer.parseInt(blackMemberSt);
                        whiteMember = Integer.parseInt(whiteMemberSt);
                        yellowMember = Integer.parseInt(yellowMemberSt);
                        purpleMember = Integer.parseInt(purpleMemberSt);
                        orangeMember = Integer.parseInt(orangeMemberSt);

                        //参加人数の合計値
                        allMembers = redMember + blueMember + yellowMember
                                        + greenMember + purpleMember + blackMember
                                                + orangeMember + pinkMember + whiteMember;

                        //リストに参加人数分の値を1から順に格納
                        allMembersList = new ArrayList<>();
                        for (int i = 1; i <= allMembers; i++) {
                            allMembersList.add(i);
                        }
                        //上記のリストをシャッフルする
                        Collections.shuffle(allMembersList);

                        //ACTION_UPのif文で使用する各色の加算した値を代入
                        rbTotal = redMember + blueMember;
                        rbyTotal = rbTotal + yellowMember;
                        rbygTotal = rbyTotal + greenMember;
                        rbygpTotal = rbygTotal + purpleMember;
                        rbygpbTotal = rbygpTotal + blackMember;
                        rbygpboTotal = rbygpbTotal + orangeMember;
                        rbygpbopTotal = rbygpboTotal + pinkMember;
                        rbygpbopwTotal = rbygpbopTotal + whiteMember;

                        /* Preferenceへ各種色の値を保存 */
                        //各色のEditTextの値をPreferenceへ保存
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConfigActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        //設定した数値をString型で保存
                        editor.putString("redMember",String.valueOf(redMember));
                        editor.putString("blueMember",String.valueOf(blueMember));
                        editor.putString("pinkMember",String.valueOf(pinkMember));
                        editor.putString("greenMember",String.valueOf(greenMember));
                        editor.putString("blackMember",String.valueOf(blackMember));
                        editor.putString("whiteMember",String.valueOf(whiteMember));
                        editor.putString("yellowMember",String.valueOf(yellowMember));
                        editor.putString("purpleMember",String.valueOf(purpleMember));
                        editor.putString("orangeMember",String.valueOf(orangeMember));
                        //Main2でのif文での判定用
                        editor.putString("redMemberDecision",redMemberSt);
                        editor.putString("rbTotal",String.valueOf(rbTotal));
                        editor.putString("rbyTotal",String.valueOf(rbyTotal));
                        editor.putString("rbygTotal",String.valueOf(rbygTotal));
                        editor.putString("rbygpTotal",String.valueOf(rbygpTotal));
                        editor.putString("rbygpbTotal",String.valueOf(rbygpbTotal));
                        editor.putString("rbygpboTotal",String.valueOf(rbygpboTotal));
                        editor.putString("rbygpbopTotal",String.valueOf(rbygpbopTotal));
                        editor.putString("rbygpbopwTotal",String.valueOf(rbygpbopwTotal));
                        editor.commit();
                        //くじ引き実行画面(Main2Activity)に遷移する際、値を渡す
                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putIntegerArrayListExtra("allMembersList", allMembersList);
                        startActivityForResult(intent,0);
                        finish();
                    }else{
                        //未入力項目がある場合、メッセージを表示する
                        Toast.makeText(getApplicationContext(),"未入力項目があります",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //Noボタンが押下された時の処理
            alert.setNegativeButton("No",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    //Noを選択時のボイス再生
                    huzakenaideyo.start();
                    //メッセージを表示する
                    Toast.makeText(getApplicationContext(),"やり直し‼︎‼︎‼︎",Toast.LENGTH_SHORT).show();
                }
            });
            alert.show();

            //人数一括設定ボタンを押下した際の処理
        }else if(v == ikkatsuButton){
            //人数一括設定の値を全色のEditTextにセットする
            ikkatsuMemberSt = ikkatsuEdit.getText().toString();
            redEdit.setText(ikkatsuMemberSt);
            blueEdit.setText(ikkatsuMemberSt);
            pinkEdit.setText(ikkatsuMemberSt);
            blackEdit.setText(ikkatsuMemberSt);
            greenEdit.setText(ikkatsuMemberSt);
            whiteEdit.setText(ikkatsuMemberSt);
            yellowEdit.setText(ikkatsuMemberSt);
            purpleEdit.setText(ikkatsuMemberSt);
            orangeEdit.setText(ikkatsuMemberSt);

            //合計人数計算ボタン
        }else if(v == goukeiButton){
            //入力値が空白か判定
            if(redEdit.length() != 0 && blueEdit.length() != 0 && yellowEdit.length() != 0 &&
                    greenEdit.length() != 0 && purpleEdit.length() != 0 && blackEdit.length() != 0 &&
                        orangeEdit.length() != 0 && pinkEdit.length() != 0 && whiteEdit.length() != 0) {

                //int型に変換
                redMember = Integer.parseInt(redEdit.getText().toString());
                blueMember = Integer.parseInt(blueEdit.getText().toString());
                pinkMember = Integer.parseInt(pinkEdit.getText().toString());
                greenMember = Integer.parseInt(greenEdit.getText().toString());
                blackMember = Integer.parseInt(blackEdit.getText().toString());
                whiteMember = Integer.parseInt(whiteEdit.getText().toString());
                yellowMember = Integer.parseInt(yellowEdit.getText().toString());
                purpleMember = Integer.parseInt(purpleEdit.getText().toString());
                orangeMember = Integer.parseInt(orangeEdit.getText().toString());

                //参加人数の合計値
                allMembers = redMember + blueMember + yellowMember
                                + greenMember + purpleMember + blackMember
                                        + orangeMember + pinkMember + whiteMember;

                goukeiEdit.setText(String.valueOf(allMembers));

            }else{
                Toast.makeText(getApplicationContext(),"未入力項目があります",Toast.LENGTH_SHORT).show();
            }
        }
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
    protected void onDestroy(){
        super.onDestroy();
        Log.d("ConfigActivity","onDestroy()呼び出し");
        if(datawoyomikomimasuka != null) {
            //リソース解放
            datawoyomikomimasuka.release();
            //MediaPlayerを破棄
            datawoyomikomimasuka = null;
        }
        if(huzakenaideyo != null) {
            huzakenaideyo.release();
            huzakenaideyo = null;
        }
        //EditTextを破棄
        redEdit.setText(null);
        blueEdit.setText(null);
        yellowEdit.setText(null);
        greenEdit.setText(null);
        purpleEdit.setText(null);
        blackEdit.setText(null);
        orangeEdit.setText(null);
        pinkEdit.setText(null);
        whiteEdit.setText(null);
        //ボタンを破棄
        startButton.setOnClickListener(null);
        preferenceButton.setOnLongClickListener(null);
        //背景画像を破棄
        backImage.setImageDrawable(null);
    }
}
