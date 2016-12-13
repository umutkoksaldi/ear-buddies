package ch.epfl.sweng.project.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.Location;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

public class MapFrag extends Fragment implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener, View
                .OnClickListener {


    private final String LATTITUDE = "lattitude";
    private final String LONGITUDE = "longitude";
    private final String USER_AROUND = "getUsersAround";
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private final String ID = "idApiConnection";
    public View view;
    private String mTest = "/";
    private User mUser;
    //Location
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;
    private Handler mHandler = new Handler();
    private Activity mActivity;
    //Map
    private GoogleMap mMap;
    private SupportMapFragment sMapFragment;
    private int ZOOM = 16;

    private Map<Long, Bitmap> mImages;

    private LayoutInflater mInflater;

    private Map<Marker, User> allMarkersMap = new HashMap<Marker, User>();
    private final Runnable runnable = new Runnable() {
        final int DELAY = 10000;

        @Override
        public void run() {
            sendAndGetLocations();
            mHandler.postDelayed(this, DELAY);
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
       // ModelApplication.getModelApplication().setTest();
        mInflater = inflater;
        mImages = new HashMap<>();
        mActivity = getActivity();
        mUser = ModelApplication.getModelApplication().getUser();
        mTest = ModelApplication.getModelApplication().getTestState();
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
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
        createLocationRequest();

        mHandler.post(runnable);
        view = inflater.inflate(R.layout.frag_map, container, false);
        ImageButton im = (ImageButton) view.findViewById(R.id.updatdeOtherUsers);
        im.setOnClickListener(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            Log.e("Location", "Don't have permission -> request");
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setInfoWindowAdapter(new InfoMarkerAdapter(mInflater));
        mMap.setOnInfoWindowClickListener(this);
        //set the camera to the user
        onMyLocationButtonClick();
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
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        mUser.setLocation(new Location(latitude, longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (mUser.getLocation() != null) {
            double latitude = mUser.getLocation().getLattitude();
            double longitude = mUser.getLocation().getLongitude();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM));
            return true;
        } else {
            Log.e("Null", "Location is null");
            return false;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        User showUser = allMarkersMap.get(marker);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setUser(showUser);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.map, detailsFragment)
                .addToBackStack("mapFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updatdeOtherUsers:
                sendAndGetLocations();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        mHandler.removeCallbacks(runnable);
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        //TODO DELAY
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);

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
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {
            @Override
            public void onSucess(ResponseEntity responseServer) {

                if (Integer.parseInt(responseServer.getStatusCode().toString()) ==
                        GlobalSetting.GOOD_ANSWER) {

                    ModelApplication.getModelApplication().setOtherUsers((User[]) (responseServer
                            .getBody()));
                    mMap.clear();
                    showOtherUsers();
                } else {
                    onFailed();
                }
            }

            @Override
            public void onFailed() {
                //Toast.makeText(getContext(), "Unable to get Locations", Toast.LENGTH_SHORT).show();
            }
        });

        if (mUser.getLocation() != null) {
            Map<String, String> params = new HashMap<>();
            params.put(ID, "" + mUser.getIdApiConnection());

            params.put(LATTITUDE, "" + mUser.getLocation().getLattitude());
            params.put(LONGITUDE, "" + mUser.getLocation().getLongitude());
            Log.i("Send item", params.toString());
            serviceHandler.doPost(params, GlobalSetting.URL + GlobalSetting.USER_API + USER_AROUND + mTest, User[]
                    .class);
        } else {
            Log.e("Null", "Location is null");
            updateLocation();
        }
    }

    private void showOtherUsers() {
        User[] otherUsers = ModelApplication.getModelApplication().getOtherUsers();
        List<MarkerOptions> markersOption = new ArrayList<>();
        for (int i = 0; i < otherUsers.length; ++i) {
            User aUser = otherUsers[i];
            double latitude = aUser.getLocation().getLattitude();
            double longitude = aUser.getLocation().getLongitude();
            MarkerOptions marker = (new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(aUser.getFirstname())
                    .snippet(aUser.getLastname()));
            // We already have the image => do not need to download
            if (mImages.containsKey(otherUsers[i].getIdApiConnection())) {
                marker.icon(BitmapDescriptorFactory.fromBitmap(mImages.get(aUser.getIdApiConnection())));
            } else {
                String url = aUser.getProfilePicture();
                new DownloadImageMarker(marker, mImages, aUser.getIdApiConnection()).execute(url);
                Activity activity = getActivity();
                if (activity != null) {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_image);
                    bm = DownloadImageMarker.scaleDown(bm, 100, true);
                    BitmapDescriptor defProfile = BitmapDescriptorFactory.fromBitmap(bm);
                    marker.icon(defProfile);
                }


            }
            markersOption.add(marker);
        }

        mMap.clear();
        allMarkersMap = new HashMap<>();
        for (int i = 0; i< markersOption.size(); ++i) {
            allMarkersMap.put(mMap.addMarker(markersOption.get(i)), otherUsers[i]);
        }
        ModelApplication.getModelApplication().setMarkerOpt(markersOption);
    }



}
