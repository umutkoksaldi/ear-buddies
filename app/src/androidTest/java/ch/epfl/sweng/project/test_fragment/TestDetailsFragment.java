package ch.epfl.sweng.project.test_fragment;


import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.view.fragment.DetailsFragment;
import ch.epfl.sweng.project.view.fragment.MapFragment;

import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_DESCRIPTION;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_FIRST_NAME;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_PROFILE_PICTURE;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_SONG_ID;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Dusan Viktor on 2016-12-06.
 */
@RunWith(AndroidJUnit4.class)
public class TestDetailsFragment {

    private DetailsFragment myFragment;
    private TextView name;
    private TextView desc;
    private TextView musicId;
    private User user;
    private User userDetails;
    private MapFragment mapFragment;
    private Activity nextActivity;

    @Before
    public void setUp() throws Exception {

        myFragment = new DetailsFragment();
        user = new User();
        user.setDescrition(MOCK_USER_DESCRIPTION);
        user.setFirstname(MOCK_USER_FIRST_NAME);
        user.setCurrentMusicId(MOCK_USER_SONG_ID);
        user.setProfilePicture(MOCK_USER_PROFILE_PICTURE);
        myFragment.setUser(user);
        userDetails = myFragment.detailsGetUser();
    }

    @Test
    public void checkValueFragment() {
        assertNotNull(myFragment);
        assertNotNull(user);
        assertEquals(user.getFirstname(), userDetails.getFirstname());
        assertEquals(user.getCurrentMusicId(), userDetails.getCurrentMusicId());
        assertEquals(user.getDescription(), userDetails.getDescription());
        assertEquals(user.getProfilePicture(), userDetails.getProfilePicture());
    }


}