package com.example.sokoban33;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.support.v4.content.ContextCompat.startActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void loadChooseLevelIntent(View view) {
        Intent intent = new Intent(this, ChooseLevelActivity.class);
        startActivity(intent);
    }

    public void loadDownloadIntent(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    public void loadScoreIntent(View view) {
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

}
