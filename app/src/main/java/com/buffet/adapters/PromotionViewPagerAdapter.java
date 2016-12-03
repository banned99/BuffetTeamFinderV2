package com.buffet.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.buffet.fragments.CategoryFragment;
import com.buffet.fragments.CategoryRootFragment;
import com.buffet.fragments.NewProFragment;
import com.buffet.fragments.TopProFragment;

/**
 * Created by Banned on 10/16/2016.
 */

public class PromotionViewPagerAdapter extends FragmentPagerAdapter {
    public PromotionViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0) {
            return NewProFragment.newInstance();
        } else if (position == 1) {
            return CategoryRootFragment.newInstance();
        } else if (position == 2) {
            return TopProFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
