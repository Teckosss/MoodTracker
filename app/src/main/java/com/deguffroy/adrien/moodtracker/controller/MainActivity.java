package com.deguffroy.adrien.moodtracker.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

    @BindView(R.id.activity_main_root) RelativeLayout mRelativeLayout;
    @BindView(R.id.activity_main_image_view) ImageView mMoodImage;
    @BindView(R.id.activity_main_add_btn) ImageView mAddBtn;
    @BindView(R.id.activity_main_history_btn) ImageView mHistBtn;
    @BindView(R.id.activity_main_share_btn) ImageView mShareBtn;

    private int mCurrentMood = 3;
    private int mImageMood;
    private String mCurrentMessage = "";
    private ArrayList<Mood> mMoods;
    private ArrayList<Mood> mMoodsCurrent;

    private static final String PREFS = "PREFS";
    private SharedPreferences mPreferences;

    public static final String PREF_KEY_MOOD = "PREF_KEY_MOOD";
    public static final String PREF_KEY_MESSAGE = "PREF_KEY_MESSAGE";

    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.retrievePreferences();
        this.configureButton();
        this.configureGesture();
    }

    // Manage click and does the corresponding action according to the tag
    @Override
    public void onClick(View v) {
        int button = (int) v.getTag();
        if (button == 0){  // If Add comment button pressed
            showCommentDialog();
        }else if (button == 1){  // If History button pressed
            Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(historyActivityIntent);
        }else{ // If Share button pressed
            shareMood(mImageMood);
        }
    }

    private void showCommentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Commentaire");
        final EditText edittext = new EditText(this);
        if (!(mPreferences.getString(PREF_KEY_MESSAGE,"").equals(""))){
            edittext.setText(mPreferences.getString(PREF_KEY_MESSAGE,""));
        }else{
            edittext.setHint("Entrez votre commentaire");
            edittext.requestFocus();
        }
        builder.setView(edittext)
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPreferences.edit().putString(PREF_KEY_MESSAGE,edittext.getText().toString()).apply();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    private void shareMood(int imageMood){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),imageMood);
        String path = getExternalCacheDir()+"/shareimage.jpg";
        java.io.OutputStream out = null;
        java.io.File file=new java.io.File(path);
        try { out = new java.io.FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path=file.getPath();
        Uri bmpUri = Uri.parse("file://"+path);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent,"Partager avec "));
    }

    //Each time we launch application check if we have already store a mood for today or if its a new day
    private void retrievePreferences(){
        mPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Mood>>(){}.getType();
        String jsonCurrent = mPreferences.getString("MoodCurrent","");
        mMoodsCurrent = gson.fromJson(jsonCurrent,type);
        if (mMoodsCurrent == null) {
            mMoodsCurrent = new ArrayList<>();
            mPreferences.edit().putInt(PREF_KEY_MOOD,3).apply();
            mPreferences.edit().putString(PREF_KEY_MESSAGE,"").apply();
            mCurrentMood = mPreferences.getInt(PREF_KEY_MOOD,3);
            mCurrentMessage = mPreferences.getString(PREF_KEY_MESSAGE,"");
            createAndSaveMood(mCurrentMood,mCurrentMessage, true);
        }else{
            int listSize = mMoodsCurrent.size();
            for (int i = 0; i<listSize; i++){ // TO SHOW LIST IN LOG
                Log.i("Index : " + i + " / " + listSize, mMoodsCurrent.get(i)+"");
            }
            mCurrentMood = mMoodsCurrent.get(0).getMood();
            mCurrentMessage = mMoodsCurrent.get(0).getMessageMood();
            if (mMoodsCurrent.get(0).getDateMood().equals(todayDate())){
                mPreferences.edit().putInt(PREF_KEY_MOOD,mCurrentMood).apply();
                mPreferences.edit().putString(PREF_KEY_MESSAGE,mCurrentMessage).apply();
            }else{
                manageMood(mCurrentMood,mCurrentMessage);
                mPreferences.edit().putInt(PREF_KEY_MOOD,3).apply();
                mPreferences.edit().putString(PREF_KEY_MESSAGE,"").apply();
                mCurrentMood = 3;
            }
        }
        changeMood(mCurrentMood);
    }

    //Assign a tag and a listener to each button to manage them easily
    private void configureButton(){
        mAddBtn.setOnClickListener(this);
        mHistBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mAddBtn.setTag(0);
        mHistBtn.setTag(1);
        mShareBtn.setTag(2);
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
        mCurrentMood = mPreferences.getInt(PREF_KEY_MOOD,3);
        mCurrentMessage = mPreferences.getString(PREF_KEY_MESSAGE,"");
        createAndSaveMood(mCurrentMood,mCurrentMessage, true);
    }

    // Manage the moods to remove the first index when a week is complete and save it
    private void manageMood(int mCurrentMoodToSave, String mCurrentMessageToSave){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Mood>>(){}.getType();

        String json = mPreferences.getString("Mood","");
        mMoods = gson.fromJson(json,type);
        if (mMoods == null) {
            mMoods = new ArrayList<>();
            createAndSaveMood(mCurrentMoodToSave,mCurrentMessageToSave, false);
        }else{
            int listSize = mMoods.size();
            if (listSize > 6){
                mMoods.remove(0);
            }
            createAndSaveMood(mCurrentMoodToSave,mCurrentMessageToSave,false);
        }
    }

    //Manage mood's save if currentmood (today) clear list and save object or add it to the list and save object
    private void createAndSaveMood(int moodToSave, String messageToSave, boolean currentMood){
        Gson gson = new Gson();
        Mood mood = new Mood(moodToSave,todayDate(),messageToSave);
        if (currentMood){
            mMoodsCurrent.clear();
            mMoodsCurrent.add(mood);
            String jsonMood = gson.toJson(mMoodsCurrent);
            mPreferences.edit().putString("MoodCurrent",jsonMood).apply();
        }else{
            mMoods.add(mood);
            String jsonMood = gson.toJson(mMoods);
            mPreferences.edit().putString("Mood",jsonMood).apply();
        }
    }

    //Return today's date
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
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() < e2.getY()) {
                changeMood(--mCurrentMood);
            }
            if (e1.getY() > e2.getY()) {
                changeMood(++mCurrentMood);
            }
            return true;
        }
    }

    //Change background color and smiley and store the currentmood value
    private void changeMood(int currentMood){
        if (currentMood < 0){
            currentMood = 0;
        } else if (currentMood > 4){
            currentMood = 4;
        }
        mCurrentMood = currentMood;
        switch(currentMood){
            case 0 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                mMoodImage.setImageResource(R.drawable.smiley_sad);
                mImageMood = R.drawable.smiley_sad;
                break;
            case 1 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                mMoodImage.setImageResource(R.drawable.smiley_disappointed);
                mImageMood = R.drawable.smiley_disappointed;
                break;
            case 2 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                mMoodImage.setImageResource(R.drawable.smiley_normal);
                mImageMood = R.drawable.smiley_normal;
                break;
            case 3 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                mMoodImage.setImageResource(R.drawable.smiley_happy);
                mImageMood = R.drawable.smiley_happy;
                break;
            case 4 :
                mRelativeLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                mMoodImage.setImageResource(R.drawable.smiley_super_happy);
                mImageMood = R.drawable.smiley_super_happy;
                break;
        }
        mPreferences.edit().putInt(PREF_KEY_MOOD,mCurrentMood).apply();
    }
}
