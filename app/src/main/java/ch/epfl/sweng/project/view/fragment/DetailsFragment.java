package ch.epfl.sweng.project.view.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.controlers.UserSongControler;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.view.util_view.DownloadImageTask;


/**
 * Created by Dusan Viktor on 2016-11-18.
 */


public class DetailsFragment extends Fragment {

    RelativeLayout musicContainer;
    private ImageView picture;
    private TextView description;
    private TextView nameDetails;
    private TextView musicName;
    private ImageView musicImage;
    private TextView musicTag;
    private TextView musicArtist;
    private User user;
    private ImageButton facebookButton;
    private Music music;
    private FragmentManager manager;
    private ImageView coverPicture;
    private ImageView songCover;
    private UserSongControler userSongControler;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("DetailsFragment", "onCreateView");

        View view = inflater.inflate(R.layout.frag_detail_user, container, false);
        userSongControler = UserSongControler.getUserSongControler();

        coverPicture = (ImageView) view.findViewById(R.id.details_cover);
        if (user != null) {
            new DownloadImageTask(coverPicture).execute(user.getBackgroundPicture());


            music = new Music();
            //musicId =
            musicArtist = (TextView) view.findViewById(R.id.tvArtistName);
            musicName = (TextView) view.findViewById(R.id.tvSongName);
            musicTag = (TextView) view.findViewById(R.id.tvSongTag);
            musicContainer = (RelativeLayout) view.findViewById(R.id.details_music_container);
            long musicId = user.getCurrentMusicId();
            Log.d("DetailsFragment", "musicId = " + musicId);
            if (musicId != 0) {
                songCover = (ImageView) view.findViewById(R.id.ivCover);
                Music music = userSongControler.getSongMap().get(user.getIdApiConnection());
                if (music != null) {
                    musicArtist.setText(music.getArtist());
                    musicName.setText(music.getName());
                    String tag = music.getTag();
                    if (!tag.isEmpty() && !tag.equals("unknown")) {
                        musicTag.setText(tag);
                    }
                    String coverUrl = music.getUrlPicture();
                    if (coverUrl != null && !coverUrl.isEmpty()) {
                        new DownloadImageTask(songCover).execute(coverUrl);
                    }
                    musicContainer.setVisibility(View.VISIBLE);
                } else {
                    Log.d("DetailsFragment", "musicId not null but no music associated with this user in the " +
                            "userSongControler.");
                }
            }

            nameDetails = (TextView) view.findViewById(R.id.details_name);
            nameDetails.setText(user.getFirstname());
            picture = (ImageView) view.findViewById(R.id.details_fragment_picture);
            new DownloadImageTask(picture).execute(user.getProfilePicture());
            description = (TextView) view.findViewById(R.id.details_description);
            if (user.getDescription() != null) {
                description.setText(user.getDescription());
            }

            facebookClicked(view);
        }
        return view;
    }

    public User detailsGetUser() {
        return user;
    }

//    public Music getMusic(){
//
//    }


    public void facebookClicked(View view) {
        facebookButton = (ImageButton) view.findViewById(R.id.button_details_facebook);
        final String url = "www.facebook.com";
        facebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fullUrl = "http://" + (url + "/").concat(String.valueOf(user.getIdApiConnection()));

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl));
                startActivity(browserIntent);

            }
        });
    }

}
