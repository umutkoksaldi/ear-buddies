package ch.epfl.sweng.project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import ch.epfl.sweng.project.Fragment.DetailsFragment;
import ch.epfl.sweng.project.Model.User;

/**
 * Created by adupeyrat on 14/12/2016.
 */

public final class MapControler {


    private static MapControler mapControler = null;
    private boolean detailFragmentOpened = false;

    private MapControler() {
    }

    public static MapControler getConnectionControler() {
        if (mapControler == null) {
            mapControler = new MapControler();
        }
        return mapControler;
    }

    public boolean isDetailFragmentOpened() {
        return detailFragmentOpened;
    }

    public void setDetailFragmentOpened(boolean detailFragmentOpened) {
        this.detailFragmentOpened = detailFragmentOpened;
    }

    public void openDetailsFragment(Fragment mapAcitivty, User user) {

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setUser(user);
        mapAcitivty.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, detailsFragment)
                .addToBackStack("mapFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
        setDetailFragmentOpened(true);
    }

}

