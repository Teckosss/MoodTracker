package com.deguffroy.adrien.moodtracker.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.model.Mood;
import com.deguffroy.adrien.moodtracker.view.MoodAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity implements RecyclerViewClickListener {

    @BindView(R.id.activity_history_recycler_view) RecyclerView mListMood;

    private ArrayList<Mood> mMoods;
    private MoodAdapter adapter;

    private static final String PREFS = "PREFS";
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        this.retrievePreferences();
        this.showMoods();
    }

    private void retrievePreferences(){
        mPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    private void showMoods(){
        Gson gson = new Gson();
        String json = mPreferences.getString("Mood","");
        Type type = new TypeToken<ArrayList<Mood>>(){}.getType();
        mMoods = gson.fromJson(json,type);

        this.adapter = new MoodAdapter(mMoods);
        this.mListMood.setAdapter(this.adapter);
        this.mListMood.setLayoutManager(new LinearLayoutManager(this));
        this.adapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position){
        String message = mMoods.get(position).getMessageMood();
        if (!(message.equals(""))){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

}
