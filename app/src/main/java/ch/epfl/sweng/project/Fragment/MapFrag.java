package ch.epfl.sweng.project.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

public class MapFrag extends Fragment {
    private User mUser;
    private User[] mOtherUser;
    private final String LOCATION = "location";
    private final String ID = "idApiConnection";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUser = ModelApplication.getModelApplication().getUser();

        //***** To change with Etienne's code *****
        Location currentLoc = new Location();
        currentLoc.setLattitude(0);
        currentLoc.setLongitude(0);
        mUser.setLocation(currentLoc);
        //*****************************************
        sendAndGetLocations();
        return inflater.inflate(R.layout.frag_map, container, false);
    }

    public void sendAndGetLocations() {
        final Handler h = new Handler();
        final int DELAY = 10000; //millisecond
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("LocationLoop", "Sending the new location");
                ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {
                    @Override
                    public void onSucess(ResponseEntity responseServer) {

                        if (Integer.parseInt(responseServer.getStatusCode().toString()) ==
                                GlobalSetting.GOOD_AWNSER) {

                            ModelApplication.getModelApplication().setOtherUsers((User[]) (responseServer
                                    .getBody()));

                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getContext(), "Unable to get Locations", Toast.LENGTH_SHORT);
                    }
                });

                /* Need server implementation and have localisation in the user*/
                Map<String, String> params = new HashMap<>();
                params.put(ID, ""+mUser.getIdApiConnection());
                params.put(LOCATION, mUser.getLocation().toString());
                serviceHandler.doPost(params, GlobalSetting.URL + GlobalSetting.USER_API + mUser
                        .getIdApiConnection(), User[].class);
                h.postDelayed(this, DELAY);
            }
        }, DELAY);
    }
}
