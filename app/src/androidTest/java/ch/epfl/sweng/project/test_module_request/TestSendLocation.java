package ch.epfl.sweng.project.test_module_request;


import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.util_rule.MockUserMainActivityRule;
import ch.epfl.sweng.project.view.activity.MainActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class TestSendLocation {

    @Rule
    public MockUserMainActivityRule mActivityRule = new MockUserMainActivityRule(MainActivity.class);


    @Test
    public void testMarker() throws InterruptedException {
        Thread.sleep(4000);
        User[] others = ModelApplication.getModelApplication().getOtherUsers();
        List<MarkerOptions> markerOpt = ModelApplication.getModelApplication().getMarkerOpt();
        assertEquals("Shoud have the same number of marker as user", others.length, markerOpt.size());
        MarkerOptions m = markerOpt.get(0);
       assertNotNull("Marker should not be null", m);
   }
}
