package ch.epfl.sweng.project.test_model;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.models.User;

import static ch.epfl.sweng.project.util_constant.GlobalSetting.SNIPPED_DESCRIPTION_LENGTH;
import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MOCK_USER_EMAIL;
import static org.junit.Assert.assertEquals;

/**
 * Created by Antoine Merino on 23/12/2016.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestUser {

    @Test
    public void testEmail() {
        User user = new User();
        user.setEmail(MOCK_USER_EMAIL);
        assertEquals(user.getEmail(), MOCK_USER_EMAIL);
    }

    @Test
    public void longDescription() {
        User user = new User();
        String longDesc = "Very long description";
        String points = new String(new char[SNIPPED_DESCRIPTION_LENGTH]).replace('\0', '.');
        longDesc += points;
        // The description should be cropped
        user.setDescrition(longDesc);
        String expected = longDesc.substring(0, SNIPPED_DESCRIPTION_LENGTH - 1) + "...";
        assertEquals(user.getSnippetDescription(), expected);
    }

}
