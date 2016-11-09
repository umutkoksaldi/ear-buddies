package ch.epfl.sweng.project.ModuleRequest;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.Toast;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Controler.ConnectionControler;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.facebook.FacebookSdk.getApplicationContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestConnexionUser {

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    //----------------------------------------------------------------
    // Define constant

    private static final String ID = "id";
    private static final String ACESS_TOKEN = "accesToken";

    public static final String  ID_FACEBOOK = "121620614972695";
    public static final String ACCESS_TOKEN_FACEBOOK =
            "EAAOZCzloFDqEBAHGnY8Q6I4d6fJRy9c6FWYZAqNxp2ChFBvpv8ZAycQC7a0oT21ZBp0Ku" +
            "IbZCIUkLWSH4Ev7pIQrjlzAxvrfznhXZAeb8A3ZCZBDks8WekNs4WgtfteZCMhUPQx5ZBPmbBMfwBgjqqAeaHOjtYFe38VYfXV35ZCnQ0y"+
            "ZBzPSDzCKDBBMkGhWA8ZAyrJAcBZA6LCi5XtgZDZD";

    //----------------------------------------------------------------
    // Constant User

    public static final int AGE_USER = 0;
    public static final String FIRSTNAME_USER = "Sweng";
    public static final String LASTNAME_USER = "Tests";
    // TODO à completer pour tester tous les parametes.

    //----------------------------------------------------------------
    // Test

    @Test
    public void Should_test_connexion_user() {

        final CountDownLatch latch = new CountDownLatch(1);

        // we call the server to
        controlerConnection.sendPost(null,ACCESS_TOKEN_FACEBOOK,ID_FACEBOOK, GlobalSetting.USER_API,true);

        try {
            latch.await(2, TimeUnit.SECONDS);
            // get the user in the model.
            User userTest = modelApplication.getUser();
            assertEquals("age should be equals", AGE_USER,userTest.getAge());
            assertEquals("first name should be equals",FIRSTNAME_USER,userTest.getFirstname());
            assertEquals("last name should be equals",LASTNAME_USER,userTest.getLastname());
            // TODO check the all parameters.

        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);

        }

    }

}
