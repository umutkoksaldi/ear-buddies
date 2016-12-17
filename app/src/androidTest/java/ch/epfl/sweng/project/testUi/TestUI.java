package ch.epfl.sweng.project.testUi;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.ModuleRequest.MockUserMainActivityRule;
import ch.epfl.sweng.project.R;

import static Util.GlobalSetting.FRAGMENT_MAP;
import static Util.GlobalSetting.FRAGMENT_PROFILE;
import static Util.GlobalSetting.FRAGMENT_USERS;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.PROFILE_TAB;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.USERS_TAB;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(USERS_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        assertThat(FRAGMENT_USERS, is(viewPager.getCurrentItem()));

    }

    @Test
    public void testCanClickFromMapToProfile() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        viewPager.getCurrentItem();
        assertThat(FRAGMENT_PROFILE, is(viewPager.getCurrentItem()));

    }

    @Test
    public void testPressBackBehavior() {
        // Go to users fragment
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(USERS_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        assertThat(FRAGMENT_USERS, is(viewPager.getCurrentItem()));

        // Then press back to go back to the map
        pressBack();
        assertThat(FRAGMENT_MAP, is(viewPager.getCurrentItem()));

        // Go to profile fragment
        matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        assertThat(FRAGMENT_PROFILE, is(viewPager.getCurrentItem()));

        // Then press back to go back to the map
        pressBack();
        assertThat(FRAGMENT_MAP, is(viewPager.getCurrentItem()));

        // Press back again, it should leave the app
        try {
            pressBack();
        } catch (NoActivityResumedException e) {
            Assert.assertThat(e.getMessage().startsWith("Pressed back and killed the app"), is(true));
        }
    }

    @Test
    public void testCanSwipeOnMap() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        onView(withId(R.id.pagerMain)).perform(swipeRight());
        onView(withId(R.id.pagerMain)).perform(swipeLeft());
        onView(withId(R.id.pagerMain)).perform(swipeLeft());
        onView(withId(R.id.pagerMain)).perform(swipeRight());
    }

    @Test
    public void profileSwipeToRefresh() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        onView(withId(R.id.pagerMain)).perform(swipeDown());
        assertThat(2, is(viewPager.getCurrentItem()));
    }

    @Test
    public void usersSwipeToRefresh() throws InterruptedException {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);


        Matcher<View> matcher1 = allOf(withText(USERS_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher1).perform(click());
        onView(matcher1).perform(swipeDown());
        onView(matcher1).check(matches(isDisplayed()));

    }



}