package ch.epfl.sweng.project;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ch.epfl.sweng.project.Fragment.MapFrag;
import ch.epfl.sweng.project.Fragment.ProfileFrag;
import ch.epfl.sweng.project.Fragment.UsersFragment;

import static Util.GlobalSetting.FRAGMENT_MAP;
import static Util.GlobalSetting.FRAGMENT_PROFILE;
import static Util.GlobalSetting.FRAGMENT_USERS;

class Pager extends FragmentStatePagerAdapter{

    private final int mTabCount;
    private UsersFragment usersFragment = new UsersFragment();
    private ProfileFrag profileFrag = new ProfileFrag();

    public Pager(FragmentManager fm, int tabCount){
        super(fm);
        mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_USERS:
                return usersFragment;
            case FRAGMENT_MAP:
                return new MapFrag();
            case FRAGMENT_PROFILE:
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
