package ch.epfl.sweng.project;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ch.epfl.sweng.project.Fragment.MapFrag;
import ch.epfl.sweng.project.Fragment.ProfileFrag;
import ch.epfl.sweng.project.Fragment.UsersFragment;

class Pager extends FragmentStatePagerAdapter{

    private final int mTabCount;
    private UsersFragment usersFragment = new UsersFragment();
    private MapFrag mapFrag = new MapFrag();
    private ProfileFrag profileFrag = new ProfileFrag();

    public Pager(FragmentManager fm, int tabCount){
        super(fm);
        mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return usersFragment;
            case 1:
                return mapFrag;
            case 2:
                return profileFrag;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

}
