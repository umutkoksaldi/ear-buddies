package ch.epfl.sweng.project.media;

import android.util.Log;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Antoine Merino on 28/10/2016.
 */
public class MusicInfoServiceTest {

    //----------------------------------------------------------------
    // Define constant

    // This is what we'll ask to the server. The typo mistakes are on purpose.
    private static final String ARTIST_NAME_REQUEST = "rihana";
    private static final String MUSIC_NAME_REQUEST = "umbrella";

    // This is what the server should answer
    private static final String ARTIST_NAME_TEST = "Rihanna";
    private static final String MUSIC_NAME_TEST = "Umbrella";
    private static final String TAG_TEST = "yolo";
    private static final String URL_TEST = "https://www.last.fm/music/Rihanna/_/Umbrella";
    private static final String USER_ID_REQUEST = "121620614972695";
    //----------------------------------------------------------------
    // Test
    private boolean testChecked = false;

    @Test
    public void shouldTestMusicInfoService() {

        final CountDownLatch latch = new CountDownLatch(1);

        Log.i("", "Should_test_music_info_service()");
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    Music musicTest = (Music) response.getBody();
                    assertEquals("artist name should be equal", ARTIST_NAME_TEST, musicTest.getArtist());
                    assertEquals("song name should be equal", MUSIC_NAME_TEST, musicTest.getName());
                    assertEquals("tag should be equal", TAG_TEST, musicTest.getTag());
                    assertEquals("LastFM URL should be equal", URL_TEST, musicTest.getUrl());
                    testChecked = true;
                } else {
                    assertTrue("The request fail", false);
                }
            }

            @Override
            public void onFailed() {
                assertTrue("The request failed", false);
            }

        });


        // Building the parameters for the
        Map<String, String> params = new HashMap<>();
        params.put(MusicInfoService.ARTIST_NAME, ARTIST_NAME_REQUEST);
        params.put(MusicInfoService.MUSIC_NAME, MUSIC_NAME_REQUEST);

        // the interface is already initiate above
        serviceHandler.doPost(params, GlobalSetting.URL + GlobalSetting.MUSIC_API + USER_ID_REQUEST, Music
                .class);
        try {
            latch.await(5, TimeUnit.SECONDS);
            assertTrue("The test is not executed.", testChecked);
        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);
        }

    }

}