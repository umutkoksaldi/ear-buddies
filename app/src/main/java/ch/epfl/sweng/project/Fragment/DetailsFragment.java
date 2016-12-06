package ch.epfl.sweng.project.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Webview.CustomTabActivityHelper;
import ch.epfl.sweng.project.Webview.WebviewFallback;


/**
 * Created by Dusan Viktor on 2016-11-18.
 */


public class DetailsFragment extends Fragment {

    private ImageView picture;
    private TextView description;
    private TextView nameDetails;
    private User user;
    private static ImageView facebookPicture;

    private FragmentManager manager;

    public void setUser(User user) {
        this.user = user;
    }


    @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_fragment, container, false);

        final Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        nameDetails = (TextView) view.findViewById(R.id.details_name);
        nameDetails.setText(user.getFirstname());
        picture = (ImageView) view.findViewById(R.id.details_fragment_picture);
        new DownloadImageTask(picture).execute(user.getProfilePicture());
        description = (TextView) view.findViewById(R.id.details_description);
        description.setText(user.getDescription());


        //onUserClick();
        facebookClicked(view);

        return view;
    }


    public void facebookClicked(View view) {
        facebookPicture = (ImageView)view.findViewById(R.id.details_fragment_facebook);
        final String url = "www.facebook.com";
        facebookPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fullUrl = "http://".concat(url.concat("/").concat(String.valueOf(user.getIdApiConnection())));
                Toast.makeText(getActivity(), "IdApiConnection is:" + fullUrl, Toast
                        .LENGTH_SHORT).show();
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();

                Activity activity = (Activity) v.getContext();

                CustomTabActivityHelper.openCustomTab(

                        activity,

                        customTabsIntent,

                        Uri.parse(fullUrl),

                        new WebviewFallback()
                );
            }
        });
    }

}
