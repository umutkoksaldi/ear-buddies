package ch.epfl.sweng.project.Fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.hamcrest.Matcher;

import ch.epfl.sweng.project.ActivityForFragmentsTest;
import ch.epfl.sweng.project.GlobalTestSettings;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Dusan Viktor on 2016-12-06.
 */

public class DetailsFragmentTest extends ActivityInstrumentationTestCase2<ActivityForFragmentsTest> {

    private ActivityForFragmentsTest myFragmentActivity;
    private DetailsFragment myFragment;
    private TextView name;
    private TextView desc;
    private TextView musicId;
    private User user;
    private User userDetails;
    private MapFrag mapFrag;
    private Button back;
    private Activity nextActivity;
    private GlobalTestSettings gts;
    public DetailsFragmentTest() {
        super(ActivityForFragmentsTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFragmentActivity = getActivity();
        myFragment = new DetailsFragment();
        user = new User();
        back = (Button)myFragmentActivity.findViewById(R.id.details_back_button);
        user.setDescrition("Love, sport, Rock ´n´ Roll");
        user.setFirstname("Nicolas");
        user.setCurrentMusicId(20161206);
        user.setProfilePicture("111");
        myFragment.setUser(user);
        userDetails = myFragment.detailsGetUser();
    }

    public void testPreConditions() {
        assertNotNull(myFragmentActivity);
        assertNotNull(myFragment);
    }
    public void testUser(){
        assertNotNull(user);
    }

    public void testUserName(){
        assertEquals(user.getFirstname(),userDetails.getFirstname() );
    }
    public void testUserCurrentId(){
        assertEquals(user.getCurrentMusicId(), userDetails.getCurrentMusicId());
    }
    public void testUserDescription(){
        assertEquals(user.getDescription(), userDetails.getDescription());
    }
    public void testPictureUrld() {
        assertEquals(user.getProfilePicture(), userDetails.getProfilePicture());
    }
        @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
