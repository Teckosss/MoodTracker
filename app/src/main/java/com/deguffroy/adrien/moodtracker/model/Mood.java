package com.deguffroy.adrien.moodtracker.model;

import java.util.Date;

/**
 * Created by Adrien Deguffroy on 29/03/2018.
 */

public class Mood {
    private int mMood;
    private String mDateMood;
    private String mMessageMood;

    public Mood(int mood, String dateMood, String messageMood) {
        mMood = mood;
        mDateMood = dateMood;
        mMessageMood = messageMood;
    }
    public int getMood() {
        return mMood;
    }

    public void setMood(int mood) {
        mMood = mood;
    }

    public String getDateMood() {
        return mDateMood;
    }

    public void setDateMood(String dateMood) {
        mDateMood = dateMood;
    }

    public String getMessageMood() {
        return mMessageMood;
    }

    public void setMessageMood(String messageMood) {
        mMessageMood = messageMood;
    }

    @Override
    public String toString() {
        return "Mood{" +
                "mMood=" + mMood +
                ", mDateMood='" + mDateMood + '\'' +
                ", mMessageMood='" + mMessageMood + '\'' +
                '}';
    }
}
