package ch.epfl.sweng.project.Fragment;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ch.epfl.sweng.project.Model.Music;
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
    private TextView musicName;
    private ImageView musicImage;
    private TextView musicArtist;
    private User user;
    private static ImageView facebookPicture;
    private Music music;
    private FragmentManager manager;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_fragment, container, false);
        long musicId = 0;
        final Button backButton = (Button) view.findViewById(R.id.details_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        music = new Music();
        //musicId = user.getCurrentMusicId();
        musicArtist = (TextView)view.findViewById(R.id.details_song_artist);
        //if no artist, set "Song" string-just in order to have better appearance
        musicArtist.setText("Song:");
        if(musicId != 0 ) {
            String s = String.valueOf(musicId);
            musicArtist.setText(s);
        }

        musicName = (TextView) view.findViewById(R.id.details_song_name);
        musicName.setText("Ride 'em all down");
        musicImage = (ImageView) view.findViewById(R.id.details_song_picture);
        //do the post request to the server in order to get the music info
        //inside of a method change the musicDetails
        if(user.getProfilePicture() != null) {
            new DownloadImageTask(musicImage).execute(user.getProfilePicture());
        }
        nameDetails = (TextView) view.findViewById(R.id.details_name);
        nameDetails.setText(user.getFirstname());
        picture = (ImageView) view.findViewById(R.id.details_fragment_picture);
        new DownloadImageTask(picture).execute(user.getProfilePicture());
        description = (TextView) view.findViewById(R.id.details_description);
        if(user.getDescription() != null) {
            description.setText("Description: " + user.getDescription());
        }
        Log.i("Music Artist","" + music.getArtist());

        facebookClicked(view);

        return view;
    }
    public User detailsGetUser (){
        return user;
    }

//    public Music getMusic(){
//
//    }


    public void facebookClicked(View view) {
        facebookPicture = (ImageView)view.findViewById(R.id.details_fragment_facebook);
        final String url = "www.facebook.com";
        facebookPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fullUrl = "http://".concat(url.concat("/").concat(String.valueOf(user.getIdApiConnection())));

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
