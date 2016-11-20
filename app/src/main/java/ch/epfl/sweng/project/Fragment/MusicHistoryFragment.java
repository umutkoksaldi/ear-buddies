package ch.epfl.sweng.project.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.project.R;

/**
 * Created by Antoine Merino on 18/11/2016.
 */

public class MusicHistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_music_history, container, false);
        return view;
    }

}
