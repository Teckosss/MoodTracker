package com.deguffroy.adrien.moodtracker.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.controller.RecyclerViewClickListener;
import com.deguffroy.adrien.moodtracker.model.Mood;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adrien Deguffroy on 07/04/2018.
 */

public class MoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.item_recycler_view_text) TextView mTextView;
    @BindView(R.id.item_recycler_view_image) ImageView mImageView;
    @BindView(R.id.item_recycler_layout) RelativeLayout mRelativeLayout;

    private RecyclerViewClickListener mListener;

    public MoodViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View view){
        mListener.onClick(view, getAdapterPosition());
    }

    public void updateWithMood(Mood moods){
        this.mTextView.setText(Integer.toString(moods.getMood()));
        if (!(moods.getMessageMood().equals(""))){
            mImageView.setImageResource(R.drawable.ic_comment_black_48px);
        }else{
            mImageView.setImageResource(0);
        }
    }
}
