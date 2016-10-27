package ch.epfl.sweng.project.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

public class MapFrag extends Fragment implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMyLocationButtonClickListener {
    private User mUser;
    private final String LATTITUDE = "lattitude";
    private final String LONGITUDE = "longitude";
    private final String USER_AROUND = "getUsersAround/";
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    private final String ID = "idApiConnection";

    //Location
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;

    private Activity mActivity;

    //Map
    private GoogleMap mMap;
    private SupportMapFragment sMapFragment;
    private int ZOOM = 16;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = this.getActivity();
        mUser = ModelApplication.getModelApplication().getUser();

        // Map from Google
        sMapFragment = SupportMapFragment.newInstance();
        FragmentManager fm = getFragmentManager();
        sMapFragment.getMapAsync(this);
        android.support.v4.app.FragmentManager sFm = getFragmentManager();
        if (!sMapFragment.isAdded())
            sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
        else
            sFm.beginTransaction().show(sMapFragment).commit();

        //Google API
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
        createLocationRequest();


        sendAndGetLocations();

        return inflater.inflate(R.layout.frag_map, container, false);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            Log.e("Location", "Don't have permission -> request");
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        double latitude = mUser.getLocation().getLattitude();
        double longitude = mUser.getLocation().getLongitude();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
        //updateLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.i("Hello", "Hello");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        mUser.setLocation(new Location(latitude, longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    @Override
    public boolean onMyLocationButtonClick() {
        double latitude = mUser.getLocation().getLattitude();
        double longitude = mUser.getLocation().getLongitude();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM));
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            updateLocation();
        }
    }

    //TODO Onresume Onresume

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }


    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        //TODO DELAY
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Check user parameters for location
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates lss = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        updateLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    mActivity,
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        Log.e("LocationRequest", "Cannot get Location");
                        break;
                }
            }
        });
    }

    private void sendAndGetLocations() {
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
                                GlobalSetting.GOOD_ANSWER) {

                            ModelApplication.getModelApplication().setOtherUsers((User[]) (responseServer
                                    .getBody()));

                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getContext(), "Unable to get Locations", Toast.LENGTH_SHORT).show();
                    }
                });

                Map<String, String> params = new HashMap<>();
                params.put(ID, "" + mUser.getIdApiConnection());
                params.put(LATTITUDE, "" + mUser.getLocation().getLattitude());
                params.put(LONGITUDE, "" + mUser.getLocation().getLongitude());
                Log.i("Send item", params.toString());
                serviceHandler.doPost(params, GlobalSetting.URL + GlobalSetting.USER_API + USER_AROUND, User[].class);
                h.postDelayed(this, DELAY);
            }
        }, DELAY);
    }


}
