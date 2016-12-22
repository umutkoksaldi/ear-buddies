package ch.epfl.sweng.project.medias;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;

import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.server_request.OnServerRequestComplete;
import ch.epfl.sweng.project.server_request.ServiceHandler;
import ch.epfl.sweng.project.util_constant.GlobalSetting;

/**
 * Created by Antoine Merino on 18/11/2016.
 */

public final class MusicHistory {

    private static MusicHistory musicHistory = null;
    // Adapter used by MusicHistoryFragment to update the listview
    private RecyclerView.Adapter adapter = null;
    private SwipeRefreshLayout swipeContainer = null;
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

    public void updateFromServer(RecyclerView.Adapter adapter, SwipeRefreshLayout swipeContainer) {
        this.adapter = adapter;
        this.swipeContainer = swipeContainer;
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
                    Collections.addAll(musicHistoryList, (Music[]) response.getBody());
                    adapter.notifyDataSetChanged();
                    if (swipeContainer != null) {
                        swipeContainer.setRefreshing(false);
                    }
                } else {
                    // Error, couldn't communicate with the server
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
        Log.d("MusicHistory", "GET Request : " + requestURL);
        serviceHandler.doGet(requestURL, Music[].class);
    }


}
