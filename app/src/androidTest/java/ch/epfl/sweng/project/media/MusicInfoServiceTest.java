package ch.epfl.sweng.project.media;

import android.util.Log;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Antoine Merino on 28/10/2016.
 */
public class MusicInfoServiceTest {

    //----------------------------------------------------------------
    // Define constant

    public static final String ARTIST_NAME_TEST = "Moby";
    public static final String MUSIC_NAME_TEST = "Flower";

    //----------------------------------------------------------------
    // Constant Music
    private static final String ARTIST_NAME = "artistName";
    private static final String MUSIC_NAME = "musicName";

    //----------------------------------------------------------------
    // Test
    private boolean testChecked = false;

    @Test
    public void Should_test_music_info_service() {

        final CountDownLatch latch = new CountDownLatch(1);

        Log.i("", "Should_test_music_info_service()");
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    User userTest = (User) response.getBody();
                    assertEquals("age should be equals", AGE_USER, userTest.getAge());
                    assertEquals("first name should be equals", FIRSTNAME_USER, userTest.getFirstname());
                    assertEquals("last name should be equals", LASTNAME_USER, userTest.getLastname());
                    testChecked = true;
                } else {
                    assertTrue("The request fail", false);
                }
            }

            @Override
            public void onFailed() {
                assertTrue("The request fail, error 404", false);
            }

        });


        // Building the parameters for the
        Map<String, String> params = new HashMap<>();
        params.put(ID, ID_FACEBOOK);
        params.put(ACESS_TOKEN, ACCESS_TOKEN_FACEBOOK);

        // the interface is already initiate above
        serviceHandler.doPost(params, GlobalSetting.URL + GlobalSetting.USER_API, User.class);

        try {
            latch.await(2, TimeUnit.SECONDS);
            assertTrue("The test is not executed.", testChecked);
        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);
        }

    }

}