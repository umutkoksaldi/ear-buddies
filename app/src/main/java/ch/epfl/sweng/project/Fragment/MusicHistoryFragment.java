package ch.epfl.sweng.project.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.media.MusicHistory;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Antoine Merino on 18/11/2016.
 */

public class MusicHistoryFragment extends Fragment {

    MusicHistory musicHistory;
    MusicListAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        musicHistory = MusicHistory.getMusicHistory();


        ArrayList<Music> musicList = musicHistory.getHistory();



        View view = inflater.inflate(R.layout.frag_music_history, container, false);

        // Create the adapter to convert the array to views
        //adapter = new MusicListAdapter(getContext(), musicList);
        // Attach the adapter to a ListView
        //RecyclerView musicHistoryListview = (RecyclerView) view.findViewById(R.id.music_history_recyclerview);
        //musicHistoryListview.setAdapter(adapter);
        // Update the history from server and notify the adapter if new musicList exist


        recyclerView = (RecyclerView) view.findViewById(R.id.music_history_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MusicListAdapter(musicList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        musicHistory.updateFromServer(adapter);

        return view;


    }

    //public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>
    public static class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {

        private ArrayList<Music> musicList;

        public MusicListAdapter(ArrayList<Music> musicList, Context context) {
            this.musicList = musicList;
        }

        @Override
        public MusicListAdapter.MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_musichistory, parent, false);
            MusicViewHolder viewHolder = new MusicViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MusicListAdapter.MusicViewHolder holder, int position) {
            holder.artist.setText(musicList.get(position).getArtist());
            holder.song.setText(musicList.get(position).getName());
        }

        public Music getSongByPosition(int pos) {
            return musicList.get(pos);
        }

        @Override
        public int getItemCount() {
            return musicList.size();
        }

        public static class MusicViewHolder extends RecyclerView.ViewHolder {

            protected TextView artist;
            protected TextView song;

            public MusicViewHolder(View itemView) {
                super(itemView);
                artist = (TextView) itemView.findViewById(R.id.tvArtistName);
                song = (TextView) itemView.findViewById(R.id.tvSongName);
            }
        }

    }

}
