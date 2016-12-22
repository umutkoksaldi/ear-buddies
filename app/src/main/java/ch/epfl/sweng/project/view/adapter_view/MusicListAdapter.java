package ch.epfl.sweng.project.view.adapter_view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.project.BuildConfig;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.view.util_view.DownloadImageTask;
import ch.epfl.sweng.project.view.web_view.CustomTabActivityHelper;
import ch.epfl.sweng.project.view.web_view.WebviewFallback;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Antoine Merino on 18/11/2016.
 */


public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {

    private ArrayList<Music> musicList;

    public MusicListAdapter(ArrayList<Music> musicList, Context context) {
        this.musicList = musicList;
    }

    @Override
    public MusicListAdapter.MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_musichistory, parent, false);
        return new MusicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MusicListAdapter.MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.artist.setText(music.getArtist());
        holder.song.setText(music.getName());
        String tag = music.getTag();
        if (!tag.equals("unknown")) {
            holder.tag.setText(tag);
        }

        String coverUrl = music.getUrlPicture();
        //String url = "https://pbs.twimg.com/profile_images/634829866504859648/GuMPPRJ6.png";
        if (BuildConfig.DEBUG) {
            Log.d("MusicHistoryFragment", "music: " + music.getArtist() + " - " + music.getName() +
                    " - " + music.getId() + " - " + music.getTag() + " - " + music.getUrl() + " - " + music
                    .getUrlPicture());
        }
        if (coverUrl != null && !coverUrl.isEmpty()) {
            new DownloadImageTask(holder.cover).execute(coverUrl);
        }
        holder.container.setOnClickListener(new SongOnClickListener(music.getUrl()));
    }


    public Music getSongByPosition(int pos) {
        return musicList.get(pos);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    private static class SongOnClickListener implements View.OnClickListener {
        String url;

        public SongOnClickListener(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            if (url == null) {
                // No lastfm page associated with the current song
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string
                        .no_lastfm_page_found), Toast.LENGTH_SHORT).show();
            } else {
                // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
                // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                // and launch the desired Url with CustomTabsIntent.launchUrl()
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                Activity activity = (Activity) v.getContext();
                CustomTabActivityHelper.openCustomTab(
                        activity,
                        customTabsIntent,
                        Uri.parse(url),
                        new WebviewFallback()
                );
            }
        }

    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder {

        protected TextView artist;
        protected TextView song;
        protected TextView tag;
        protected ImageView cover;
        protected RelativeLayout container;

        public MusicViewHolder(View itemView) {
            super(itemView);
            artist = (TextView) itemView.findViewById(R.id.tvArtistName);
            song = (TextView) itemView.findViewById(R.id.tvSongName);
            tag = (TextView) itemView.findViewById(R.id.tvSongTag);
            cover = (ImageView) itemView.findViewById(R.id.ivCover);
            container = (RelativeLayout) itemView.findViewById(R.id.single_row_music_history);
        }
    }

}

