package com.example.sokoban33;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.LevelListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;

public class ChooseLevelActivity extends ListActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<LevelItem> listItems=new ArrayList<LevelItem>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    //ArrayAdapter<LevelItem> adapter;
    LevelItemAdapter adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;

    ListView listview;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_choose_level);

        adapter=new LevelItemAdapter(this,
                R.layout.level_item_row,
                listItems);
        setListAdapter(adapter);

        listview  = (ListView)findViewById(android.R.id.list);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LevelItem item = (LevelItem)parent.getItemAtPosition(position);
                Log.d("Item", "Item content: " + item.name + "levelId: " + item.id);

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("levelId", item.id);
                startActivity(intent);
            }
        });

        fillList("level");
    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void fillList(String levelName) {

        AssetManager am = getAssets();

        int i = 1;
        InputStream is;
        LevelItem currentItem;

        while(true) {

            try {
                is = am.open("levels/"+levelName+i+".txt");

                currentItem = new LevelItem(i, levelName);

                listItems.add(currentItem);
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                //Log.d("LEVEL count", "LEVEL COUNT: " + i + " - " + e.getMessage());
                break;
            }

            i++;

        }
    }




}
