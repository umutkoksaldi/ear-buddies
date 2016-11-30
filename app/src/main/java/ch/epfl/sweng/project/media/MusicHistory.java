package ch.epfl.sweng.project.media;

import android.util.Log;
import android.widget.BaseAdapter;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Fragment.MusicHistoryFragment;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

/**
 * Created by Antoine Merino on 18/11/2016.
 */

public final class MusicHistory {

    private static MusicHistory musicHistory = null;
    // Adapter used by MusicHistoryFragment to update the listview
    BaseAdapter adapter = null;
    MusicHistoryFragment musicHistoryFragment = null;
    private int length = GlobalSetting.MUSIC_HISTORY_MAX_LENGTH;
    private ArrayList<Music> musicHistoryList = new ArrayList<>();
    private ModelApplication modelApplication = ModelApplication.getModelApplication();
    private MusicHistory() {
    }

    // allow to have one instance of the class shared in multiple activity.
    public static synchronized MusicHistory getMusicHistory() {
        if (musicHistory == null) {
            musicHistory = new MusicHistory();
        }
        return musicHistory;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("The length of music history should a positive number higher than zero");
        }

        if (length > GlobalSetting.MUSIC_HISTORY_MAX_LENGTH) {
            throw new IllegalArgumentException("The length of music history should not exceed the limit allowed (" +
                    GlobalSetting.MUSIC_HISTORY_MAX_LENGTH
                    + ")");
        }
        this.length = length;
    }

    public void updateFromServer(BaseAdapter adapter) {
        this.adapter = adapter;
        musicHistoryList.clear();
        sendGet(GlobalSetting.MUSIC_HISTORY_API);
    }

    public ArrayList<Music> getHistory() {
        return musicHistoryList;
    }

    private void sendGet(@SuppressWarnings("SameParameterValue") String
                                 requestApi) {
        Log.d("MusicHistory", "sendGet");
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    Log.d("MusicHistory", response.getBody().toString());
                    //modelApplication.setMusic((Music) response.getBody());
                    for (Music music : (Music[]) response.getBody()) {
                        musicHistoryList.add(music);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    // Erreur pas pu communiquer avec le serveur
                    Log.e("MusicHistory", "onSuccess() != Code 200 (good answer)");
                }
            }

            @Override
            public void onFailed() {
                Log.e("MusicHistory", "onFailed() : could not retreive the info from the server about music history");
            }
        });


        // the interface is already initiate above
        String requestURL = GlobalSetting.URL + requestApi + modelApplication.getUser().getIdApiConnection();
        Log.d("MusicInfoService", "GET Request : " + requestURL);
        serviceHandler.doGet(requestURL, Music[].class);
    }

}
