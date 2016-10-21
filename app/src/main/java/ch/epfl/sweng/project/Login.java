package ch.epfl.sweng.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

public class Login extends AppCompatActivity  {

    @SuppressWarnings("WeakerAccess")
    public static CallbackManager callbackManager;
    private LoginButton loginButton;
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "the user connect to the application.");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        // THE SERVICE IS NOT IMPLEMENTED YET ON THE SERVER SIDE
        if (isAlreadyConnected(GlobalSetting.USER_API)) {
            Toast.makeText(getApplicationContext(), getString(R.string.connexion_facebook_pending), Toast.LENGTH_SHORT).show();
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        addPermissions();

        /*******************************    Callback facebook developer ***********************/

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @SuppressWarnings("unused")
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult result) {
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            sendPost(AccessToken.getCurrentAccessToken().getToken(), profile2.getId(), GlobalSetting.USER_API);
                            Toast.makeText(getApplicationContext(), getString(R.string.connexion_facebook_pending), Toast.LENGTH_SHORT).show();
                        }
                    };
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.i("facebook - profile 2eme", profile.getFirstName());
                    sendPost(AccessToken.getCurrentAccessToken().getToken(), profile.getId(), GlobalSetting.USER_API);
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
                error.printStackTrace();
                Log.i("onError()", "Facebook login error.");
            }
        });
        /*******************************    Callback facebook developer ***********************/
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {}
    }

    private void sendPost(String AccesToken, String idFacebook, @SuppressWarnings("SameParameterValue") String requestApi) {
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if(Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_AWNSER){
                    modelApplication.setUser((User)response.getBody());
                    changeActivity(MainActivity.class,new HashMap<String, String>());
                }
                else{
                    logOutFace();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_connexion_facebook), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed() {
                logOutFace();
                Toast.makeText(getApplicationContext(), getString(R.string.error_connexion_facebook), Toast.LENGTH_SHORT).show();
            }
        });

        // Building the parameters for the
        Map<String, String> params = new HashMap<>();
        params.put("id", idFacebook);
        params.put("accesToken", AccesToken);

        // the interface is already initiate above
        serviceHandler.doPost(params, GlobalSetting.URL + requestApi ,User.class);
    }

    /**
     * Check if the user is already connected with facebook on our application.
     * @return true if
     */
    private boolean isAlreadyConnected (String requestApi) {
        boolean isConnected = false;
        if (Profile.getCurrentProfile() != null) {
            isConnected = true;
            sendPost(AccessToken.getCurrentAccessToken().getToken(), Profile.getCurrentProfile().getId(),
                    GlobalSetting.USER_API);
        }
        return isConnected;
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
     * Disconnection to facebook
     */
    private void logOutFace() {
        Log.i("logOutFace()" ,"Disconnection facebook");
        LoginManager.getInstance().logOut();
    }

    /**
     * Allow to change activity.
     * @param transitionClass destination Activity. should not be null
     * @param intentExtra  shared data. should not be null
     */
    private void changeActivity(Class transitionClass, Map<String, String> intentExtra){
        if (transitionClass == null && intentExtra == null) {
            Log.w("changeActivity()","null value parameters");
            return;
        }

        Intent intent = new Intent(Login.this, transitionClass);
        for (Map.Entry<String, String> map : intentExtra.entrySet()) {
            intent.putExtra(map.getKey(), map.getValue());
        }
        startActivity(intent);
    }
}