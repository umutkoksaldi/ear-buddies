package ch.epfl.sweng.project.test_media;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.ClassRule;
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
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestMusicInfoService {

    // This is what we'll ask to the server. The typo mistakes are on purpose.
    private static final String ARTIST_NAME_REQUEST = "rihana";
    private static final String MUSIC_NAME_REQUEST = "umbrella";
    // This is what the server should answer
    private static final String ARTIST_NAME_TEST = "Rihanna";
    private static final String MUSIC_NAME_TEST = "Umbrella";
    private static final String URL_TEST = "https://www.last.fm/music/Rihanna/_/Umbrella";
    private static final String TAG_TEST = "Pop";
    // Workaround for service creation. See https://code.google.com/p/android/issues/detail?id=180396
    private static final int MAX_ITERATION = 100;
    @ClassRule
    public static ServiceTestRule mServiceRule = new ServiceTestRule();
    private static Context context;
    private static IBinder binder;
    private static MusicInfoService service = null;
    //----------------------------------------------------------------
    // Define constant
    private ModelApplication modelApplication;
    private ConnectionControler controlerConnection;
    //----------------------------------------------------------------
    // Test
    private boolean testChecked = false;

    @BeforeClass
    public static void  init() throws TimeoutException {
         context = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                "TestMusicInfoService");
        if (service == null) {
            Log.d("TestMusicInfoService", "init: null service");
        } else {
            Log.d("TestMusicInfoService", "init: " + service.toString());
        }

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
        //binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call public methods on the binder directly.
        // If the binder is null, it means that the service hasn't been bound, in our case it's because
        // it's bound one time for the first test, then it doesn't need to be bound again for the next ones.
        if (binder != null) {
            MusicInfoService musicInfoService = ((MusicInfoService.LocalBinder) binder).getService();
            if (musicInfoService != null) {
                service = musicInfoService;
            }
        }
        createMockUser();
        if (service == null) {
            Log.d("TestMusicInfoService", "init(end): null service");
        } else {
            Log.d("TestMusicInfoService", "init(end): " + service.toString());
        }
    }

    @Test
    public void bindedService() throws TimeoutException {
        assertEquals(service.ping(), "pong");
    }



    @Test
    public void playingSong() throws TimeoutException {
        assertEquals(service.ping(), "pong");
        // Inform the service that a new song will be played
        Intent trackIntent = new Intent("com.android.music.metachanged");
        trackIntent.putExtra("artist", ARTIST_NAME_REQUEST);
        trackIntent.putExtra("track", MUSIC_NAME_REQUEST);
        trackIntent.putExtra("playing", true);
        Log.d("TestMusicInfoService", "mock music playing: " + ARTIST_NAME_REQUEST + " - " + MUSIC_NAME_REQUEST);
        context.sendBroadcast(trackIntent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e("TestMusicInfoService", e.toString());
        }

        Music newMusic = ModelApplication.getModelApplication().getMusic();
        assertEquals("Artist names should be equals", ARTIST_NAME_TEST, newMusic.getArtist());
        assertEquals("Song names should be equals", MUSIC_NAME_TEST, newMusic.getName());
        assertEquals("Lastfm URL should be equals", URL_TEST, newMusic.getUrl());
        assertEquals("Music tags should be equals", TAG_TEST, newMusic.getTag());
    }
}