package ch.epfl.sweng.project.test_media;

import android.content.Context;
import android.content.Intent;
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

import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.controler.ConnectionControler;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
import ch.epfl.sweng.project.model.ModelApplication;
import ch.epfl.sweng.project.model.Music;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Antoine Merino on 28/10/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestMusicInfoService {

    // This is what we'll ask to the server. The typo mistakes are on purpose.
    private static final String ARTIST_NAME_REQUEST = "rihana";
    private static final String MUSIC_NAME_REQUEST = "umbrella";
    // This is what the server should answer
    private static final String ARTIST_NAME_TEST = "Rihanna";
    private static final String MUSIC_NAME_TEST = "Umbrella";
    private static final String URL_TEST = "https://www.last.fm/music/Rihanna/_/Umbrella";
    private static final String TAG_TEST = "pop";

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();
    //----------------------------------------------------------------
    // Define constant
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    //----------------------------------------------------------------
    // Test
    private boolean testChecked = false;
    private Context context;

    @Before
    public void init() {
        context = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                "TestMusicInfoService");
        controlerConnection.sendPost(null, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK, GlobalTestSettings
                .MOCK_ID_FACEBOOK,
                GlobalSetting.USER_API, true);
    }

    @Test
    public void testWithStartedService() {
        try {
            mServiceRule.startService(
                    new Intent(InstrumentationRegistry.getTargetContext(), ch.epfl.sweng.project.test_media.MusicInfoService.class));
        } catch (TimeoutException e) {
            Log.e("TestMusicInfoService", e.toString());
        }

        // Inform the service that a new song will be played
        Intent trackIntent = new Intent("com.android.music.metachanged");
        trackIntent.putExtra("artist", ARTIST_NAME_REQUEST);
        trackIntent.putExtra("track", MUSIC_NAME_REQUEST);
        trackIntent.putExtra("playing", true);
        context.sendBroadcast(trackIntent);
        try {
            Thread.sleep(2000);
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