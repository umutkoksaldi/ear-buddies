package ch.epfl.sweng.project.ModuleRequest;

import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.facebook.FacebookSdk;

import ch.epfl.sweng.project.GlobalTestSettings;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

/**
 * Created by Antoine Merino on 10/12/2016.
 */

public class MockUserRule<A extends Activity> extends ActivityTestRule<A> {

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    public MockUserRule(Class<A> activityClass) {
        super(activityClass);
    }


    @Override
    protected void beforeActivityLaunched() {
        Log.d("MockUserRule", "beforeActivityLaunched()");
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
        Log.d("MockUserRule", "getActivityIntent()");
        Intent customIntent = new Intent();
        // add some custom extras and stuff
        return customIntent;
    }

    @Override
    protected void afterActivityLaunched() {
        Log.d("MockUserRule", "afterActivityLaunched()");
        super.afterActivityLaunched();
        // maybe you want to do something here
        FacebookSdk.sdkInitialize(getActivity());
    }

    @Override
    protected void afterActivityFinished() {
        Log.d("MockUserRule", "afterActivityFinished()");
        super.afterActivityFinished();
        // Clean up mocks
    }
}
