package com.buffet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.buffet.fragments.JoinDealFragment;
import com.buffet.fragments.OwnerDealFragment;

/**
 * Created by icespw on 12/3/2016 AD.
 */

public class AllDealViewPagerAdapter extends FragmentPagerAdapter {
    public AllDealViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return OwnerDealFragment.newInstance();
        } else if (position == 1) {
            return JoinDealFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
