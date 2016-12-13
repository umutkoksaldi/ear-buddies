package ch.epfl.sweng.project.testUi;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.media.MusicHistoryTest;
import ch.epfl.sweng.project.ModuleRequest.MockUserMainActivityRule;
import ch.epfl.sweng.project.ModuleRequest.MyViewAction;
import ch.epfl.sweng.project.utils.GlobalTestSettings;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.PROFILE_TAB;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by arnauddupeyrat on 13/12/16.
 */

@RunWith(AndroidJUnit4.class)
public class TestHistoryMusic {

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    @Rule
    public MockUserMainActivityRule mActivityRule = new MockUserMainActivityRule(MainActivity.class);


    @Test
    public void testHistoryFragment() {

        try {
            // Play a song that we know it's on Lastfm
            MusicHistoryTest.playSongIntent(mActivityRule.getActivity().getApplicationContext(), GlobalTestSettings
                    .ARTIST_NAME_REQUEST, GlobalTestSettings.MUSIC_NAME_REQUEST);

            Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                    isDescendantOfA(withId(R.id.tabLayoutMain)));
            onView(matcher).perform(click());

            // Open history fragment ansd click on the first music, which should display its lastfm page
/*            onView(withId(R.id.music_history_recyclerview)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id
                            .single_row_music_history)));*/

        // Close the webview activity and go back to the main activity
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("testHistoryFragment", e.getMessage());
            fail("Exception during the execution");
        }


        // Test if the Web fragment on lastFm is opened.
/*        ActivityManager am = (ActivityManager) mActivityRule.getActivity().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Log.d("CURRENT Activity ::" , taskInfo.get(0).topActivity.getClassName()+"   Package Name :  "+componentInfo
                .getPackageName());*/
    }

}
