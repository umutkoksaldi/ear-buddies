package ch.epfl.sweng.project.test_media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ch.epfl.sweng.project.controlers.ConnectionControler;
import ch.epfl.sweng.project.medias.MusicHistory;
import ch.epfl.sweng.project.medias.MusicInfoService;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
import ch.epfl.sweng.project.view.adapter_view.MusicListAdapter;

import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MAX_ITERATION_BIND;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.createMockUser;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;



/**
 * Created by Antoine Merino on 18/11/2016.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestMusicHistory {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();
    MusicHistory musicHistory = null;
    private Context context;
    private IBinder binder;
    private MusicInfoService service;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TestMusicHistory", "New music registered by modelApplication");
        }
    };

    public static void playSongIntent(Context context, String artist, String song) {
        // Inform the service that a new song will be played
        Intent trackIntent = new Intent("com.android.music.metachanged");
        trackIntent.putExtra("artist", artist);
        trackIntent.putExtra("track", song);
        trackIntent.putExtra("playing", true);
        context.sendBroadcast(trackIntent);
    }

    @Before
    public void init() throws TimeoutException {
        context = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                "TestMusicHistory");
        ConnectionControler.getConnectionControler().sendPost(null, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK,
                GlobalTestSettings
                        .MOCK_ID_FACEBOOK,
                GlobalSetting.USER_API, true);
        createMockUser();
        musicHistory = MusicHistory.getMusicHistory();


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
    }

    @Test
    public void singletonInitialization() {
        boolean initialized = false;
        if (musicHistory != null) {
            initialized = true;
        }
        assertThat(initialized, is(true));
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

    @Test
    public void playingSong() { 
        try {
            playSongIntent(context, GlobalTestSettings.ARTIST_NAME_REQUEST, GlobalTestSettings.MUSIC_NAME_REQUEST);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Log.e("testWithStartedService",e.getMessage());
            fail("Exception during the execution");
        }
        Music newMusic = ModelApplication.getModelApplication().getMusic();
        assertEquals("Artist names should be equals", GlobalTestSettings.ARTIST_NAME_RESPONSE, newMusic.getArtist());
        assertEquals("Song names should be equals", GlobalTestSettings.MUSIC_NAME_RESPONSE, newMusic.getName());

    }
 
}
