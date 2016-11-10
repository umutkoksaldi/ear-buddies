package ch.epfl.sweng.project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Controler.ConnectionControler;
import ch.epfl.sweng.project.Model.ModelApplication;

public class Login extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    private static CallbackManager callbackManager;
    final WeakReference<Activity> currentActivity = new WeakReference(this);
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    private LoginButton loginButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "the user connect to the application.");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        addPermissions();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @SuppressWarnings("unused")
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult result) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            controlerConnection.sendPost(currentActivity.get(), AccessToken
                                    .getCurrentAccessToken()
                                    .getToken(), profile2
                                    .getId(), GlobalSetting.USER_API, false);
                            Toast.makeText(getApplicationContext(), getString(R.string.connexion_facebook_pending), Toast.LENGTH_SHORT).show();
                        }
                    };
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.i("facebook - profile 2eme", profile.getFirstName());
                    controlerConnection.sendPost(currentActivity.get(), AccessToken.getCurrentAccessToken().getToken(),
                            profile.getId(),
                            GlobalSetting.USER_API, false);
                    Toast.makeText(getApplicationContext(), getString(R.string.connexion_facebook_pending), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Logging in canceled.", Toast.LENGTH_SHORT).show();
                Log.i("onCancel()", "Facebook login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error occurred while logging in. Please try again.", Toast.LENGTH_SHORT).show();
                Log.i("onError()", "Facebook login error.");
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
        }
    }

    private void addPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_about_me");
        loginButton.setReadPermissions(permissions);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}