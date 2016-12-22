package ch.epfl.sweng.project.test_module_request;

import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.app.AppCompatActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.project.controlers.ConnectionControler;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.util_rule.MockUserRule;
import ch.epfl.sweng.project.view.activity.LoginActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.internal.util.Checks.checkNotNull;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestChangeActivity {


    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();

    @Rule
    public MockUserRule<LoginActivity> mActivityRule = new MockUserRule<>(LoginActivity.class);

    private Activity curActivity;


    /**
     * Testing actions linked to the button login.
     */
    @Test
    public void testNewUser() {

        final CountDownLatch latch = new CountDownLatch(1);

        // change the activity
        controlerConnection.changeActivity(mActivityRule.getActivity(), LoginActivity.class, new HashMap<String,
                String>());

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
