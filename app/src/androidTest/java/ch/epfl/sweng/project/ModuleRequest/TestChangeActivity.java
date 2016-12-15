package ch.epfl.sweng.project.ModuleRequest;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;

import com.facebook.FacebookSdk;

import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Controler.ConnectionControler;
import ch.epfl.sweng.project.utils.GlobalTestSettings;
import ch.epfl.sweng.project.Login;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

import static android.support.test.internal.util.Checks.checkNotNull;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

@SuppressWarnings("deprecation")
public class TestChangeActivity extends ActivityInstrumentationTestCase2<MainActivity> {


    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    private Activity curActivity;

    public TestChangeActivity() {
        super(MainActivity.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        final CountDownLatch latch = new CountDownLatch(1);

        // we call the server to
        controlerConnection.sendPost(null, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK, GlobalTestSettings
                .MOCK_ID_FACEBOOK,
                GlobalSetting.USER_API, true);

        try {
            latch.await(2, TimeUnit.SECONDS);
            // Check the request is okay.
            User userTest = modelApplication.getUser();
            assertEquals("first name should be equals", GlobalTestSettings.MOCK_USER_FIRST_NAME, userTest.getFirstname());

        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);

        }

        getActivity();
    }

    /**
     * Testing actions linked to the button login.
     */
    @Test
    public void test_new_user() {

        final CountDownLatch latch = new CountDownLatch(1);

        getActivity();
        // change the activity
        controlerConnection.changeActivity(getActivity(), Login.class, new HashMap<String, String>());

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

    public Activity getActivityInstance() {

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage
                        (RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    setCurrentActivity((Activity) resumedActivities.iterator().next());
                }
            }
        });

        return curActivity;
    }

    private void setCurrentActivity(Activity activity) {
        curActivity = activity;
    }

}
