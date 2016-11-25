package ch.epfl.sweng.project.media;

import android.util.Log;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

/**
 * Created by Antoine Merino on 18/11/2016.
 */

public final class MusicHistory {

    private static MusicHistory musicHistory = null;
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

    public void updateFromServer() {
        // The server part is currently not implemented so  it's currently faked with example musics
        musicHistoryList.clear();
        musicHistoryList.add(new Music("Rihanna", "What's my name"));
        musicHistoryList.add(new Music("Rihanna", "Rude boy"));
        musicHistoryList.add(new Music("Rihanna", "Umbrella"));
        musicHistoryList.add(new Music("Rihanna", "Don't stop the music"));
        musicHistoryList.add(new Music("Rihanna", "Russian roulette"));
        musicHistoryList.add(new Music("Rihanna", "We found love"));
        musicHistoryList.add(new Music("Rihanna", "Live your life"));
        musicHistoryList.add(new Music("Rihanna", "Love the way you lie"));
        musicHistoryList.add(new Music("Rihanna", "Take a bow"));
        musicHistoryList.add(new Music("Rihanna", "Disturbia"));

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
        serviceHandler.doGet(requestURL, Music.class);
    }

}
