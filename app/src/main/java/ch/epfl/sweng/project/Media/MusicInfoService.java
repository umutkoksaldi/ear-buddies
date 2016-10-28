package ch.epfl.sweng.project.media;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

// Some code has been taken from this website :
// http://www.codeproject.com/Articles/992398/Getting-Current-Playing-Song-with-BroadcastReceive

public class MusicInfoService extends Service {
    public static final String ARTIST_NAME = "artistName";
    public static final String MUSIC_NAME = "musicName";
    private String artist = null;
    private String track = null;
    private Music music;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String newTrack = intent.getStringExtra("track");
            String newArtist = intent.getStringExtra("artist");
            // It's also possible to get the album, if needed in the future
            // String new_album = intent.getStringExtra("album");

            boolean playing = intent.getBooleanExtra("playing", false);


            if (playing) {
                if (track == null) {
                    track = newTrack;
                }
                if (artist == null) {
                    artist = newArtist;
                }
                if (artist.equals(newArtist) && track.equals(newTrack)) {
                    // It happens when you pause and play the same song. We don't want to send several times the
                    // same song info.
                    // Do nothing
                } else if (newArtist != null && newTrack != null) {
                    // Send the newly played song
                    Log.d("MusicInfoService", newArtist + " - " + newTrack);
                    Toast.makeText(MusicInfoService.this,
                            "[Love at 1st song] " + newArtist + " - " + newTrack,
                            Toast.LENGTH_LONG).show();
                    // Keep track of new song
                    artist = newArtist;
                    track = newTrack;
                }

            } else {
                Log.d("MusicInfoService", "No song currently playing");
                // The first time, the track and artist variables are null
            }

        }
    };



    @Override
    public void onCreate() {
        Log.i("MusicInfoService", "Service started");

        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.musicservicecommand");
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.updateprogress");

        registerReceiver(mReceiver, iF);

        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (!manager.isMusicActive()) {
            Log.d("MusicInfoService", "Music is paused or stopped");
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendPost(String AccesToken, String idFacebook, @SuppressWarnings("SameParameterValue") String requestApi) {
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    modelApplication.setUser((User) response.getBody());
                } else {
                    // Erreur pas pu communiquer avec le serveur
                    Toast.makeText(getApplicationContext(), getString(R.string.error_connexion_facebook), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed() {
// Erreur 404
                Toast.makeText(getApplicationContext(), getString(R.string.error_connexion_facebook), Toast.LENGTH_SHORT).show();
            }
        });

        // Building the parameters for the
        Map<String, String> params = new HashMap<>();
        params.put(ARTIST_NAME, idFacebook);
        params.put(MUSIC_NAME, AccesToken);

        // the interface is already initiate above
        serviceHandler.doPost(params, GlobalSetting.URL + requestApi, User.class);
    }

}
