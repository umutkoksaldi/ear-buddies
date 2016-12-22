package ch.epfl.sweng.project.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.controlers.ConnectionControler;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.view.fragment.PresentationAppFragment;

public class LoginActivity extends AppCompatActivity {

    public static int NUMBER_FRAGMENT_PRESENTATION = 3;

    @SuppressWarnings("WeakerAccess")
    private static CallbackManager callbackManager;
    final WeakReference<Activity> currentActivity = new WeakReference(this);
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    private final ConnectionControler controlerConnection = ConnectionControler.getConnectionControler();
    private LoginButton loginButton;
    private List<PresentationAppFragment> presentationApp;
    private int indiceFragment;
    private FragmentManager fragmentManager;


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
        defineActionOnClick();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @SuppressWarnings("unused")
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult result) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                controlerConnection.sendPost(currentActivity.get(), AccessToken
                                        .getCurrentAccessToken()
                                        .getToken(), profile2
                                        .getId(), GlobalSetting.USER_API, false);
                                Toast.makeText(getApplicationContext(), getString(R.string
                                        .connexion_facebook_pending), Toast.LENGTH_SHORT).show();

                            }
                        }
                    };
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.i("facebook - profile 2eme", profile.getFirstName());
                    controlerConnection.sendPost(currentActivity.get(), AccessToken.getCurrentAccessToken().getToken(),
                            profile.getId(),
                            GlobalSetting.USER_API, false);
                    Toast.makeText(getApplicationContext(), getString(R.string.connexion_facebook_pending), Toast
                            .LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Logging in canceled.", Toast.LENGTH_SHORT).show();
                Log.i("onCancel()", "Facebook login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error occurred while logging in. Please try again.", Toast
                        .LENGTH_SHORT).show();
                Log.i("onError()", "Facebook login error.");
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * iniate the value in the fragment.
     */
    /**
     * iniate the value in the fragment.
     */
    private void initiateFragment() {

        // create fragment
        PresentationAppFragment presentationfragmentUser = new PresentationAppFragment();
        PresentationAppFragment presentationfragmentPeople = new PresentationAppFragment();
        PresentationAppFragment presentationfragmentInteraction = new PresentationAppFragment();

        // set parameters
        presentationfragmentUser.setDrawableId(R.mipmap.music_image);
        presentationfragmentUser.setPresentation(R.string.presentation_music);
        presentationfragmentUser.setStateLocation(1);

        presentationfragmentPeople.setDrawableId(R.mipmap.people_image);
        presentationfragmentPeople.setPresentation(R.string.presentation_people);
        presentationfragmentPeople.setStateLocation(2);

        presentationfragmentInteraction.setDrawableId(R.mipmap.interaction_image);
        presentationfragmentInteraction.setPresentation(R.string.presentation_interaction);
        presentationfragmentInteraction.setStateLocation(3);

        // add to list
        presentationApp = new ArrayList<>();

        presentationApp.add(presentationfragmentUser);
        presentationApp.add(presentationfragmentPeople);
        presentationApp.add(presentationfragmentInteraction);

        indiceFragment = 0;
        fragmentManager = getFragmentManager();
    }

    /**
     * Increment the value of the indice.
     */
    private void incrementIndiceFragment() {
        indiceFragment++;
        indiceFragment %= NUMBER_FRAGMENT_PRESENTATION;
    }

    /**
     * Define the action on click on frame Layout
     * The method should change the fragment inside to continue the presentation.
     */
    private void defineActionOnClick() {

        initiateFragment();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment currentFragment = presentationApp.get(indiceFragment);
        ft.replace(R.id.containFragmentPresentation, currentFragment, "" + indiceFragment);
        ft.commit();
        incrementIndiceFragment();

        FrameLayout fragmentPresentation = (FrameLayout) findViewById(R.id.containFragmentPresentation);
        fragmentPresentation.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Fragment currentFragment = presentationApp.get(indiceFragment);
                ft.replace(R.id.containFragmentPresentation, currentFragment, "" + indiceFragment);
                ft.commit();
                incrementIndiceFragment();
            }
        }));
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

    @Override
    public void onBackPressed() {
        // Leave the app properly without going back to the welcome activity
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
