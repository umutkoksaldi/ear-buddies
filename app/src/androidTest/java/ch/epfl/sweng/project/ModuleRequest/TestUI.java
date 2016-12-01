package ch.epfl.sweng.project.ModuleRequest;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Matcher;

import ch.epfl.sweng.project.GlobalTestSettings;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;

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
        //User userTest = new User();
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
}