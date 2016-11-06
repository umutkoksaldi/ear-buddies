package ch.epfl.sweng.project.ModuleRequest;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestConnexionUser {

    //----------------------------------------------------------------
    // Define constant

    public static final String ID_FACEBOOK = "121620614972695";
    public static final String ACCESS_TOKEN_FACEBOOK =
            "EAAOZCzloFDqEBAHGnY8Q6I4d6fJRy9c6FWYZAqNxp2ChFBvpv8ZAycQC7a0oT21ZBp0Ku" +
                    "IbZCIUkLWSH4Ev7pIQrjlzAxvrfznhXZAeb8A3ZCZBDks8WekNs4WgtfteZCMhUPQx5ZBPmbBMfwBgjqqAeaHOjtYFe38VYfXV35ZCnQ0y" +
                    "ZBzPSDzCKDBBMkGhWA8ZAyrJAcBZA6LCi5XtgZDZD";
    public static final int AGE_USER = 0;
    public static final String FIRSTNAME_USER = "Sweng";

    //----------------------------------------------------------------
    // Constant User
    public static final String LASTNAME_USER = "Tests";
    private static final String ID = "id";
    private static final String ACESS_TOKEN = "accesToken";
    // TODO à completer pour tester tous les parametes.

    //----------------------------------------------------------------
    // Test
    private boolean testChecked = false;

    @Test
    public void Should_test_connexion_user() {

        final CountDownLatch latch = new CountDownLatch(1);

        Log.i("","Should_test_connexion_user()");
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    User userTest = (User) response.getBody();
                    assertEquals("age should be equals", AGE_USER,userTest.getAge());
                    assertEquals("first name should be equals",FIRSTNAME_USER,userTest.getFirstname());
                    assertEquals("last name should be equals",LASTNAME_USER,userTest.getLastname());
                    checkValue();
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
            latch.await(10, TimeUnit.SECONDS);
            assertTrue("The test is not executed.", testChecked);
        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);
        }

    }

    private void checkValue() {
        testChecked = true;
    }

}