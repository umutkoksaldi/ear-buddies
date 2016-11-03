package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by adupeyrat on 03/11/2016.
 */

public class WelcomeActivity extends AppCompatActivity{

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    private static final String ID = "id";
    private static final String ACESS_TOKEN = "accesToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "the user connect to the application");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.welcome_activity);

        // We get the user information back.
        if (isAlreadyConnected(GlobalSetting.USER_API)) {
            Toast.makeText(getApplicationContext(), getString(R.string.connexion_facebook_pending), Toast.LENGTH_SHORT).show();

        }
        // We don't have any informations about the user in database.
        else{
            changeActivity(Login.class, new HashMap<String, String>());
        }
    }

    /**
     * Allow to change activity.
     *
     * @param transitionClass destination Activity. should not be null
     * @param intentExtra     shared data. should not be null
     */
    private void changeActivity(Class transitionClass, Map<String, String> intentExtra) {
        if (transitionClass == null && intentExtra == null) {
            Log.w("changeActivity()", "null value parameters");
            return;
        }

        Intent intent = new Intent(WelcomeActivity.this, transitionClass);
        for (Map.Entry<String, String> map : intentExtra.entrySet()) {
            intent.putExtra(map.getKey(), map.getValue());
        }
        startActivity(intent);
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

    /**
     * Send post to the server in order to get the profile
     * @param AccesToken given by facebook
     * @param idFacebook given by facebook
     * @param requestApi url to get information
     */
    private void sendPost(String AccesToken, String idFacebook, @SuppressWarnings("SameParameterValue") String requestApi) {
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    modelApplication.setUser((User) response.getBody());
                    changeActivity(MainActivity.class, new HashMap<String, String>());

                } else {
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
        params.put(ID, idFacebook);
        params.put(ACESS_TOKEN, AccesToken);

        // the interface is already initiate above
        serviceHandler.doPost(params, GlobalSetting.URL + requestApi, User.class);
    }

    /**
     * Disconnection to facebook
     */
    private void logOutFace() {
        Log.i("logOutFace()", "Disconnection facebook");
        LoginManager.getInstance().logOut();
    }
}
