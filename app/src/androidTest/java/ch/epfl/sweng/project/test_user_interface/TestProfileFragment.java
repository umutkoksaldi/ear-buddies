package ch.epfl.sweng.project.test_user_interface;

import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
import ch.epfl.sweng.project.util_rule.MockUserMainActivityRule;
import ch.epfl.sweng.project.view.activity.LoginActivity;
import ch.epfl.sweng.project.view.activity.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.util.Checks.checkNotNull;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_PROFILE;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.BUTTON_CANCEL;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.BUTTON_OK;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_DESCRIPTION;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_FIRST_NAME;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.PROFILE_TAB;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Dusan Viktor on 2016-12-17.
 */
@RunWith(AndroidJUnit4.class)
public class TestProfileFragment {
    private static final String USER_NAME = "Melania";
    private static final String USER_DESCRIPTION = "Trumpete";
    public static List<String> NAME_SONG = Arrays.asList("Rock", "Pop", "Metal");
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    @Rule
    public MockUserMainActivityRule mActivityRule = new MockUserMainActivityRule(MainActivity.class);
    public GlobalTestSettings globalTestSettings;
    private Activity curActivity;

    @Test
    public void testClickTheDistanceButton() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        onView(withId(R.id.button_profile_radar))
                .perform(click());
        assertThat(viewPager.getCurrentItem(), is(FRAGMENT_PROFILE));
    }


    @Test
    public void testEditName() {
        // Go to profile fragment
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());

        // Change name to USER_NAME and check if it's changed
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText(R.string.menu_edit_name))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(modelApplication.getUser().getFirstname()))
                .check(matches(isDisplayed()))
                .perform(replaceText(USER_NAME));
        onView(withId(BUTTON_OK))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(USER_NAME))
                .check(matches(isDisplayed()));

        // Go to the menu again but cancel action
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText(R.string.menu_edit_name))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(BUTTON_CANCEL))
                .check(matches(isDisplayed()))
                .perform(click());

        // Change back to the previous name and check if it's been changed
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText(R.string.menu_edit_name))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(modelApplication.getUser().getFirstname()))
                .check(matches(isDisplayed()))
                .perform(replaceText(MOCK_USER_FIRST_NAME));
        onView(withId(BUTTON_OK))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(MOCK_USER_FIRST_NAME))
                .check(matches(isDisplayed()));

        assertThat(viewPager.getCurrentItem(), is(FRAGMENT_PROFILE));

    }

    @Test
    public void testEditDescription() {
        // Go to profile fragment
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());

        // Go to change description menu but cancel action
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText(R.string.menu_edit_description))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(BUTTON_CANCEL))
                .check(matches(isDisplayed()))
                .perform(click());


        // Go again in description and do the action
        onView(withId(R.id.button_profile_menu))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(R.string.menu_edit_description))
                .check(matches(isDisplayed()))
                .perform(click());
        String currentDescription;
        if (modelApplication.getUser().getDescription() != null) {
            currentDescription = modelApplication.getUser().getDescription();
        } else {
            currentDescription = getInstrumentation().getTargetContext().getResources().getString(R.string
                    .default_description);
        }
        onView(withText(currentDescription))
                .perform(replaceText(USER_DESCRIPTION));
        onView(withId(BUTTON_OK))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(USER_DESCRIPTION))
                .check(matches(isDisplayed()));

        // Change the description back to the first one
        onView(withId(R.id.button_profile_menu))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(R.string.menu_edit_description))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(USER_DESCRIPTION))
                .perform(replaceText(MOCK_USER_DESCRIPTION));
        onView(withId(BUTTON_OK))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(MOCK_USER_DESCRIPTION))
                .check(matches(isDisplayed()));

        assertThat(viewPager.getCurrentItem(), is(FRAGMENT_PROFILE));

    }

    @Test
    public void testDeleteAcc() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText(R.string.menu_delete_account))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(BUTTON_CANCEL))
                .check(matches(isDisplayed()))
                .perform(click());
        assertThat(viewPager.getCurrentItem(), is(FRAGMENT_PROFILE));

    }


    @Test
    public void testLogout() {
        // Go to profile
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());

        // Go to logout menu but cancel the action
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText(R.string.menu_logout))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(BUTTON_CANCEL))
                .check(matches(isDisplayed()))
                .perform(click());

        assertThat(viewPager.getCurrentItem(), is(FRAGMENT_PROFILE));

        // Go to the menu again and validate the logout action
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText(R.string.menu_logout))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(BUTTON_OK))
                .check(matches(isDisplayed()))
                .perform(click());

        // The user should be redirected to the login activity
        assertCurrentActivityIsInstanceOf(LoginActivity.class);
    }


    @Test
    public void clickOnMusicTasteSlection() {

        try {


            Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                    isDescendantOfA(withId(R.id.tabLayoutMain)));
            onView(matcher).perform(click());

            sleep(1000);

            onView(withId(R.id.button_profile_music_tag)).perform(click());

            sleep(1000);

            // check if the song is displayed
            for (String elementNameSong : NAME_SONG) {
                onView(withText(elementNameSong)).check(matches(isDisplayed()));
            }

            onView(withText(NAME_SONG.get(0))).perform(click());

            sleep(2000);

            onView(withText(NAME_SONG.get(0).toLowerCase())).check(matches(isDisplayed()));

            try {
                onView(withText(NAME_SONG.get(0))).check(matches(isDisplayed()));
            } catch (Exception e) {
                return;
            }

        } catch (Exception e) {
            Log.e("clickOnMusic", e.getMessage());
            fail();
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



