package com.deguffroy.adrien.moodtracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deguffroy.adrien.moodtracker.controller.fragments.DissapPageFragment;
import com.deguffroy.adrien.moodtracker.controller.fragments.HappyPageFragment;
import com.deguffroy.adrien.moodtracker.controller.fragments.NormalPageFragment;
import com.deguffroy.adrien.moodtracker.controller.fragments.SadPageFragment;
import com.deguffroy.adrien.moodtracker.controller.fragments.SuperHapPageFragment;

/**
 * Created by Adrien Deguffroy on 28/03/2018.
 */

public class PageAdapter extends FragmentPagerAdapter{

    public PageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(5); // Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: //Page number 1
                return SadPageFragment.newInstance();
            case 1: //Page number 2
                return DissapPageFragment.newInstance();
            case 2: //Page number 3
                return NormalPageFragment.newInstance();
            case 3: //Page number 4
                return HappyPageFragment.newInstance();
            case 4: //Page number 5
                return SuperHapPageFragment.newInstance();
            default:
                return null;
        }
    }
}
