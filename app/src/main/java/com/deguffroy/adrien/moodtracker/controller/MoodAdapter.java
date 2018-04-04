package com.deguffroy.adrien.moodtracker.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private Activity activity;

    public MoodAdapter(Context context, List<Mood> score) {
        super(context, 0, score);
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
            viewHolder.date =  convertView.findViewById(R.id.date);
            viewHolder.image =  convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }

        Mood users = getItem(position);
        int mood = users.getMood();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        viewHolder.date.setText(users.getDateMood());
        if (!(users.getMessageMood().equals(""))){
            viewHolder.image.setImageResource(R.drawable.ic_comment_black_48px);
        }else{
            viewHolder.image.setImageResource(0);
        }


        return convertView;
    }

    private class HOFViewHolder{
        public TextView name;
        public TextView date;
        public ImageView image;
        public LinearLayout layout;
    }
}
