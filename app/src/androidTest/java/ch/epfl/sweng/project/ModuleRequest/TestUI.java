package ch.epfl.sweng.project.ModuleRequest;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.facebook.FacebookSdk;

import org.hamcrest.Matcher;

import ch.epfl.sweng.project.GlobalTestSettings;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.media.MusicHistoryTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Etienne on 28.10.2016.
 */

public class TestUI extends ActivityInstrumentationTestCase2<MainActivity> {

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    public TestUI() {
        super(MainActivity.class);
    }

    @Override
    public void setUp(){
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        GlobalTestSettings.createFakeUser();
        User userTest = modelApplication.getUser();
        userTest.setLocation(new Location());
        modelApplication.setUser(userTest);
        getActivity();
    }

    public void testCanClickFromMapToBlank() {
        Matcher<View> matcher = allOf(withText("Users"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

    public void testCanClickFromMapToProfile() {
        Matcher<View> matcher = allOf(withText("Profile"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

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

    public void testCanSwipeOnMap() {
        onView(withId(R.id.pagerMain)).perform(swipeRight());
        onView(withId(R.id.pagerMain)).perform(swipeLeft());
        onView(withId(R.id.pagerMain)).perform(swipeLeft());
        onView(withId(R.id.pagerMain)).perform(swipeRight());
    }

    public void testHistoryFragment() {
        // Play a song that we know it's on Lastfm
        MusicHistoryTest.playSongIntent(getActivity().getApplicationContext(), GlobalTestSettings
                .ARTIST_NAME_REQUEST, GlobalTestSettings.MUSIC_NAME_REQUEST);

        Matcher<View> matcher = allOf(withText("Profile"),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        // Open history fragment and click on the first music, which should display its lastfm page
        onView(withId(R.id.music_history_recyclerview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id
                        .single_row_music_history)));

    }




}