package ch.epfl.sweng.project.ModuleRequest;

import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.utils.GlobalTestSettings;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Antoine Merino on 10/12/2016.
 */

public class MockUserRule<A extends Activity> extends ActivityTestRule<A> {

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    public static List<String> NAME_USER = Arrays.asList("Name Test 1","Name Test 2","Name Test 3",
            "Name Test 4","Name Test 5");

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
        customIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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