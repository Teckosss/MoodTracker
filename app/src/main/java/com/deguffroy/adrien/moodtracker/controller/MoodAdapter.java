package com.deguffroy.adrien.moodtracker.controller;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Color;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.model.Mood;

import java.util.List;

/**
 * Created by Adrien Deguffroy on 03/04/2018.
 */

public class MoodAdapter extends ArrayAdapter<Mood> {



    public MoodAdapter(Context context, List<Mood> mood) {
        super(context, 0, mood);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listview,parent, false);
        }

        HOFViewHolder viewHolder = (HOFViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new HOFViewHolder();
            viewHolder.layout =  convertView.findViewById(R.id.layout);
            viewHolder.name =  convertView.findViewById(R.id.name);
            viewHolder.image =  convertView.findViewById(R.id.image);
            viewHolder.listview =  convertView.findViewById(R.id.activity_history_list_view);
            convertView.setTag(viewHolder);
        }

        Mood users = getItem(position);
        int mood = users.getMood();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int totalHeight = parent.getHeight();
        viewHolder.layout.setMinimumHeight(totalHeight / getCount());
        switch (mood){
            case 0:
                params.weight = 80;
                viewHolder.layout.setLayoutParams(params);
                viewHolder.layout.setBackgroundResource(R.color.faded_red);
                break;
            case 1:
                params.weight = 60;
                viewHolder.layout.setLayoutParams(params);
                viewHolder.layout.setBackgroundResource(R.color.warm_grey);
                break;
            case 2:
                params.weight = 40;
                viewHolder.layout.setLayoutParams(params);
                viewHolder.layout.setBackgroundResource(R.color.cornflower_blue_65);
                break;
            case 3:
                params.weight = 20;
                viewHolder.layout.setLayoutParams(params);
                viewHolder.layout.setBackgroundResource(R.color.light_sage);
                break;
            case 4:
                params.weight = 0;
                viewHolder.layout.setLayoutParams(params);
                viewHolder.layout.setBackgroundResource(R.color.banana_yellow);
                break;
        }
        viewHolder.name.setText(users.getMood()+"");
        if (!(users.getMessageMood().equals(""))){
            viewHolder.image.setImageResource(R.drawable.ic_comment_black_48px);
        }else{
            viewHolder.image.setImageResource(0);
        }


        return convertView;
    }

    private class HOFViewHolder{
        public TextView name;
        public ImageView image;
        public ListView listview;
        public LinearLayout layout;
    }
}
