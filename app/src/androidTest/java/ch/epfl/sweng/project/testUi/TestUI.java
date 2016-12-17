package ch.epfl.sweng.project.testUi;

import android.support.test.espresso.AppNotIdleException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.Swipe;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.Fragment.DetailsFragment;
import ch.epfl.sweng.project.Fragment.ProfileFrag;
import ch.epfl.sweng.project.Fragment.UsersFragment;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.ModuleRequest.MockUserMainActivityRule;
import ch.epfl.sweng.project.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.MAP_TAB;
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
        Matcher<View> matcher = allOf(withText(USERS_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
    }

    @Test
    public void testCanClickFromMapToProfile() {
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        ViewPager viewPager =(ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        viewPager.getCurrentItem();
        assertThat(2,is(viewPager.getCurrentItem()));
    }

    @Test
    public void testCanClickOnAllFragments() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(USERS_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        assertThat(0,is(viewPager.getCurrentItem()));
        matcher = allOf(withText(MAP_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        assertThat(1,is(viewPager.getCurrentItem()));
        matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        assertThat(2,is(viewPager.getCurrentItem()));
        matcher = allOf(withText(MAP_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        assertThat(1,is(viewPager.getCurrentItem()));
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