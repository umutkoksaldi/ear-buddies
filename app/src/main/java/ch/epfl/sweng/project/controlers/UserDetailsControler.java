package ch.epfl.sweng.project.controlers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.view.fragment.DetailsFragment;
import ch.epfl.sweng.project.view.fragment.MapFragment;
import ch.epfl.sweng.project.view.fragment.UsersFragment;

/**
 * Created by adupeyrat on 14/12/2016.
 */

public final class UserDetailsControler {


    private static UserDetailsControler userDetailsControler = null;
    private boolean openFromMap = false;
    private boolean openFromUserList = false;

    private UserDetailsControler() {
    }

    public static synchronized UserDetailsControler getConnectionControler() {
        if (userDetailsControler == null) {
            userDetailsControler = new UserDetailsControler();
        }
        return userDetailsControler;
    }

    public synchronized boolean isOpenFromUserList() {
        return openFromUserList;
    }

    public synchronized void setOpenFromUserList(boolean openFromUserList) {
        this.openFromUserList = openFromUserList;
    }

    public synchronized boolean isOpenFromMap() {
        return openFromMap;
    }

    public synchronized void setOpenFromMap(boolean openFromMap) {
        this.openFromMap = openFromMap;
    }

    public synchronized void openDetailsFragment(Fragment fragment, User user) {
        int layoutId;
        if (fragment instanceof MapFragment) {
            setOpenFromMap(true);
            layoutId = R.id.framelayout_map;
        } else if (fragment instanceof UsersFragment) {
            setOpenFromUserList(true);
            layoutId = R.id.framelayout_users;
        } else {
            throw new IllegalArgumentException("Trying to open UserDetailsFragment from a fragment different than " +
                    "People or Map");
        }
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setUser(user);
        fragment.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(layoutId, detailsFragment)
                .addToBackStack("")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();

    }

}

