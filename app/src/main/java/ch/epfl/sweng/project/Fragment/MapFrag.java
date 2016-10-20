package ch.epfl.sweng.project.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;

public class MapFrag extends Fragment{
    User mUser;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mUser = ModelApplication.getModelApplication().getUser();
        mUser.sendAndGetLocations();
        return inflater.inflate(R.layout.frag_map, container, false);
    }
}
