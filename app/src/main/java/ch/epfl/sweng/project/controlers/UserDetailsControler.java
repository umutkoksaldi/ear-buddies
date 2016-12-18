package ch.epfl.sweng.project.controlers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import ch.epfl.sweng.project.view.fragment.DetailsFragment;
import ch.epfl.sweng.project.view.fragment.MapFragment;
import ch.epfl.sweng.project.view.fragment.UsersFragment;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.R;

/**
 * Created by adupeyrat on 14/12/2016.
 */

public final class UserDetailsControler {


    private static UserDetailsControler userDetailsControler = null;
    private boolean openFromMap = false;
    private boolean openFromUserList = false;

    private UserDetailsControler() {
    }

    public static UserDetailsControler getConnectionControler() {
        if (userDetailsControler == null) {
            userDetailsControler = new UserDetailsControler();
        }
        return userDetailsControler;
    }

    public boolean isOpenFromUserList() {
        return openFromUserList;
    }

    public void setOpenFromUserList(boolean openFromUserList) {
        this.openFromUserList = openFromUserList;
    }

    public boolean isOpenFromMap() {
        return openFromMap;
    }

    public void setOpenFromMap(boolean openFromMap) {
        this.openFromMap = openFromMap;
    }

    public void openDetailsFragment(Fragment fragment, User user) {
        int layout_id;
        if (fragment instanceof MapFragment) {
            setOpenFromMap(true);
            layout_id = R.id.framelayout_map;
        } else if (fragment instanceof UsersFragment) {
            setOpenFromUserList(true);
            layout_id = R.id.framelayout_users;
        } else {
            throw new IllegalArgumentException("Trying to open UserDetailsFragment from a fragment different than " +
                    "People or Map");
        }
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setUser(user);
        fragment.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(layout_id, detailsFragment)
                .addToBackStack("")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();


    }

}

