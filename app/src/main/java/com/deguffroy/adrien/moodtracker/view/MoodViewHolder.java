package com.deguffroy.adrien.moodtracker.view;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.model.Mood;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Adrien Deguffroy on 07/04/2018.
 */

public class MoodViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.item_recycler_view_text) TextView mTextView;
    @BindView(R.id.item_recycler_view_image) ImageView mImageView;
    @BindView(R.id.item_recycler_layout) RelativeLayout mRelativeLayout;

    public MoodViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void updateWithMood(Mood moods, int position){
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = (Resources.getSystem().getDisplayMetrics().heightPixels - getStatusBarHeight()) / 7;
        switch (position){
            case 0:
                this.mTextView.setText("Il y a une semaine");
                break;
            case 1:
                this.mTextView.setText("Il y a six jours");
                break;
            case 2:
                this.mTextView.setText("Il y a cinq jours");
                break;
            case 3:
                this.mTextView.setText("Il y a quatre jours");
                break;
            case 4:
                this.mTextView.setText("Il y a trois jours");
                break;
            case 5:
                this.mTextView.setText("Avant-hier");
                break;
            case 6:
                this.mTextView.setText("Hier");
                break;
        }
        switch (moods.getMood()){
            case 0:
                params.width = width * 20 / 100;
                this.itemView.setBackgroundResource(R.color.faded_red);
                break;
            case 1:
                params.width = width * 40 / 100;
                this.itemView.setBackgroundResource(R.color.warm_grey);
                break;
            case 2:
                params.width = width * 60 / 100;
                this.itemView.setBackgroundResource(R.color.cornflower_blue_65);
                break;
            case 3:
                params.width = width * 80 / 100;
                this.itemView.setBackgroundResource(R.color.light_sage);
                break;
            case 4:
                params.width = width;
                this.itemView.setBackgroundResource(R.color.banana_yellow);
                break;
        }
        this.itemView.setLayoutParams(params);
        if (!(moods.getMessageMood().equals(""))){
            mImageView.setImageResource(R.drawable.ic_comment_black_48px);
        }else{
            mImageView.setImageResource(0);
        }
    }
}
