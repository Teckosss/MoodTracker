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
import android.widget.Toast;

import com.deguffroy.adrien.moodtracker.R;
import com.deguffroy.adrien.moodtracker.model.Mood;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout mRelativeLayout;
    private ImageView mMoodImage;
    private ImageView mAddBtn;
    private ImageView mHistBtn;

    private int mCurrentMood = 3;
    private ArrayList<Mood> mMoods;

    private static final String PREFS = "PREFS";
    private SharedPreferences mPreferences;

    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD";
    public static final String PREF_KEY_MESSAGE = "PREF_KEY_MESSAGE";

    private GestureDetector mDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.findViews();
        this.retrievePreferences();
        this.configureButton();
        this.configureGesture();

    }

    @Override
    public void onClick(View v) {
        int button = (int) v.getTag();

        if (button == 0){
            // If Add comment button pressed

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Commentaire");
            final EditText edittext = new EditText(this);
            if (mPreferences.getString(PREF_KEY_MESSAGE,null) != null){
                edittext.setText(mPreferences.getString(PREF_KEY_MESSAGE,""));
            }else{
                edittext.setHint("Entrez votre commentaire");
                edittext.requestFocus();
            }
            builder.setView(edittext)
                    .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when click 'Valider'
                                mPreferences.edit().putString(PREF_KEY_MESSAGE,edittext.getText().toString()).apply();
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

    private void configureButton(){
        mAddBtn.setOnClickListener(this);
        mHistBtn.setOnClickListener(this);
        mAddBtn.setTag(0);
        mHistBtn.setTag(1);
    }

    private void configureGesture(){
        // get the gesture detector
        mDetector = new GestureDetector(this, new MyGestureListener());

        // Add a touch listener to the view
        // The touch listener passes all its events on to the gesture detector
        mRelativeLayout.setOnTouchListener(touchListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mPreferences.edit().putInt(PREF_KEY_MOOD, mCurrentMood).apply();

        Gson gson = new Gson();
        String json = mPreferences.getString("Mood","");
        Type type = new TypeToken<ArrayList<Mood>>(){}.getType();
        mMoods = gson.fromJson(json,type);
        if (mMoods == null) {
            mMoods = new ArrayList<>();
            createAndSaveMood(mCurrentMood,mPreferences.getString(PREF_KEY_MESSAGE,""));
        }else{
            int listSize = mMoods.size();
            for (int i = 0; i<listSize; i++){
                //Log.i("Index : " + i + " / " + listSize, mMoods.get(i)+"");
                if (mMoods.get(i).getDateMood().equals(todayDate())){
                    //Log.i("Break", "Break de la boucle la date d'aujourdhui à été trouvée !");
                    /*if (listSize > 6){ // FOR TESTING ONLY
                        Toast.makeText(this, "Il y a 7 éléments !", Toast.LENGTH_SHORT).show();
                        mMoods.remove(0);
                        createAndSaveMood(mCurrentMood,mPreferences.getString(PREF_KEY_MESSAGE,""));
                    }else{
                        createAndSaveMood(mCurrentMood,mPreferences.getString(PREF_KEY_MESSAGE,""));
                    }*/
                    modifyAndSaveMood(i,mCurrentMood,mPreferences.getString(PREF_KEY_MESSAGE,null));
                    break;
                }else if (i >= listSize - 1){
                    //Log.i("NoBreak", "La date n'a pas été trouvée");
                    if (listSize > 6){
                        Toast.makeText(this, "Il y a 7 éléments !", Toast.LENGTH_SHORT).show();
                        mMoods.remove(0);
                    }
                    createAndSaveMood(mCurrentMood,mPreferences.getString(PREF_KEY_MESSAGE,""));
                }

            }
        }
    }

    private void createAndSaveMood(int moodToSave, String messageToSave){
        Gson gson = new Gson();
            Mood mood = new Mood(moodToSave,todayDate(),messageToSave);
            mMoods.add(mood);
            String jsonMood = gson.toJson(mMoods);
            mPreferences.edit().putString("Mood",jsonMood).apply();
    }

    private void modifyAndSaveMood(int indexMood, int moodToSave, String messageToSave){
        Gson gson = new Gson();
        mMoods.get(indexMood).setMood(moodToSave);
        mMoods.get(indexMood).setMessageMood(messageToSave);
        String jsonMood = gson.toJson(mMoods);
        mPreferences.edit().putString("Mood",jsonMood).apply();
    }

    private String todayDate(){
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(Calendar.getInstance().getTime());
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
            }

            if (e1.getY() > e2.getY()) {
                changeMood(++mCurrentMood);
            }
            return true;
        }
    }

    private void changeMood(int currentMood){
        //Log.d("TAG", "MoodValue Entering method : " + mCurrentMood);
        if (currentMood < 0){
            currentMood = 0;
            mCurrentMood = currentMood;
        }
        else if (currentMood > 4){
            currentMood = 4;
            mCurrentMood = currentMood;
        }
        //Log.d("TAG", "MoodValue After normalize : " + mCurrentMood);
        switch(currentMood){

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
