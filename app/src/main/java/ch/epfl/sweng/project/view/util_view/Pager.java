package ch.epfl.sweng.project.view.util_view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ch.epfl.sweng.project.view.fragment.MapFragment;
import ch.epfl.sweng.project.view.fragment.ProfileFragment;
import ch.epfl.sweng.project.view.fragment.UsersFragment;

import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_MAP;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_PROFILE;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_USERS;

public class Pager extends FragmentStatePagerAdapter {

    private final int mTabCount;
    private UsersFragment usersFragment = new UsersFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_USERS:
                return usersFragment;
            case FRAGMENT_MAP:
                return new MapFragment();
            case FRAGMENT_PROFILE:
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

}
