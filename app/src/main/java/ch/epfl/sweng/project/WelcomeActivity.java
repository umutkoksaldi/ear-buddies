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
import ch.epfl.sweng.project.Controler.ConnectionControler;
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
    private final ConnectionControler controlerApplication = ConnectionControler.getConnectionControler();

    private static final String ID = "id";
    private static final String ACESS_TOKEN = "accesToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "the user connect to the application");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.welcome_activity);

        // We get the user information back.
        if (Profile.getCurrentProfile() != null && AccessToken.getCurrentAccessToken() != null) {
            controlerApplication.sendPost(this,AccessToken.getCurrentAccessToken().getToken(),Profile
                    .getCurrentProfile().getId(),GlobalSetting.USER_API, false);
        }
        // We don't have any informations about the user in database.
        else{
            controlerApplication.changeActivity(this, Login.class, new HashMap<String, String>());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // We get the user information back.
        if (Profile.getCurrentProfile() != null && AccessToken.getCurrentAccessToken() != null) {
            controlerApplication.sendPost(this,AccessToken.getCurrentAccessToken().getToken(),Profile
                    .getCurrentProfile().getId(),GlobalSetting.USER_API, false);
        }
        // We don't have any informations about the user in database.
        else{
            controlerApplication.changeActivity(this, Login.class, new HashMap<String, String>());
        }
    }

}
