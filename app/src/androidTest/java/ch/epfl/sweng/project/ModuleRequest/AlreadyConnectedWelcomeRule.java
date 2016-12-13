package ch.epfl.sweng.project.ModuleRequest;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import Util.GlobalSetting;
import ch.epfl.sweng.project.utils.GlobalTestSettings;
import ch.epfl.sweng.project.WelcomeActivity;

/**
 * Created by Antoine Merino on 10/12/2016.
 */

public class AlreadyConnectedWelcomeRule<A extends WelcomeActivity> extends ActivityTestRule<A> {


    private static final String TESTING_ALREADY_CONNECTED = "Testing_already_conected";


    public AlreadyConnectedWelcomeRule(Class<A> activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched() {
        Log.d("AlreadyConnectedWelc...", "beforeActivityLaunched()");
        super.beforeActivityLaunched();
        // Maybe prepare some mock service calls
        // Maybe override some depency injection modules with mocks
    }

    @Override
    protected Intent getActivityIntent() {
        Log.d("AlreadyConnectedWelc...", "getActivityIntent()");
        Intent customIntent = new Intent();
        // add some custom extras and stuff
        Bundle bundle = new Bundle();
        bundle.putString(TESTING_ALREADY_CONNECTED, "true");
        bundle.putString(GlobalSetting.ID, GlobalTestSettings.MOCK_ID_FACEBOOK);
        bundle.putString(GlobalSetting.ACCESS_TOKEN, GlobalTestSettings.MOCK_ACCESS_TOKEN_FACEBOOK);

        customIntent.putExtras(bundle);
        return customIntent;
    }

    @Override
    protected void afterActivityLaunched() {
        Log.d("AlreadyConnectedWelc...", "afterActivityLaunched()");
        super.afterActivityLaunched();
        // maybe you want to do something here
    }

    @Override
    protected void afterActivityFinished() {
        Log.d("AlreadyConnectedWelc...", "afterActivityFinished()");
        super.afterActivityFinished();
        // Clean up mocks
    }
}
