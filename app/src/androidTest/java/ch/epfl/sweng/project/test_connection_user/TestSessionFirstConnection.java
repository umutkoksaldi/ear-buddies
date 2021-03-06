package ch.epfl.sweng.project.test_connection_user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
import ch.epfl.sweng.project.view.activity.LoginActivity;
import ch.epfl.sweng.project.view.activity.WelcomeActivity;

import static android.support.test.internal.util.Checks.checkNotNull;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by arnauddupeyrat on 08/11/16.
 */
public class TestSessionFirstConnection extends ActivityInstrumentationTestCase2<WelcomeActivity> {


    /******************************
     * Set Constant
     *********************************************/

    private static final String TESTING_ALREADY_CONNECTED = "Testing_already_conected";
    private static final String TESTING_FIRST_CONNECTION = "Testing_frist_connection";
    @Rule
    public ActivityTestRule<WelcomeActivity> mActivityRule = new ActivityTestRule<>(
            WelcomeActivity.class);
    private Activity curActivity;


    public TestSessionFirstConnection() {
        super(WelcomeActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(TESTING_FIRST_CONNECTION, "true");
        bundle.putString(GlobalSetting.ID, GlobalTestSettings.MOCK_ID_FACEBOOK);
        bundle.putString(GlobalSetting.ACCESS_TOKEN, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK);

        intent.putExtras(bundle);
        // Set the intent.

        // Set the intent.
        setActivityIntent(intent);
        getActivity();
    }

    /**
     * Testing actions linked to the button login.
     */

    public void firstUserConnection() {

        final CountDownLatch latch = new CountDownLatch(1);

        getActivity();


        try {
            latch.await(2, TimeUnit.SECONDS);
            // check the foreground activity.
            assertCurrentActivityInstanceOf(LoginActivity.class);
        } catch (InterruptedException e) {
            assertTrue("Error in the time waiting", false);

        }
    }


    public void assertCurrentActivityInstanceOf(Class<? extends AppCompatActivity> activityClass) {
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
