package ch.epfl.sweng.project;


import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

public class TestSendLocation extends ActivityInstrumentationTestCase2<MainActivity> {
    public TestSendLocation() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInsrumentation(InstrumentationRegistry.getInstrumentation());
        User mUser = new User();
        mUser.setLocation(new ch.epfl.sweng.project.Model.Location(0, 0));
        mUser.setAge(21);
        mUser.setBackgroundPicture("https://scontent-amt2-1.xx.fbcdn.net/v/t1.0-9/419921_334072353301892_257076548_n" +
                ".jpg?oh=1da08f8d32b10b20958a3df2f18096fa&oe=5886B16B");
        mUser.setProfilePicture("https://scontent-amt2-1.xx.fbcdn.net/v/t1.0-9/419921_334072353301892_257076548_n" +
                ".jpg?oh=1da08f8d32b10b20958a3df2f18096fa&oe=5886B16B");
        mUser.setEmail("arnill-electro@hotmail.com");
        mUser.setFirstname("Arnaud");
        mUser.setLastname("Hennig");
        mUser.setIdApiConnection(1331778390197945L);
        mUser.setSeeBirth(true);
        ModelApplication.getModelApplication().setUser(mUser);
    }


    //This is a base test that we need to modify when we get the other users correctly
    @Test
    public void testGPS() throws InterruptedException {
        getActivity();
        Thread.sleep(15000);
        User[] others = ModelApplication.getModelApplication().getOtherUsers();
        assertTrue("No other people found",others != null);
    }


}