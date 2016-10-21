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

import java.util.ArrayList;
import java.util.List;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

public class MapFrag extends Fragment{
    User mUser;
    List<Location> mOthersLocation;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mUser = ModelApplication.getModelApplication().getUser();
        mOthersLocation = new ArrayList<>();
        sendLocation();
        return inflater.inflate(R.layout.frag_map, container, false);
    }

    public void sendLocation() {
        final Handler h = new Handler();
        // To change 1 sec only for trying
        final int DELAY = 1000; //millisecond
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("LocationLoop", "Sending the new location");
                ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {
                    @Override
                    public void onSucess(ResponseEntity responseServer) {

                        if(Integer.parseInt(responseServer.getStatusCode().toString()) ==
                                GlobalSetting.GOOD_AWNSER){
                            /* Not Implemented yet on the server
                                mOthersLocation = ((ArrayList<Location>)(responseServer.getBody()));
                             */
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
                serviceHandler.doPost(mUser.getLocation(), GlobalSetting.URL + GlobalSetting.USER_API ,
                        Location.class);
                h.postDelayed(this, DELAY);
            }
        }, DELAY);
    }
}
