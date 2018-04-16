package com.deguffroy.adrien.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.model.Mood;
import com.deguffroy.adrien.moodtracker.utils.ItemClickSupport;
import com.deguffroy.adrien.moodtracker.view.MoodAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

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
        this.configureOnClickRecyclerView();
    }

    // Retrieve SharedPreferences
    private void retrievePreferences(){
        mPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    // Display the moods history
    private void showMoods(){
        Gson gson = new Gson();
        String json = mPreferences.getString("Mood","");
        Type type = new TypeToken<ArrayList<Mood>>(){}.getType();
        mMoods = gson.fromJson(json,type);

        adapter = new MoodAdapter(mMoods);
        this.mListMood.setAdapter(this.adapter);
        this.mListMood.setLayoutManager(new LinearLayoutManager(this));
    }

    // Configure item click on RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mListMood, R.layout.item_recyclerview)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // Get mood message from adapter
                        String message = adapter.getUser(position).getMessageMood();
                        // Show result in a Toast
                        if (!(message.equals(""))){
                            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
