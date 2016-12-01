package ch.epfl.sweng.project.ModuleRequest;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Controler.ConnectionControler;
import ch.epfl.sweng.project.GlobalTestSettings;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestConnectionUser {

    //----------------------------------------------------------------
    // Define constant
    public static final int AGE_USER = 0;
    public static final String FIRSTNAME_USER = "Sweng";
    public static final String LASTNAME_USER = "Tests";
    private static final String ID = "id";

    //----------------------------------------------------------------
    // Constant User
    private static final String ACESS_TOKEN = "accesToken";
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    // TODO à completer pour tester tous les parametes.

    //----------------------------------------------------------------
    // Test

    @Test
    public void Should_test_connexion_user() {

        final CountDownLatch latch = new CountDownLatch(1);

        // we call the server to
        controlerConnection.sendPost(null, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK, GlobalTestSettings
                .MOCK_ID_FACEBOOK,
                GlobalSetting
                        .USER_API, true);

        try {
            latch.await(2, TimeUnit.SECONDS);
            // get the user in the model.
            User userTest = modelApplication.getUser();
            assertEquals("age should be equals", AGE_USER, userTest.getAge());
            assertEquals("first name should be equals", FIRSTNAME_USER, userTest.getFirstname());
            assertEquals("last name should be equals", LASTNAME_USER, userTest.getLastname());
            // TODO check the all parameters.

        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);

        }

    }

}
