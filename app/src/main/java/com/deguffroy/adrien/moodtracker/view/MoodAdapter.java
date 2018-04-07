package com.deguffroy.adrien.moodtracker.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.controller.RecyclerViewClickListener;
import com.deguffroy.adrien.moodtracker.model.Mood;

import java.util.ArrayList;

/**
 * Created by Adrien Deguffroy on 07/04/2018.
 */

public class MoodAdapter extends RecyclerView.Adapter<MoodViewHolder> {

    // FOR DATA
    private ArrayList<Mood> mMoods;
    private RecyclerViewClickListener mListener;

    // CONSTRUCTOR
    public MoodAdapter(ArrayList<Mood> mMoods) {
        this.mMoods = mMoods;
    }

    public void setClickListener(RecyclerViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recyclerview, parent, false);

        return new MoodViewHolder(view, mListener);
    }

    // UPDATE VIEW HOLDER WITH A MOOD
    @Override
    public void onBindViewHolder(MoodViewHolder viewHolder, int position) {
        viewHolder.updateWithMood(this.mMoods.get(position));
        int height = Resources.getSystem().getDisplayMetrics().heightPixels / getItemCount();
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = height;
        switch (mMoods.get(position).getMood()){
            case 0:
                params.width = width * 20 / 100;
                viewHolder.itemView.setBackgroundResource(R.color.faded_red);
                break;
            case 1:
                params.width = width * 40 / 100;
                viewHolder.itemView.setBackgroundResource(R.color.warm_grey);
                break;
            case 2:
                params.width = width * 60 / 100;
                viewHolder.itemView.setBackgroundResource(R.color.cornflower_blue_65);
                break;
            case 3:
                params.width = width * 80 / 100;
                viewHolder.itemView.setBackgroundResource(R.color.light_sage);
                break;
            case 4:
                params.width = width;
                viewHolder.itemView.setBackgroundResource(R.color.banana_yellow);
                break;
        }
        viewHolder.itemView.setLayoutParams(params);

    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mMoods.size();
    }
}
