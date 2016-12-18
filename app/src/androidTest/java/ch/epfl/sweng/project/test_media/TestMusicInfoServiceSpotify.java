package ch.epfl.sweng.project.test_media;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import ch.epfl.sweng.project.controlers.ConnectionControler;
import ch.epfl.sweng.project.medias.MusicInfoService;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;

import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MAX_ITERATION_BIND;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.createMockUser;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Antoine Merino on 28/10/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestMusicInfoServiceSpotify {

    // This is what we'll ask to the server. The typo mistakes are on purpose.
    private static final String SPOTIFY_ARTIST_NAME_REQUEST = "eminem";
    private static final String SPOTIFY_MUSIC_NAME_REQUEST = "berzerk";
    // This is what the server should answer
    private static final String SPOTIFY_ARTIST_NAME_TEST = "Eminem";
    private static final String SPOTIFY_MUSIC_NAME_TEST = "Berzerk";
    private static final String SPOTIFY_TAG_TEST = "rap";
    private static final String SPOTIFY_URL_TEST = "https://www.last.fm/music/Eminem/_/Berzerk";
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();
    //----------------------------------------------------------------
    // Define constant
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    //----------------------------------------------------------------
    // Test
    private Context context;
    private IBinder binder;
    private MusicInfoService service;

    @Before
    public void init() throws TimeoutException {
        context = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                "TestMusicInfoService");

        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(), MusicInfoService.class);

        // Bind the service and grab a reference to the binder.
        // it is a known bugs https://code.google.com/p/android/issues/detail?id=180396
        int it = 0;
        while(binder == null && it < MAX_ITERATION_BIND){
            binder = mServiceRule.bindService(serviceIntent);
            it++;
        }

        // Get the reference to the service, or you can call public methods on the binder directly.
        // If the binder is null, it means that the service hasn't been bound, in our case it's because
        // it's bound one time for the first test, then it doesn't need to be bound again for the next ones.
        if (binder != null) {
            service = ((MusicInfoService.LocalBinder) binder).getService();
        }
        createMockUser();
    }

    @Test
    public void playingSong() {

        // Inform the service that a new song will be played
        Intent spotifyTrackIntent = new Intent("com.spotify.music.metadatachanged");
        spotifyTrackIntent.putExtra("artist", SPOTIFY_ARTIST_NAME_REQUEST);
        spotifyTrackIntent.putExtra("track", SPOTIFY_MUSIC_NAME_REQUEST);
        spotifyTrackIntent.putExtra("playing", true);
        context.sendBroadcast(spotifyTrackIntent);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.e("TestMusicInfoService", e.toString());
        }
        Music newSpotifyMusic = ModelApplication.getModelApplication().getMusic();
        assertEquals("Artist names should be equals", SPOTIFY_ARTIST_NAME_TEST, newSpotifyMusic.getArtist());
        assertEquals("Song names should be equals", SPOTIFY_MUSIC_NAME_TEST, newSpotifyMusic.getName());
        assertEquals("Lastfm URL should be equals", SPOTIFY_URL_TEST, newSpotifyMusic.getUrl());
        assertEquals("Music tags should be equals", SPOTIFY_TAG_TEST, newSpotifyMusic.getTag());
    }
}