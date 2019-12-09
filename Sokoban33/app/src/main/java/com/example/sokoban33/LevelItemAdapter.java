package com.example.sokoban33;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LevelItemAdapter extends ArrayAdapter<LevelItem> {

    Context context;
    int layoutResourceId;
    List<LevelItem> data = null;

    public LevelItemAdapter(Context context, int layoutResourceId, List<LevelItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LevelItem level = (LevelItem)getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.level_item_row, parent, false);
        }

        TextView levelTitle = (TextView) convertView.findViewById(R.id.level_item_row_name);
        levelTitle.setText(level.name + " " + level.id);
        return convertView;
    }


}
