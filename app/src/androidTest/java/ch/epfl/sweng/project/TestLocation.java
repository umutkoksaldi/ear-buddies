package ch.epfl.sweng.project;


import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.InstrumentationRegistry;
import android.support.v4.app.ActivityCompat;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

public class TestLocation extends ActivityInstrumentationTestCase2<MainActivity> {
    public TestLocation() {
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
        mUser.setSeeBirth(true);
        ModelApplication.getModelApplication().setUser(mUser);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Test
    public void testGPS() throws InterruptedException {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService
                (Context.LOCATION_SERVICE);

        locationManager.addTestProvider("Test", false, false, false, false, false, false, false, Criteria.POWER_LOW,
                Criteria.ACCURACY_FINE);

        //set up test
        Location location = new Location("");
        location.setLatitude(48);
        location.setLongitude(7);
        location.setAccuracy(3333);
        location.setTime(333);
        location.setElapsedRealtimeNanos(333);
        locationManager.setTestProviderLocation("Test", location);
        locationManager.setTestProviderEnabled("Test", true);

        //Wait for location update
        Thread.sleep(15000);
        //Check
        double mLatitude = ModelApplication.getModelApplication().getUser().getLocation().getLattitude();
        double mLongitude = ModelApplication.getModelApplication().getUser().getLocation().getLongitude();
        locationManager.removeTestProvider("Test");

        assertEquals(location.getLatitude(), mLatitude);
    }


}
