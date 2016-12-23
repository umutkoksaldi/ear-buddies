package ch.epfl.sweng.project.util_rule;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.project.models.Location;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
import ch.epfl.sweng.project.view.activity.MainActivity;


/**
 * Created by Antoine Merino on 10/12/2016.
 */

public class MockUserMainActivityRule extends ActivityTestRule<MainActivity> {

    public static List<String> NAME_USER = Arrays.asList("Name Test 1", "Name Test 2", "Name Test 3",
            "Name Test 4", "Name Test 5");
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


        // prepare the mock users for testing the list.
        GlobalTestSettings.createMockUsers();

        // Testing allow to refuse the update of users.
        modelApplication.setTestingApp(true);

    }

    @Override
    protected Intent getActivityIntent() {
        Log.d("MockUserMainActivity...", "getActivityIntent()");
        // add some custom extras and stuff
        return new Intent();
    }

    @Override
    protected void afterActivityLaunched() {
        Log.d("MockUserMainActivity...", "afterActivityLaunched()");
        super.afterActivityLaunched();
        // maybe you want to do something here
        FacebookSdk.sdkInitialize(getActivity());
        GlobalTestSettings.mockMusicPlayed();
    }

    @Override
    protected void afterActivityFinished() {
        Log.d("MockUserMainActivity...", "afterActivityFinished()");
        super.afterActivityFinished();
        // Clean up mocks
    }

}