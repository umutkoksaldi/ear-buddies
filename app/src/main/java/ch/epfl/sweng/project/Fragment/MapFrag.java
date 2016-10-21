package ch.epfl.sweng.project.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.project.R;

import static android.content.Intent.CATEGORY_APP_MUSIC;

public class MapFrag extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_map, container, false);
    }

}
