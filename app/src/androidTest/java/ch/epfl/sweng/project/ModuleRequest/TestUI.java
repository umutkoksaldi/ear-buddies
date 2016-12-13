package ch.epfl.sweng.project.ModuleRequest;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import ch.epfl.sweng.project.GlobalTestSettings;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.media.MusicHistoryTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Etienne on 28.10.2016.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class TestUI {
 
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    @Rule
    public MockUserMainActivityRule mActivityRule = new MockUserMainActivityRule(MainActivity.class);

    @Test
    public void testCanClickFromMapToBlank() {
        Matcher<View> matcher = allOf(withText("Users"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

    @Test
    public void testCanClickFromMapToProfile() {
        Matcher<View> matcher = allOf(withText("Profile"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

    @Test
    public void testCanClickOnAllFragments() {
        Matcher<View> matcher = allOf(withText("Users"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        matcher = allOf(withText("Map"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        matcher = allOf(withText("Profile"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        matcher = allOf(withText("Map"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

    @Test
    public void testCanSwipeOnMap() {
        onView(withId(R.id.pagerMain)).perform(swipeRight());
        onView(withId(R.id.pagerMain)).perform(swipeLeft());
        onView(withId(R.id.pagerMain)).perform(swipeLeft());
        onView(withId(R.id.pagerMain)).perform(swipeRight());
    }

    @Test
    public void testHistoryFragment() {
        // Play a song that we know it's on Lastfm
        MusicHistoryTest.playSongIntent(mActivityRule.getActivity().getApplicationContext(), GlobalTestSettings
                .ARTIST_NAME_REQUEST, GlobalTestSettings.MUSIC_NAME_REQUEST);

        Matcher<View> matcher = allOf(withText("Profile"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());


        // Problem with webview testing, can't resume MainActivity ?
        // Open history fragment and click on the first music, which should display its lastfm page
        onView(withId(R.id.music_history_recyclerview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id
                        .single_row_music_history)));

        // Close the webview activity and go back to the main activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; ++i) {
            pressBack();
        }



    }




}