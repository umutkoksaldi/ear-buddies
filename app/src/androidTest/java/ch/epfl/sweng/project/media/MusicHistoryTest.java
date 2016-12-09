package ch.epfl.sweng.project.media;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Controler.ConnectionControler;
import ch.epfl.sweng.project.Fragment.MusicListAdapter;
import ch.epfl.sweng.project.GlobalTestSettings;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.Music;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by Antoine Merino on 18/11/2016.
 */

public class MusicHistoryTest {

    @ClassRule
    public static final ServiceTestRule mServiceRule = new ServiceTestRule();
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    ch.epfl.sweng.project.media.MusicHistory musicHistory = null;
    private Context context;

    public static void playSongIntent(Context context, String artist, String song) {
        try {
            mServiceRule.startService(
                    new Intent(InstrumentationRegistry.getTargetContext(), ch.epfl.sweng.project.media.MusicInfoService.class));
        } catch (TimeoutException e) {
            Log.e("MusicHistoryTest", e.toString());
        }

        // Inform the service that a new song will be played
        Intent trackIntent = new Intent("com.android.music.metachanged");
        trackIntent.putExtra("artist", artist);
        trackIntent.putExtra("track", song);
        trackIntent.putExtra("playing", true);
        context.sendBroadcast(trackIntent);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e("MusicHistoryTest", e.toString());
        }


    }

    @Before
    public void init() {
        GlobalTestSettings.createFakeUser();
        context = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                "MusicHistoryTest");
        controlerConnection.sendPost(null, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK, GlobalTestSettings
                        .MOCK_ID_FACEBOOK,
                GlobalSetting.USER_API, true);
        musicHistory = ch.epfl.sweng.project.media.MusicHistory.getMusicHistory();
    }

    @Test
    public void testSingletonInitialization() {
        boolean initialized = false;
        if (musicHistory != null) {
            initialized = true;
        }
        assertThat(initialized, is(true));
    }

    @Test
    public void testWithStartedService() {
        playSongIntent(context, GlobalTestSettings.ARTIST_NAME_REQUEST, GlobalTestSettings.MUSIC_NAME_REQUEST);
        Music newMusic = ModelApplication.getModelApplication().getMusic();
        assertEquals("Artist names should be equals", GlobalTestSettings.ARTIST_NAME_RESPONSE, newMusic.getArtist());
        assertEquals("Song names should be equals", GlobalTestSettings.MUSIC_NAME_RESPONSE, newMusic.getName());

    }

    @Test
    public void historyLengthHigherThanZero() {
        boolean behavesAsExpected = false;
        try {
            musicHistory.setLength(0);
        } catch (IllegalArgumentException e) {
            // It's expected, we don't want to put a zero length for the history
            behavesAsExpected = true;
        }
        assertThat(behavesAsExpected, is(true));
    }

    @Test
    public void historyLengthShouldBePositive() {
        boolean behavesAsExpected = false;
        try {
            musicHistory.setLength(-5);
        } catch (IllegalArgumentException e) {
            // It's expected, we don't want to put a negative length for the history
            behavesAsExpected = true;
        }
        assertThat(behavesAsExpected, is(true));
    }

    @Test
    public void historyLengthShouldNotExceedMaxLength() {
        boolean behavesAsExpected = false;
        try {
            musicHistory.setLength(GlobalSetting.MUSIC_HISTORY_MAX_LENGTH + 1);
        } catch (IllegalArgumentException e) {
            // It's expected, we don't want to put the music history length above the limit
            behavesAsExpected = true;
        }
        assertThat(behavesAsExpected, is(true));
    }

    @Test
    public void testHistorySetLength() {
        int expectedLength = GlobalSetting.MUSIC_HISTORY_MAX_LENGTH - 1;
        boolean behavesAsExpected = false;
        musicHistory.setLength(expectedLength);
        assertEquals(musicHistory.getLength(), expectedLength);
    }

    @Test
    public void testHistory() {
        ArrayList<Music> musicList = new ArrayList<>();
        MusicListAdapter adapter = new MusicListAdapter(musicList,
                context);
        musicHistory.updateFromServer(adapter, null);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e("MusicHistoryTest", e.toString());
        }
        musicList = musicHistory.getHistory();
        assertEquals(musicList.get(0).getArtist(), GlobalTestSettings.ARTIST_NAME_RESPONSE);
        assertEquals(musicList.get(0).getName(), GlobalTestSettings.MUSIC_NAME_RESPONSE);
    }

}
