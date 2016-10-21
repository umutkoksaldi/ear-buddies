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

// Some code has been taken from this website :
// http://www.codeproject.com/Articles/992398/Getting-Current-Playing-Song-with-BroadcastReceive

public class MusicInfoService extends Service {
    private String artist = null;
    private String track = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");

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
                } else {
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
}
