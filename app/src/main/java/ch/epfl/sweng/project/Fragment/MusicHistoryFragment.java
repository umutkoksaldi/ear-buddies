package ch.epfl.sweng.project.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.media.MusicHistory;

/**
 * Created by Antoine Merino on 18/11/2016.
 */

public class MusicHistoryFragment extends Fragment {

    MusicHistory musicHistory = null;
    MusicListAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        musicHistory = MusicHistory.getMusicHistory();


        ArrayList<Music> musicList = musicHistory.getHistory();



        View view = inflater.inflate(R.layout.frag_music_history, container, false);

        // Create the adapter to convert the array to views
        adapter = new MusicListAdapter(getContext(), musicList);
        // Attach the adapter to a ListView
        ListView musicHistoryListview = (ListView) view.findViewById(R.id.music_history_listview);
        musicHistoryListview.setAdapter(adapter);
        // Update the history from server and notify the adapter if new musics exist
        musicHistory.updateFromServer(adapter);


        return view;


    }

    public static class MusicListAdapter extends ArrayAdapter<Music> {


        private Context context;
        private String[] artistNames;
        private String[] songNames;

        public MusicListAdapter(Context context, ArrayList<Music> musics) {
            super(context, 0, musics);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Music music = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_row_musichistory, parent,
                        false);
            }
            // Lookup view for data population
            TextView tvArtistName = (TextView) convertView.findViewById(R.id.tvArtistName);
            TextView tvSongName = (TextView) convertView.findViewById(R.id.tvSongName);
            // Populate the data into the template view using the data object
            tvArtistName.setText(music.getArtist());
            tvSongName.setText(music.getName());
            // Return the completed view to render on screen
            return convertView;

        }

        public Music getSongByPosition(int pos) {
            return getItem(pos);
        }
    }

}
