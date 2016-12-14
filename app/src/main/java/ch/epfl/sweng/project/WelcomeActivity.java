package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.util.HashMap;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Controler.ConnectionControler;
import ch.epfl.sweng.project.Model.ModelApplication;


/**
 * Created by adupeyrat on 03/11/2016.
 */

public class WelcomeActivity extends AppCompatActivity {

    private static final String TESTING_ALREADY_CONNECTED = "Testing_already_conected";
    private static final String TESTING_FIRST_CONNECTION = "Testing_frist_connection";
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerApplication = ConnectionControler.getConnectionControler();
    private boolean mAlreadyConnected = false;
    private String mId;
    private String mAccessToken;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "the user connect to the application");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.welcome_activity);

        // test if the user is connected or not
        // Moreover test if it is for a test or not.
        mIntent = getIntent();
        setAlreadyConnected(mIntent);


        // We get the user information back.
        if (mAlreadyConnected) {
            controlerApplication.sendPost(this, mAccessToken, mId, GlobalSetting.USER_API, false);
        }
        // We don't have any informations about the user in database.
        else {
            controlerApplication.changeActivity(this, Login.class, new HashMap<String, String>());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // We get the user information back.
        if (mAlreadyConnected) {
            controlerApplication.sendPost(this, mAccessToken, mId, GlobalSetting.USER_API, false);
        }
        // We don't have any informations about the user in database.
        else {
            controlerApplication.changeActivity(this, Login.class, new HashMap<String, String>());
        }
    }


    private void setAlreadyConnected(Intent intent) {

        // Facebook maintain the Profile active from the moment the user connect in the app.
        if (Profile.getCurrentProfile() != null && AccessToken.getCurrentAccessToken() != null) {
            mAlreadyConnected = true;
            mId = Profile.getCurrentProfile().getId();
            mAccessToken = AccessToken.getCurrentAccessToken().getToken();
        }

        // -----------------------------------------------------------------
        // Set up for testing
        if (intent.getExtras() != null) {

            if (intent.getExtras().getString(TESTING_ALREADY_CONNECTED) != null) {
                mAlreadyConnected = true;
                mId = intent.getExtras().getString(GlobalSetting.ID);
                mAccessToken = intent.getExtras().getString(GlobalSetting.ACCESS_TOKEN);
            } else if (intent.getExtras().getString(TESTING_FIRST_CONNECTION) != null) {
                mAlreadyConnected = false;
            }
        }

    }

}
