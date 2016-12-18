package ch.epfl.sweng.project.test_user_interface;

import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
import ch.epfl.sweng.project.util_rule.MockUserMainActivityRule;
import ch.epfl.sweng.project.view.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.FRAGMENT_PROFILE;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_FIRST_NAME;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.PROFILE_TAB;
import static java.lang.Thread.sleep;
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
        onView(withId(android.R.id.button1))
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
        onView(withId(android.R.id.button2))
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
        onView(withId(android.R.id.button1))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(MOCK_USER_FIRST_NAME))
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
        onView(withText("Delete account"))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Cancel"))
                .check(matches(isDisplayed()))
                .perform(click());
        assertThat(viewPager.getCurrentItem(), is(2));

    }

    @Test
    public void testEditDescription() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        TextView name = (TextView) viewPager.findViewById(R.id.menu_edit_description);
        onView(matcher).perform(click());
        onView(withId(R.id.button_profile_menu))
                .perform(click());

        onView(withText("Edit description"))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Cancel"))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.button_profile_menu))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText("Edit description"))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Choose your description"))
                .perform(replaceText(USER_DESCRIPTION));
        onView(withText("OK"))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(USER_DESCRIPTION))
                .check(matches(isDisplayed()));

        assertThat(viewPager.getCurrentItem(), is(2));

    }

    @Test
    public void testLogout() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        onView(withId(R.id.button_profile_menu))
                .perform(click());
        onView(withText("Logout"))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Cancel"))
                .check(matches(isDisplayed()))
                .perform(click());

        assertThat(viewPager.getCurrentItem(), is(2));

    }


    @Test
    public void testBackButtonOnEveryClickedButton() {
        ViewPager viewPager = (ViewPager) mActivityRule.getActivity().findViewById(R.id.pagerMain);
        Matcher<View> matcher = allOf(withText(PROFILE_TAB),
                isDescendantOfA(withId(R.id.tabLayoutMain)));
        onView(matcher).perform(click());
        Espresso.pressBack();
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

            sleep(1000);

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
}



