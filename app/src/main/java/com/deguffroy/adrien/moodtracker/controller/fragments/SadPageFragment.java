package com.deguffroy.adrien.moodtracker.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deguffroy.adrien.moodtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SadPageFragment extends BaseFragment {


    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected BaseFragment newInstance() { return new SadPageFragment(); }

    @Override
    protected int getFragmentLayout() { return R.layout.fragment_sad_page; }

    @Override
    protected void configureDesign() { }

    @Override
    protected void updateDesign() {
    }

}
