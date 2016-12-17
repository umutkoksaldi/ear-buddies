package ch.epfl.sweng.project.testUi;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.ModuleRequest.MockUserMainActivityRule;
import ch.epfl.sweng.project.ModuleRequest.MyViewAction;
import ch.epfl.sweng.project.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.PROFILE_TAB;
import static ch.epfl.sweng.project.utils.GlobalTestSettings.USERS_TAB;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by arnauddupeyrat on 15/12/16.
 */

@RunWith(AndroidJUnit4.class)
public class TestDetailFragment {


    public static String NAME_FIRST_USER = "Name Test 1";
    public static String NAME_ARTIST = "Rihana";
    @Rule
    public MockUserMainActivityRule mActivityRule = new MockUserMainActivityRule(MainActivity.class);

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    @Test
    public void testDisplayUsers() {

        try {

            Matcher<View> matcher = allOf(withText(USERS_TAB),
                    isDescendantOfA(withId(R.id.tabLayoutMain)));
            onView(matcher).perform(click());


            Thread.sleep(1000);

            // check if the list contain contain all occurence of the.
            List<User> items = Arrays.asList(modelApplication.getOtherUsers());
            RecyclerViewInteraction.<User>onRecyclerView(withId(R.id.user_recyclerview))
                    .withItems(items)
                    .check(new RecyclerViewInteraction.ItemViewAssertion<User>() {
                        @Override
                        public void check(User item, View view, NoMatchingViewException e) {
                            matches(hasDescendant(withText(item.getFirstname())))
                                    .check(view, e);
                        }
                    });
        }
        catch(Exception e){
            Log.e("testDisplayUsers",e.getMessage());
            fail();
        }
    }

    @Test
    public void ClickOnBackButton() {

        try {

            Matcher<View> matcher = allOf(withText(USERS_TAB),
                    isDescendantOfA(withId(R.id.tabLayoutMain)));
            onView(matcher).perform(click());

            Thread.sleep(1000);

            // Click on first element of the list
            onView(withId(R.id.user_recyclerview))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            Thread.sleep(1000);

            // check if the fragment display something.

            Espresso.pressBack();

            Thread.sleep(1000);


            List<User> items = Arrays.asList(modelApplication.getOtherUsers());
            RecyclerViewInteraction.<User>onRecyclerView(withId(R.id.user_recyclerview))
                    .withItems(items)
                    .check(new RecyclerViewInteraction.ItemViewAssertion<User>() {
                        @Override
                        public void check(User item, View view, NoMatchingViewException e) {
                            matches(hasDescendant(withText(item.getFirstname())))
                                    .check(view, e);
                        }
                    });

        } catch (Exception e) {
            Log.e("testDisplayUsers", e.getMessage());
            fail();
        }
    }

    @Test
    public void checkListUsers() {

        try {

            Matcher<View> matcher = allOf(withText(USERS_TAB),
                    isDescendantOfA(withId(R.id.tabLayoutMain)));
            onView(matcher).perform(click());

            Thread.sleep(1000);

            // Click on first element of the list
            onView(withId(R.id.user_recyclerview))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            Thread.sleep(1000);

            // check user name.
            onView(allOf(withText(NAME_FIRST_USER),
                    isDescendantOfA(withId(R.id.details_frag))))
                    .check(matches(isDisplayed()));


        } catch (Exception e) {
            Log.e("testDisplayUsers", e.getMessage());
            fail();
        }
    }


    @Test
    public void ClickOnFacebookButton() {

        try {

            Matcher<View> matcher = allOf(withText(USERS_TAB),
                    isDescendantOfA(withId(R.id.tabLayoutMain)));
            onView(matcher).perform(click());

            Thread.sleep(1000);

            // Click on first element of the list
            onView(withId(R.id.user_recyclerview))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            Thread.sleep(1000);

            // check if the fragment display something.

            Matcher<View> matcherFacebookButton = allOf(withId(R.id.button_details_facebook),
                    isDescendantOfA(withId(R.id.details_frag)));
            onView(matcherFacebookButton).perform(click());

            Thread.sleep(1000);
            // TODO check if the webview is opened.

        } catch (Exception e) {
            Log.e("testDisplayUsers", e.getMessage());
            fail();
        }
    }



}
