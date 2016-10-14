package ch.epfl.sweng.project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFrag extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.frag_profile, container, false);
        ImageView coverPict = (ImageView) profile.findViewById(R.id.coverPicProfile);
        new DownloadImageTask(coverPict).execute(
                "https://scontent-fra3-1.xx.fbcdn.net/v/t1.0-9/419921_334072353301892_257076548_n."+
                "jpg?oh=da0d059723d43991eb7d5484eb978bad&oe=585F246B");
        TextView name = (TextView) profile.findViewById(R.id.nameProfile);
        //TODO Get name from the database
        name.setText("Arnaud Hennig");
        ImageView profilePict = (ImageView) profile.findViewById(R.id.profilePicProfile);
        new DownloadImageTask(profilePict).execute(
                "https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/1509160_10203102805357984_286487804_n."+
                        "jpg?oh=704369e064ea17c00f6ea2e6ff1495a3&oe=5862268E");
        return profile;
    }
}
