package com.example.sokoban33;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import static com.example.sokoban33.R.menu.menu_main;

public class MainActivity extends AppCompatActivity {

    int activeLevel = 1;
    String activeLevelName;
    SharedPreferences pref;
    MediaPlayer loadLevel;
    SharedPreferences.Editor editor;
    Boolean continue_g = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLevel = MediaPlayer.create(this, R.raw.sfx_sounds_pause2_in);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        SokoView sv =  (SokoView)findViewById(R.id.sokoView);

        try {
            this.continue_g = getIntent().getBooleanExtra("continue", false);
            this.activeLevel = getIntent().getIntExtra("levelId", this.activeLevel);
            this.activeLevelName = getIntent().getStringExtra("levelName");
            loadLevel();
            sv.activeLevelName = this.activeLevel;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sv.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.resetItem) {
            SokoView sv =  (SokoView)findViewById(R.id.sokoView);
            int originalLevel[] = sv.originalLevel;
/*
            for(int i = 0;i<sv.level.length; i++) {
                sv.level[i] = sv.originalLevel[i];
            }*/

            System.arraycopy( originalLevel, 0, sv.level, 0, originalLevel.length );
            sv.invalidate();

        }

        if(item.getItemId() == R.id.homeItem) {
            /*SokoView sv =  (SokoView)findViewById(R.id.sokoView);
            this.activeLevel = 1;
            try {
                this.loadLevel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sv.invalidate();
            */
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void loadLevel() throws Exception {
        if (pref.getBoolean("sound", false)) {
            loadLevel.start();
        }
        if (!this.continue_g) {

        if (activeLevelName.equals("level")) {
            SokoView sv = (SokoView) findViewById(R.id.sokoView);
            String lvlName = "level" + activeLevel + ".txt";
            StringBuilder sb = new StringBuilder();

            AssetManager am = getAssets();
            InputStream is = am.open("levels/" + lvlName);
            Scanner input = new Scanner(is);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                sb.append(line);
            }
            input.close();
            String levelString = sb.toString();

            int[] levelArr = new int[100];

            String[] levelStrArr = levelString.split(",");

            for (int i = 0; i < 100; i++) {
                levelArr[i] = Integer.valueOf(levelStrArr[i]);
            }

            System.arraycopy(levelArr, 0, sv.level, 0, levelArr.length);
            System.arraycopy(levelArr, 0, sv.originalLevel, 0, levelArr.length);
        } else {

            SokoView sv = (SokoView) findViewById(R.id.sokoView);
            String lvlName = "level" + activeLevel + ".txt";
            StringBuilder sb = new StringBuilder();


            File file = new File("/storage/emulated/0/Android/data/com.example.sokoban33/files/sokoLevels", activeLevelName + activeLevel + ".txt");

            FileInputStream fis = new FileInputStream(file);
            int c;
            StringBuilder sb2 = new StringBuilder();

            while ((c = fis.read()) != -1) {
                sb2.append(String.valueOf((char) c));
            }


            String levelString = sb2.toString().replace("\n", "").replace("\r", "");


            int[] levelArr = new int[100];

            String[] levelStrArr = levelString.split(",");

            for (int i = 0; i < 100; i++) {
                levelArr[i] = Integer.valueOf(levelStrArr[i]);
            }

            System.arraycopy(levelArr, 0, sv.level, 0, levelArr.length);
            System.arraycopy(levelArr, 0, sv.originalLevel, 0, levelArr.length);

        }
    } else {
            SokoView sv =  (SokoView)findViewById(R.id.sokoView);
            String lvlName = "level1.txt";
            StringBuilder sb = new StringBuilder();

            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.pref.getString("continueLevel",""));

            String levelString = sb2.toString().replace("\n", "").replace("\r", "");

            int[] levelArr = new int[100];

            String[] levelStrArr = levelString.split(",");

            for (int i = 0; i < 100; i++) {
                levelArr[i] = Integer.valueOf(levelStrArr[i]);
            }

            System.arraycopy(levelArr, 0, sv.level, 0, levelArr.length);
            System.arraycopy(levelArr, 0, sv.originalLevel, 0, levelArr.length);
        }
    }

    public void updateScore() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        editor = pref.edit();
                SokoView sv =  (SokoView)findViewById(R.id.sokoView);
        int originalLevel[] = sv.level;
        StringBuilder sb = new StringBuilder();

        for (int v:
             originalLevel) {
            sb.append(String.valueOf(v));
            sb.append(",");
        }

        String lvlStr = sb.toString();
        lvlStr = lvlStr.substring(0, lvlStr.length()-1);
        editor.putString("continueLevel",lvlStr);
        editor.commit();

        System.arraycopy( originalLevel, 0, sv.level, 0, originalLevel.length );

    }
}
