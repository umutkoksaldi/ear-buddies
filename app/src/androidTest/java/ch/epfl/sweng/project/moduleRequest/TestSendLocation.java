package ch.epfl.sweng.project.ModuleRequest;


import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.project.utils.GlobalTestSettings;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;

public class TestSendLocation extends ActivityInstrumentationTestCase2<MainActivity> {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    public TestSendLocation() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInsrumentation(InstrumentationRegistry.getInstrumentation());
        GlobalTestSettings.createMockUser();
        ModelApplication.getModelApplication().setTest();
    }


    //This is a base test that we need to modify when we get the other users correctly
    @Test
    public void testOtherUser() throws InterruptedException {
        /*getActivity();
        Thread.sleep(4000);
        User[] others = ModelApplication.getModelApplication().getOtherUsers();
        assertTrue("No other people found", others != null);
        //TODO Verify if people are in the radius (Actually it may be better server side)
        //TODO Check if information actually match with the server like :
        if (others.length != 0) {
            assertEquals("Bad name found", GlobalTestSettings.MOCK_USER_FIRST_NAME, others[0].getFirstname());
        }*/
    }

    @Test
    public void testMarker() throws InterruptedException {
        /*getActivity();
        Thread.sleep(5000);
        User[] others = ModelApplication.getModelApplication().getOtherUsers();
        List<MarkerOptions> markerOpt = ModelApplication.getModelApplication().getMarkerOpt();
        assertEquals("Shoud have the same number of marker as user", others.length, markerOpt.size());
        MarkerOptions m = markerOpt.get(0);

        assertNotNull("Marker should not be null", m);*/
    }
}
