package com.deguffroy.adrien.moodtracker.controller;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deguffroy.adrien.moodtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout mRelativeLayout;
    private ImageView mMoodImage;
    private ImageView mAddBtn;
    private ImageView mHistBtn;

    private int mCurrentMood = 3;

    private static final String PREFS = "PREFS";
    private SharedPreferences mPreferences;

    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD";

    private GestureDetector mDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViews();
        this.retrievePreferences();

        mAddBtn.setOnClickListener(this);
        mHistBtn.setOnClickListener(this);
        mAddBtn.setTag(0);
        mHistBtn.setTag(1);

        // get the gesture detector
        mDetector = new GestureDetector(this, new MyGestureListener());

        // Add a touch listener to the view
        // The touch listener passes all its events on to the gesture detector
        mRelativeLayout.setOnTouchListener(touchListener);
    }

    @Override
    public void onClick(View v) {
        int button = (int) v.getTag();

        if (button == 0){
            // If Add comment button pressed

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Commentaire");
            final EditText edittext = new EditText(this);
            edittext.setHint("Entrez votre commentaire");
            edittext.requestFocus();
            builder.setView(edittext)
                    .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when click 'Valider'
                        }
                    })
                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when click 'Annuler'
                        }
                    })
                    .create()
                    .show();
        }else{
            // If History button pressed
            Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(historyActivityIntent);
        }
    }

    private void findViews(){
        mRelativeLayout = findViewById(R.id.activity_main_root);
        mMoodImage = findViewById(R.id.activity_main_image_view);
        mAddBtn = findViewById(R.id.activity_main_add_btn);
        mHistBtn = findViewById(R.id.activity_main_history_btn);
    }

    private void retrievePreferences(){
        mPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        mCurrentMood = mPreferences.getInt(PREF_KEY_MOOD,3);
        changeMood(mCurrentMood);
    }

    @Override
    protected void onPause(){
        super.onPause();

        mPreferences.edit().putInt(PREF_KEY_MOOD, mCurrentMood).apply();
    }

    // This touch listener passes everything on to the gesture detector.
    // That saves us the trouble of interpreting the raw touch events
    // ourselves.
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mDetector.onTouchEvent(event);

        }
    };

    // In the SimpleOnGestureListener subclass you should override
    // onDown and any other gesture that you want to detect.
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            if (e1.getY() < e2.getY()) {
                changeMood(--mCurrentMood);
                //Log.d("TAG", "MoodValue : " + mCurrentMood);
            }

            if (e1.getY() > e2.getY()) {
                changeMood(++mCurrentMood);
                //Log.d("TAG", "MoodValue : " + mCurrentMood);
            }
            return true;
        }
    }

    private void changeMood(int CurrentMood){
        Log.d("TAG", "MoodValue Entering method : " + mCurrentMood);
        if (CurrentMood < 0){
            CurrentMood = 0;
            mCurrentMood = 0;
        }
        else if (CurrentMood > 4){
            CurrentMood = 4;
            mCurrentMood = 4;
        }
        Log.d("TAG", "MoodValue After normalize : " + mCurrentMood);
        switch(CurrentMood){

            case 0 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                mMoodImage.setImageResource(R.drawable.smiley_sad);
                break;
            case 1 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                mMoodImage.setImageResource(R.drawable.smiley_disappointed);
                break;
            case 2 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                mMoodImage.setImageResource(R.drawable.smiley_normal);
                break;
            case 3 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                mMoodImage.setImageResource(R.drawable.smiley_happy);
                break;
            case 4 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                mMoodImage.setImageResource(R.drawable.smiley_super_happy);
                break;
        }
    }

}
