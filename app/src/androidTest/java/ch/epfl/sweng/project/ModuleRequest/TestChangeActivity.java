package ch.epfl.sweng.project.ModuleRequest;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;


import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Controler.ConnectionControler;
import ch.epfl.sweng.project.Login;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.WelcomeActivity;

import static android.support.test.internal.util.Checks.checkNotNull;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

@SuppressWarnings("deprecation")
public class TestChangeActivity extends ActivityInstrumentationTestCase2<WelcomeActivity> {


    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    private Context context;
    private Activity currentActivity;
    public static final String FIRSTNAME_USER = "Sweng";

    public TestChangeActivity() {
        super(WelcomeActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

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
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        context = getInstrumentation().getTargetContext().getApplicationContext();
        FacebookSdk.sdkInitialize(context);
    }

    /**
     * Testing actions linked to the button login.
     */
    public void test_new_user() {

        final CountDownLatch latch = new CountDownLatch(1);

        // The user is connected for the first time.
        Profile.setCurrentProfile(null);

        getActivity();

        try {
            latch.await(2, TimeUnit.SECONDS);

            // check the foreground activity.
            assertCurrentActivityIsInstanceOf(Login.class);
        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);
        }

    }

    public void assertCurrentActivityIsInstanceOf(Class<? extends AppCompatActivity> activityClass) {
        Activity currentActivity = getActivityInstance();
        checkNotNull(currentActivity);
        checkNotNull(activityClass);
        assertTrue(currentActivity.getClass().isAssignableFrom(activityClass));
    }

    public Activity getActivityInstance(){

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    setCurrentActivity((Activity) resumedActivities.iterator().next());
                }
            }
        });

        return currentActivity;
    }

    private void setCurrentActivity (Activity activity){
        currentActivity = activity;
    }

}
