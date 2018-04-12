package com.deguffroy.adrien.moodtracker.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        viewHolder.updateWithMood(this.mMoods.get(position),position);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        if (mMoods == null){
            return 0;
        }else{
            return this.mMoods.size();
        }
    }
}
