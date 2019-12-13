package com.example.sokoban33;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Scanner;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by kru13 on 12.10.16.
 */

public class SokoView extends View{

    Bitmap[] bmp;

    int lx = 10;
    int ly = 10;

    int width;
    int height;

    public int level[] = new int [100];
    public int originalLevel[] = new int [100];

    public int score = 0;

    public TextView myScore;

    public int activeLevelName;

    DatabaseHelper myDb;

    public SokoView(Context context) {
        super(context);
        init(context);
        myDb = new DatabaseHelper(getContext());
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        myDb = new DatabaseHelper(getContext());
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        myDb = new DatabaseHelper(getContext());
    }

    void init(Context context) {
        bmp = new Bitmap[6];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

    }

    //do priste pohyb a tlacit bednu
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
            {
                int deviceHeight = this.getHeight();
                int deviceWidth = this.getWidth();
                int xRange = deviceHeight/2; //1280
                int up = (deviceHeight-xRange)/2;
                int down = deviceHeight-up;

                float xDown = event.getX();
                float yDown = event.getY();
                String smer ="";

                if(yDown > 0 && yDown < up) {
                    smer = "up";
                } else if( yDown > down && yDown < deviceHeight) {
                    smer = "down";
                } else if(yDown > up && yDown < down && xDown > deviceWidth/2) {
                    smer = "right";
                } else if(yDown > up && yDown < down && xDown < deviceWidth/2) {
                    smer = "left";
                }

                int playerIndex = getPlayerIndex();

                if(canMove(smer, playerIndex) && !boxokOn(smer, playerIndex)) {
                    if(!boxOn(smer, playerIndex)) {
                        movePlayer(smer, playerIndex);
                        updateScore();
                    } else {
                        if(canMoveBox(smer, playerIndex)) {
                            moveBox(smer, playerIndex);
                            movePlayer(smer, playerIndex);

                            updateScore();
                        }
                    }
                    if(win()) {
                        showAddItemDialog(getContext());
                    }
                }
                this.invalidate();
                break;
            }
        }
        return true;
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Your score is " + String.valueOf(score))
                .setMessage("Enter your name:")
                .setView(taskEditText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        boolean isInserted = myDb.insertData(task, score, activeLevelName);
                        if(isInserted) {
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getContext(), HighScoresActivity.class);
                            startActivity(getContext(), intent, new Bundle());

                        } else {
                            Toast.makeText(getContext(), "Saving failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void updateScore() {
        this.score++;
        myScore = (TextView)this.getRootView().findViewById(R.id.myScore);
        if(myScore != null) {
            myScore.setText("Score: " + String.valueOf(score));
        }
    }

    private boolean win() {
        boolean winner = true;
        for(int i =0;i<this.originalLevel.length;i++) {
            if(this.originalLevel[i] == 3) {
                if(this.level[i] != 2) {
                    winner = false;
                }
            }
        }
        return winner;
    }

    private void moveBox(String smer, int playerIndex) {
        switch(smer) {
            case "right": {
                if(originalLevel[playerIndex+1] != 3) {
                    level[playerIndex+1] = 0;
                    level[playerIndex+2] = 2;
                } else {
                    level[playerIndex+1] = 3;
                    level[playerIndex+2] = 2;
                }
                break;
            }
            case "left": {
                if(originalLevel[playerIndex-1] != 3) {
                    level[playerIndex-1] = 0;
                    level[playerIndex-2] = 2;
                } else {
                    level[playerIndex-1] = 3;
                    level[playerIndex-2] = 2;
                }
                break;
            }
            case "up": {
                if(originalLevel[playerIndex-10] != 3) {
                    level[playerIndex-10] = 0;
                    level[playerIndex-20] = 2;
                } else {
                    level[playerIndex-10] = 3;
                    level[playerIndex-20] = 2;
                }
                break;
            }
            case "down": {
                if(originalLevel[playerIndex+10] != 3) {
                    level[playerIndex+10] = 0;
                    level[playerIndex+20] = 2;
                } else {
                    level[playerIndex+10] = 3;
                    level[playerIndex+20] = 2;
                }
                break;
            }
        }
    }

    private void movePlayer(String smer, int playerIndex) {
        switch(smer) {
            case "right": {
                if(originalLevel[playerIndex] != 3) {
                    level[playerIndex] = 0;
                    level[playerIndex+1] = 4;
                } else {
                    level[playerIndex] = 3;
                    level[playerIndex+1] = 4;
                }
                break;
            }
            case "left": {
                if(originalLevel[playerIndex] != 3) {
                    level[playerIndex] = 0;
                    level[playerIndex-1] = 4;
                } else {
                    level[playerIndex] = 3;
                    level[playerIndex-1] = 4;
                }
                break;
            }
            case "up": {
                if(originalLevel[playerIndex] != 3) {
                    level[playerIndex] = 0;
                    level[playerIndex-10] = 4;
                } else {
                    level[playerIndex] = 3;
                    level[playerIndex-10] = 4;
                }
                break;
            }
            case "down": {
                if(originalLevel[playerIndex] != 3) {
                    level[playerIndex] = 0;
                    level[playerIndex+10] = 4;
                } else {
                    level[playerIndex] = 3;
                    level[playerIndex+10] = 4;
                }
                break;
            }
        }
    }

    private int getPlayerIndex() {
        for(int i = 0; i < level.length;i++) {
            if(level[i] == 4) {
                return i;
            }
        }
        return -1;
    }

    private boolean canMove(String smer, int playerIndex) {
        switch(smer) {
            case "right": {

                if(level[playerIndex+1] == 0 || level[playerIndex+1] == 2 || level[playerIndex+1] == 3 || level[playerIndex+1] == 5) {
                    return true;
                }
                return false;
            }
            case "left": {

                if(level[playerIndex-1] == 0 || level[playerIndex-1] == 2 || level[playerIndex-1] == 3 || level[playerIndex-1] == 5) {
                    return true;
                }
                return false;
            }
            case "up": {

                if(level[playerIndex-10] == 0 || level[playerIndex-10] == 2 || level[playerIndex-10] == 3 || level[playerIndex-10] == 5) {
                    return true;
                }
                return false;
            }
            case "down": {

                if(level[playerIndex+10] == 0 || level[playerIndex+10] == 2 || level[playerIndex+10] == 3 || level[playerIndex+10] == 5) {
                    return true;
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    private boolean boxOn(String smer, int playerIndex) {
        switch(smer) {
            case "right": {

                if(level[playerIndex+1] == 2) {
                    return true;
                }
                return false;
            }
            case "left": {

                if(level[playerIndex-1] == 2) {
                    return true;
                }
                return false;
            }
            case "up": {

                if(level[playerIndex-10] == 2) {
                    return true;
                }
                return false;
            }
            case "down": {

                if(level[playerIndex+10] == 2) {
                    return true;
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    private boolean boxokOn(String smer, int playerIndex) {
        switch(smer) {
            case "right": {

                if(level[playerIndex+1] == 5) {
                    return true;
                }
                return false;
            }
            case "left": {

                if(level[playerIndex-1] == 5) {
                    return true;
                }
                return false;
            }
            case "up": {

                if(level[playerIndex-10] == 5) {
                    return true;
                }
                return false;
            }
            case "down": {

                if(level[playerIndex+10] == 5) {
                    return true;
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    private boolean goalOn(String smer, int playerIndex) {
        switch(smer) {
            case "right": {

                if(level[playerIndex+1] == 3) {
                    return true;
                }
                return false;
            }
            case "left": {

                if(level[playerIndex-1] == 3) {
                    return true;
                }
                return false;
            }
            case "up": {

                if(level[playerIndex-10] == 3) {
                    return true;
                }
                return false;
            }
            case "down": {

                if(level[playerIndex+10] == 3) {
                    return true;
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    private boolean canMoveBox(String smer, int playerIndex) {
        switch(smer) {
            case "right": {

                if(level[playerIndex+2] == 0 || level[playerIndex+2] == 3) {
                    return true;
                }
                return false;
            }
            case "left": {

                if(level[playerIndex-2] == 0 || level[playerIndex-2] == 3) {
                    return true;
                }
                return false;
            }
            case "up": {

                if(level[playerIndex-20] == 0 || level[playerIndex-20] == 3) {
                    return true;
                }
                return false;
            }
            case "down": {

                if(level[playerIndex+20] == 0 || level[playerIndex+20] == 3) {
                    return true;
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    public int[] getLevel() {
        return this.level;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / ly;
        height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(this.level != null) {
            for (int i = 0; i < lx; i++) {
                for (int j = 0; j < ly; j++) {
                    canvas.drawBitmap(bmp[level[i*10 + j]], null,
                            new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
                }
            }
        }
    }
}