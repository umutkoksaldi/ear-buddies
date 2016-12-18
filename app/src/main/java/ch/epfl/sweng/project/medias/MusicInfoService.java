package ch.epfl.sweng.project.medias;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.project.BuildConfig;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.server_request.OnServerRequestComplete;
import ch.epfl.sweng.project.server_request.ServiceHandler;
import ch.epfl.sweng.project.util_constant.GlobalSetting;

// Some code has been taken from this website :
// http://www.codeproject.com/Articles/992398/Getting-Current-Playing-Song-with-BroadcastReceive

public class MusicInfoService extends Service {
    public static final String ARTIST_NAME = "artistName";
    public static final String MUSIC_NAME = "musicName";
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private String artist = "";
    private String track = "";
    private Music music;
    // For standard music players
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String newTrack = intent.getStringExtra("track");
            String newArtist = intent.getStringExtra("artist");
            if (BuildConfig.DEBUG) {
                Log.d("MusicInfoService", intent.toString());
                Log.v("MusicInfoService", intent.getExtras().toString());
            }

            boolean playing = intent.getBooleanExtra("playing", false);


            if (playing && newArtist != null && newTrack != null) {
                if (artist.equals(newArtist) && track.equals(newTrack)) {
                    // It happens when you pause and play the same song. We don't want to send several times the
                    // same song info.
                    // Do nothing
                    Log.d("MusicInfoService", "mReceiver.onReceive : new song is the same as the last one");
                } else if (newArtist != null && newTrack != null) {
                    // Send the newly played song instead of just displaying a toast
                    Log.d("MusicInfoService", "mReceiver.onReceive new song playing: " + newArtist + " - " + newTrack);
                    sendPost(newArtist, newTrack, GlobalSetting.MUSIC_API);
                }
                // Keep track of new song
                artist = newArtist;
                track = newTrack;

            } else {
                Log.d("MusicInfoService", "Music is paused");
            }

        }
    };

    // Spotify specific implementation


    @Override
    public void onCreate() {
        Log.i("MusicInfoService", "Service started");
        getLastMusicListened();
        registerMusicIntentReceiver();

        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (!manager.isMusicActive()) {
            Log.d("MusicInfoService", "Music is paused or stopped");
        }

    }


    @Override
    public void onDestroy() {
        try {
            if (mReceiver != null)
                unregisterReceiver(mReceiver);
        } catch (Exception e) {

        }
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void sendPost(String artistName, String trackName, @SuppressWarnings("SameParameterValue") String
            requestApi) {
        Log.d("MusicInfoService", "sendPost : " + artistName + ", " + trackName);
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    Log.d("MusicInfoService", "Response body : " + response.getBody());
                    modelApplication.setMusic((Music) response.getBody());
                    Log.d("MusicInfoService", "new track in modelApplication : " + modelApplication.getMusic()
                            .getArtist() + " - " + modelApplication.getMusic().getName());
                } else {
                    // Error, couldn't communicate with the server
                    Toast.makeText(getApplicationContext(), getString(R.string.server_error_music_info), Toast
                            .LENGTH_SHORT).show();
                    Log.e("MusicInfoService", "onSuccess() != Code 200 (good answer)");
                }
            }

            @Override
            public void onFailed() {
// Erreur 404
                Toast.makeText(getApplicationContext(), getString(R.string.server_error_music_info), Toast
                        .LENGTH_SHORT).show();
                Log.e("MusicInfoService", "onFailed() : could not retreive the info from the server about currently " +
                        "playing music");
            }
        });

        // Building the parameters for the
        Map<String, String> params = new HashMap<>();
        params.put(ARTIST_NAME, artistName);
        params.put(MUSIC_NAME, trackName);

        // the interface is already initiate above
        String requestURL = GlobalSetting.URL + requestApi + modelApplication.getUser().getIdApiConnection();
        Log.d("MusicInfoService", "POST Request : " + requestURL);
        serviceHandler.doPost(params, requestURL, Music.class);
    }

    public String ping() {
        return "pong";
    }

    private void registerMusicIntentReceiver() {
        IntentFilter iF = new IntentFilter();
        // Some filters found on this page:
        // http://stackoverflow.com/questions/34389404/android-get-current-song-playing-and-song-changed-events-like
        // -musixmatch
        // Stock music player, Google play music, VLC...
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");
        iF.addAction("com.android.music.playbackcomplete");
        iF.addAction("com.android.music.queuechanged");

        // MIUI music player
        iF.addAction("com.miui.player.metachanged");

        // HTC music player
        iF.addAction("com.htc.music.metachanged");

        // WinAmp
        iF.addAction("com.nullsoft.winamp.metachanged");

        // MyTouch4G
        iF.addAction("com.real.IMP.metachanged");

        // Spotify
        // More doc about Spotify implementation : https://developer.spotify
        // .com/technologies/spotify-android-sdk/android-media-notifications
        iF.addAction("com.spotify.music.metadatachanged");

        registerReceiver(mReceiver, iF);
    }

    private void getLastMusicListened() {
        long songId = ModelApplication.getModelApplication()
                .getUser().getCurrentMusicId();
        Log.d("MusicInfoService", "asking last music listened by the user (Id: " + songId + ")");
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {
                // Remember what was the last song played by this user to avoid duplicate
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {

                    Music music = (Music) response.getBody();
                    if (music.getArtist() != null && music.getName() != null) {
                        artist = music.getArtist();
                        track = music.getName();
                        Log.d("MusicInfoService", "fetched last music listened: " + artist + " - " + track);
                    }


                } else {
                    // Error, couldn't communicate with the serveur
                    Log.e("UsersFragment", "onSuccess() != Code 200 (good answer)");
                }
            }

            @Override
            public void onFailed() {
                Log.e("UsersFragment", "onFailed() : could not retreive the info from the server about the song");
            }
        });


        // the interface is already initiate above
        String requestURL = GlobalSetting.URL + GlobalSetting.MUSIC_API + songId;
        Log.d("UsersFragment", "GET Request : " + requestURL);
        serviceHandler.doGet(requestURL, Music.class);
    }

    public class LocalBinder extends Binder {

        public MusicInfoService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return MusicInfoService.this;
        }
    }
}
