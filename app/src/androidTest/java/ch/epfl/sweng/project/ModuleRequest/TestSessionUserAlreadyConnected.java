package ch.epfl.sweng.project.ModuleRequest;

import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.WelcomeActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.internal.util.Checks.checkNotNull;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static junit.framework.Assert.assertTrue;

/**
 * Created by arnauddupeyrat on 08/11/16.
 */

@RunWith(AndroidJUnit4.class)
public class TestSessionUserAlreadyConnected {


    /******************************
     * Set Constant
     *********************************************/


    private static final String TESTING_ALREADY_CONNECTED = "Testing_already_conected";
    @Rule
    public AlreadyConnectedWelcomeRule<WelcomeActivity> mActivityRule = new AlreadyConnectedWelcomeRule<>(
            WelcomeActivity.class);
    private Activity curActivity;


    /**
     * Testing actions linked to the button login.
     */

    /*@Before
    public void launchActivity(){
        Log.d("TestSessionUserAlrea...","launchActivity()");
        Intent intent = mActivityRule.getActivityIntent();
        mActivityRule.launchActivity(intent);
    }*/
    @Test
    public void testSessionConnection() {
        Log.d("TestSessionUserAlrea...", "testSessionConnection()");
        final CountDownLatch latch = new CountDownLatch(1);

        try {
            latch.await(10, TimeUnit.SECONDS);
            // check the foreground activity.
            assertCurrentActivityIsInstanceOf(MainActivity.class);

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
