package ch.epfl.sweng.project.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

public class MapFrag extends Fragment implements OnMapReadyCallback{
    private User mUser;
    private final String LOCATION = "location";
    private final String ID = "idApiConnection";

    private SupportMapFragment sMapFragment;
    private double latitude = 46.5186995;
    private double longitude = 6.5619528;
    private int zoom = 18;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUser = ModelApplication.getModelApplication().getUser();

       sMapFragment = SupportMapFragment.newInstance();
        FragmentManager fm = getFragmentManager();

        sMapFragment.getMapAsync(this);
        android.support.v4.app.FragmentManager sFm = getFragmentManager();

        if (!sMapFragment.isAdded())
            sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
        else
            sFm.beginTransaction().show(sMapFragment).commit();


        sendAndGetLocations();
        return inflater.inflate(R.layout.frag_map, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));

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
