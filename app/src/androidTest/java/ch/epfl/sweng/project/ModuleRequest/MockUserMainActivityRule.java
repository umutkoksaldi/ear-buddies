package ch.epfl.sweng.project.ModuleRequest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.facebook.FacebookSdk;

import ch.epfl.sweng.project.utils.GlobalTestSettings;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

/**
 * Created by Antoine Merino on 10/12/2016.
 */

public class MockUserMainActivityRule extends ActivityTestRule<MainActivity> {

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    public MockUserMainActivityRule(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    public MockUserMainActivityRule(Class<MainActivity> activityClass, boolean initialTouchMode, boolean
            launchActivity) {
        super(activityClass, initialTouchMode, launchActivity);
    }


    @Override
    protected void beforeActivityLaunched() {
        Log.d("MockUserMainActivity...", "beforeActivityLaunched()");
        super.beforeActivityLaunched();
        // Maybe prepare some mock service calls
        // Maybe override some depency injection modules with mocks
        GlobalTestSettings.createMockUser();
        User userTest = modelApplication.getUser();
        userTest.setLocation(new Location());
        modelApplication.setUser(userTest);
    }

    @Override
    protected Intent getActivityIntent() {
        Log.d("MockUserMainActivity...", "getActivityIntent()");
        Intent customIntent = new Intent();
        // add some custom extras and stuff
        return customIntent;
    }

    @Override
    protected void afterActivityLaunched() {
        Log.d("MockUserMainActivity...", "afterActivityLaunched()");
        super.afterActivityLaunched();
        // maybe you want to do something here
        FacebookSdk.sdkInitialize(getActivity());
    }

    @Override
    protected void afterActivityFinished() {
        Log.d("MockUserMainActivity...", "afterActivityFinished()");
        super.afterActivityFinished();
        // Clean up mocks
    }
}
