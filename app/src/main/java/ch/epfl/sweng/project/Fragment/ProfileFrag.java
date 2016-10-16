package ch.epfl.sweng.project.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.R;

public class ProfileFrag extends Fragment{

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.frag_profile, container, false);
        ImageView coverPict = (ImageView) profile.findViewById(R.id.coverPicProfile);
        new DownloadImageTask(coverPict).execute(modelApplication.getUser().getProfilePicture());
        TextView name = (TextView) profile.findViewById(R.id.nameProfile);
        //TODO Get name from the database
        name.setText(modelApplication.getUser().getFirstname());
        ImageView profilePict = (ImageView) profile.findViewById(R.id.profilePicProfile);
        new DownloadImageTask(profilePict).execute(modelApplication.getUser().getBackgroundPicture());
        return profile;
    }
}
