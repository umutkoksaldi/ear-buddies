package ch.epfl.sweng.project.test_module_request;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.controlers.UserSongControler;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
import ch.epfl.sweng.project.util_rule.MockUserMainActivityRule;
import ch.epfl.sweng.project.view.activity.MainActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class TestMatch {
    private ModelApplication modelApplication = ModelApplication.getModelApplication();

    @Rule
    public MockUserMainActivityRule mActivityRule = new MockUserMainActivityRule(MainActivity.class);


    @Test
    public void gotMatch() throws InterruptedException {
        Thread.sleep(GlobalTestSettings.SHORT_REQUEST_DELAY);

        assertEquals("The user's music should have good id", GlobalTestSettings.ID_MUSIC, modelApplication.getUser()
                .getCurrentMusicId());
        assertEquals("other User should have the same music id", GlobalTestSettings.ID_MUSIC, modelApplication
                .getOtherUsers()[0].getCurrentMusicId());
        Music otherMusic = UserSongControler.getUserSongControler().getSongMap().get(ModelApplication
                .getModelApplication().getOtherUsers()[0].getIdApiConnection());
        assertNotNull("The other user music should not be null", otherMusic);
        assertNotNull("The current music should not be null", modelApplication.getMusic());
        assertEquals("Should have the same music name", modelApplication.getMusic().getName(), otherMusic.getName());
        assertEquals("Should have the same aritst", modelApplication.getMusic().getArtist(), otherMusic.getArtist());

        //waiting the handler to launch the match search
        Thread.sleep(GlobalTestSettings.LONG_REQUEST_DELAY);

        assertTrue("We should find a match", mActivityRule.getActivity().gotMatch());
        assertEquals("Last music matched should be the current one", "" + GlobalTestSettings.ID_MUSIC,
               modelApplication.getLastMatchedMusic().getId() + "");
    }


}
