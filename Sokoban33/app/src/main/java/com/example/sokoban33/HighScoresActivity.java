package com.example.sokoban33;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HighScoresActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    public TextView scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        myDb = new DatabaseHelper(this);
        scoreList = (TextView)findViewById(R.id.scoreList);

        loadData();
    }

    public void loadData() {
        Cursor res = myDb.getAllData();

        StringBuffer buffer = new StringBuffer();
        buffer.append("POS NAME SCORE LEVEL \n\r");
        while(res.moveToNext()) {
            buffer.append("#" + (Integer.valueOf(res.getPosition())+1) + "        " + res.getString(0) +"         "+ res.getString(1) +"          "+ res.getString(2) + "\n\r");
        }
        scoreList.setText(buffer.toString());
    }
}
