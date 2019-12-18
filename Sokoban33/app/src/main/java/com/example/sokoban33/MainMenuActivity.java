package com.example.sokoban33;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.support.v4.content.ContextCompat.startActivity;

public class MainMenuActivity extends AppCompatActivity {

    public Switch toggleSound;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
     MediaPlayer menu_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toggleSound = (Switch) findViewById(R.id.soundSwitch);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        toggleSound.setChecked(pref.getBoolean("sound",false));

        menu_select = MediaPlayer.create(this, R.raw.sfx_menu_select3);

        String isCont = pref.getString("continueLevel","");
        if(!isCont.equals("") && isCont.contains("4")) {
            Button cb = (Button) findViewById(R.id.continueButton);
            cb.setVisibility(1);
        }
    }

    public void loadChooseLevelIntent(View view) {
        if(pref.getBoolean("sound",false) ) {
            menu_select.start();
        }
        Intent intent = new Intent(this, ChooseLevelActivity.class);
        startActivity(intent);
    }

    public void loadDownloadIntent(View view) {
        if(pref.getBoolean("sound",false) ) {
            menu_select.start();
        }
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    public void loadScoreIntent(View view) {
        if(pref.getBoolean("sound",false) ) {
            menu_select.start();
        }
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    private String readFile(File fl)
    {
        String myData = "";
        File myExternalFile = fl;
        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine + "\n";
                Log.d("lajna", strLine);
            }
            br.close();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }

    public void soundSwitched(View view) {

        Boolean swVal = this.toggleSound.isChecked();

        editor.putBoolean("sound",swVal);
        editor.commit();

    }


    public void loadContinue(View view) {

        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("continue", true);
        startActivity(intent);

    }
}
