package com.example.sokoban33;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.LevelListDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
                intent.putExtra("levelName", item.name);
                startActivity(intent);
            }
        });

        try {
            fillList("level");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void fillList(String levelName) throws IOException {

        AssetManager am = getAssets();

        int i = 1;
        InputStream is;
        LevelItem currentItem;

        while (true) {

            try {
                is = am.open("levels/" + levelName + i + ".txt");

                currentItem = new LevelItem(i, levelName);

                listItems.add(currentItem);
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                //Log.d("LEVEL count", "LEVEL COUNT: " + i + " - " + e.getMessage());
                break;
            }

            i++;

        }


        File f2 = new File("/storage/emulated/0/Android/data/com.example.sokoban33/files/sokoLevels", "");
        File[] list = f2.listFiles();
        int fc = list.length;

        for(int j = 0; j < fc; j++) {
            String fn = list[j].getName();
            fn = fn.replace("download","").replace(".txt","");
            currentItem = new LevelItem(Integer.parseInt(fn), "download");

            listItems.add(currentItem);
            adapter.notifyDataSetChanged();
        }
        /*
        i = 1;
        while(true) {
            try {
                File file = new File("/storage/emulated/0/Android/data/com.example.sokoban33/files/sokoLevels", "download"+i+".txt");

                File f2 = new File("/storage/emulated/0/Android/data/com.example.sokoban33/files/sokoLevels", "");
                File[] list = f2.listFiles();
                int fc = list.length;

                for(int j = 0; j < fc; j++) {
                    String fn = list[j].getName();
                    fn = fn.replace("download","").replace(".txt","");
                    currentItem = new LevelItem(Integer.parseInt(fn), "download");

                    listItems.add(currentItem);
                    adapter.notifyDataSetChanged();
                }

                FileInputStream fis = new FileInputStream(file);
                int c;
                StringBuilder sb = new StringBuilder();

                while ((c = fis.read()) != -1) {
                    sb.append(String.valueOf((char) c));
                }

                Log.d("LEVEL count", "LEVEL COUNTT: " + i );

                currentItem = new LevelItem(i, "download");

                listItems.add(currentItem);
                adapter.notifyDataSetChanged();
            }

            catch (Exception e) {
                Log.d("LEVEL count", "BREJK" );
                break;
            }
            i++;
        }*/
    }




}
