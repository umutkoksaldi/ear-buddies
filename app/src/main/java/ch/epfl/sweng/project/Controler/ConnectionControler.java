package ch.epfl.sweng.project.Controler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by adupeyrat on 05/11/2016.
 */

public final class ConnectionControler {

    private static ConnectionControler connectionControler = null;
    private final ModelApplication modelApplication = ModelApplication.getModelApplication();

    private ConnectionControler() {
    }

    public static ConnectionControler getConnectionControler() {
        if (connectionControler == null) {
            connectionControler = new ConnectionControler();
        }
        return connectionControler;
    }


    /**
     * Allow to change activity.
     *
     * @param transitionClass destination Activity. should not be null
     * @param intentExtra     shared data. should not be null
     */
    public void changeActivity(Activity currentActivity, Class transitionClass, Map<String, String> intentExtra) {
        if (transitionClass == null && intentExtra == null) {
            Log.w("openDetailsFragment()", "null value parameters");
            return;
        }

        Intent intent = new Intent(currentActivity, transitionClass);
        for (Map.Entry<String, String> map : intentExtra.entrySet()) {
            intent.putExtra(map.getKey(), map.getValue());
        }
        currentActivity.startActivity(intent);
    }

    /**
     * Send post to the server in order to get the profile
     *
     * @param AccesToken given by facebook
     * @param idFacebook given by facebook
     * @param requestApi url to get information
     */
    public void sendPost(final Activity currentActivity, String AccesToken, String idFacebook, @SuppressWarnings
            ("SameParameterValue") String requestApi, final boolean isTest) {
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    modelApplication.setUser((User) response.getBody());
                    if (!isTest) {
                        //Intent intent = new Intent(currentActivity, MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //getApplicationContext().startActivity(intent);
                        changeActivity(currentActivity, MainActivity.class, new HashMap<String, String>());
                    }

                } else {
                    logOutFace();
                    Toast.makeText(currentActivity.getApplicationContext(), currentActivity.getString(R.string
                            .error_connexion_facebook), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed() {
                logOutFace();
                Toast.makeText(getApplicationContext(), currentActivity.getString(R.string.error_connexion_facebook),
                        Toast.LENGTH_SHORT).show();
            }
        });


        // Building the parameters for the
        Map<String, String> params = new HashMap<>();
        params.put(GlobalSetting.ID, idFacebook);
        params.put(GlobalSetting.ACCESS_TOKEN, AccesToken);

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
