package ch.epfl.sweng.project.test_media;

import android.content.BroadcastReceiver;
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

import ch.epfl.sweng.project.medias.MusicHistory;
import ch.epfl.sweng.project.medias.MusicInfoService;
import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.controlers.ConnectionControler;
import ch.epfl.sweng.project.view.adapter_view.MusicListAdapter;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;



/**
 * Created by Antoine Merino on 18/11/2016.
 */

public class TestMusicHistory {

    @ClassRule
    public static final ServiceTestRule mServiceRule = new ServiceTestRule();
    MusicHistory musicHistory = null;
    private Context context;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TestMusicHistory", "New music registered by modelApplication");
        }
    };

    public static void playSongIntent(Context context, String artist, String song) {
        try {
            mServiceRule.startService(
                    new Intent(InstrumentationRegistry.getTargetContext(), MusicInfoService.class));
        } catch (TimeoutException e) {
            Log.e("TestMusicHistory", e.toString());
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
            Log.e("TestMusicHistory", e.toString());
        }


    }

    @Before
    public void init() {
        context = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                "TestMusicHistory");
        ConnectionControler.getConnectionControler().sendPost(null, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK,
                GlobalTestSettings
                        .MOCK_ID_FACEBOOK,
                GlobalSetting.USER_API, true);
        musicHistory = MusicHistory.getMusicHistory();
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
        try {
            playSongIntent(context, GlobalTestSettings.ARTIST_NAME_REQUEST, GlobalTestSettings.MUSIC_NAME_REQUEST);
            //IntentFilter iF = new IntentFilter();
            //iF.addAction(GlobalSetting.INTENT_NEW_MUSIC);
            //context.registerReceiver(mReceiver, iF);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.e("testWithStartedService",e.getMessage());
            fail("Exception during the execution");
        }
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
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.e("testHistory",e.getMessage());
            fail("Exception during the execution");
        }
        musicList = musicHistory.getHistory();
        assertEquals(musicList.get(0).getArtist(), GlobalTestSettings.ARTIST_NAME_RESPONSE);
        assertEquals(musicList.get(0).getName(), GlobalTestSettings.MUSIC_NAME_RESPONSE);
    }
 
}
