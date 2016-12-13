package ch.epfl.sweng.project.testUi;

import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ModuleRequest.MockUserMainActivityRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.MAP_TAB;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.PROFILE_TAB;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.USERS_TAB;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Etienne on 28.10.2016.
 */

@RunWith(AndroidJUnit4.class)
public class TestUI {
 
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    @Rule
    public MockUserMainActivityRule mActivityRule = new MockUserMainActivityRule(MainActivity.class);

    @Test
    public void testCanClickFromMapToBlank() {
        Matcher<View> matcher = allOf(withText(USERS_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

    @Test
    public void testCanClickFromMapToProfile() {
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

    @Test
    public void testCanClickOnAllFragments() {
        Matcher<View> matcher = allOf(withText(USERS_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        matcher = allOf(withText(MAP_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        matcher = allOf(withText(MAP_TAB),
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

}