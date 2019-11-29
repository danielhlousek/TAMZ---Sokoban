package com.example.sokoban33;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import static com.example.sokoban33.R.menu.menu_main;

public class MainActivity extends AppCompatActivity {

    int activeLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SokoView sv =  (SokoView)findViewById(R.id.sokoView);

        try {
            loadLevel();
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

        if(item.getItemId() == R.id.level1Item) {
            SokoView sv =  (SokoView)findViewById(R.id.sokoView);
            this.activeLevel = 1;
            try {
                this.loadLevel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sv.invalidate();

        }

        if(item.getItemId() == R.id.level2Item) {
            SokoView sv =  (SokoView)findViewById(R.id.sokoView);
            this.activeLevel = 2;
            try {
                this.loadLevel();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sv.invalidate();

        }
        return super.onOptionsItemSelected(item);
    }

    void loadLevel() throws Exception {
        SokoView sv =  (SokoView)findViewById(R.id.sokoView);
        String lvlName = "level"+activeLevel+".txt";
        StringBuilder sb = new StringBuilder();

        AssetManager am = getAssets();
        InputStream is = am.open("levels/"+lvlName);
        Scanner input = new Scanner(is);

        while (input.hasNextLine()) {
            String line = input.nextLine();
            sb.append(line);
        }
        input.close();
        String levelString = sb.toString();

        int[] levelArr = new int[100];

        String[] levelStrArr = levelString.split(",");

        for(int i = 0; i < 100;i++) {
            levelArr[i] = Integer.valueOf(levelStrArr[i]);
        }

        System.arraycopy( levelArr, 0, sv.level, 0, levelArr.length );
        System.arraycopy( levelArr, 0, sv.originalLevel, 0, levelArr.length );
    }

}
