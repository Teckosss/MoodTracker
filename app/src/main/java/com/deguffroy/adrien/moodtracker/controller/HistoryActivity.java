package com.deguffroy.adrien.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.model.Mood;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {

    private ListView mListMood;

    private ArrayList<Mood> mMoods;

    private static final String PREFS = "PREFS";
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.retrievePreferences();
        this.findViews();
        this.showMoods();
    }

    private void retrievePreferences(){
        mPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    private void findViews(){
        mListMood = findViewById(R.id.activity_history_list_view);
    }

    private void showMoods(){
        Gson gson = new Gson();
        String json = mPreferences.getString("Mood","");
        Type type = new TypeToken<ArrayList<Mood>>(){}.getType();
        mMoods = gson.fromJson(json,type);

        Toast.makeText(this, "Size : " + mMoods.size() ,Toast.LENGTH_SHORT).show();

        MoodAdapter adapter = new MoodAdapter(HistoryActivity.this, mMoods);
        mListMood.setAdapter(adapter);
    }

}
