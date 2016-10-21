package ch.epfl.sweng.project;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ch.epfl.sweng.project.Fragment.BlankFrag;
import ch.epfl.sweng.project.Fragment.MapFrag;
import ch.epfl.sweng.project.Fragment.ProfileFrag;

class Pager extends FragmentStatePagerAdapter{

    private final int mTabCount;

    public Pager(FragmentManager fm, int tabCount){
        super(fm);
        mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BlankFrag();
            case 1:
                return new MapFrag();
            case 2:
                return new ProfileFrag();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

}
