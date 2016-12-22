package ch.epfl.sweng.project.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.controlers.UserDetailsControler;
import ch.epfl.sweng.project.models.Location;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.server_request.OnServerRequestComplete;
import ch.epfl.sweng.project.server_request.ServiceHandler;
import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.view.adapter_view.InfoMarkerAdapter;
import ch.epfl.sweng.project.view.util_view.DownloadImageMarker;

import static ch.epfl.sweng.project.util_constant.GlobalSetting.MAP_LOCATION_FASTEST_INTERVAL;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.MAP_LOCATION_INTERVAL;
import static ch.epfl.sweng.project.util_constant.GlobalSetting.MARKER_SIZE;

public class MapFragment extends Fragment implements OnMapReadyCallback, ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener {


    private final String LATTITUDE = "lattitude";
    private final String LONGITUDE = "longitude";
    private final String USER_AROUND = "getUsersAround";
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private final String ID = "idApiConnection";
    public View view;
    private String mTest = "/";
    private User mUser;
    // Location
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;
    private Handler mHandler = new Handler();
    private Activity mActivity;
    private boolean neverLocated = true;
    // Map
    private GoogleMap mMap;
    private SupportMapFragment sMapFragment;
    private int ZOOM = 16;
    //Detail fragment
    private UserDetailsControler userDetailsControler;

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
        userDetailsControler = UserDetailsControler.getConnectionControler();
        // ModelApplication.getModelApplication().setTest();
        mInflater = inflater;
        mImages = new HashMap<>();
        mActivity = getActivity();
        mUser = ModelApplication.getModelApplication().getUser();
        mTest = ModelApplication.getModelApplication().getTestState();
        // Map from Google
        sMapFragment = SupportMapFragment.newInstance();
        sMapFragment.getMapAsync(this);
        android.support.v4.app.FragmentManager sFm = getFragmentManager();
        if (!sMapFragment.isAdded())
            sFm.beginTransaction().add(R.id.framelayout_map, sMapFragment).commit();
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
        User selectedUser = allMarkersMap.get(marker);
        userDetailsControler.openDetailsFragment(this, selectedUser);
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
        mLocationRequest.setInterval(MAP_LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(MAP_LOCATION_FASTEST_INTERVAL);

        //Check user parameters for location
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
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
                    // Fill the users fragment if it's the first time we get location of users
                    if (neverLocated && getContext() != null) {
                        Intent intent = new Intent(GlobalSetting.MAP_REFRESHED);
                        getContext().sendBroadcast(intent);
                        neverLocated = false;
                    }
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
                    .snippet(aUser.getSnippetDescription())
            );
            // We already have the image => do not need to download
            if (mImages.containsKey(otherUsers[i].getIdApiConnection())) {
                Bitmap bitmap = mImages.get(aUser.getIdApiConnection());
                marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            } else {
                String url = aUser.getProfilePicture();
                new DownloadImageMarker(marker, mImages, aUser.getIdApiConnection()).execute(url);
                Activity activity = getActivity();
                if (activity != null) {

                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_image);

                    BitmapDescriptor defProfile = BitmapDescriptorFactory.fromBitmap(getCircleBitmap(bm));
                    marker.icon(defProfile);
                }


            }
            markersOption.add(marker);
        }

        User match = ModelApplication.getModelApplication().getMatchedUser();
        if (match != null) {
            if (!ModelApplication.getModelApplication().isZoomedOnMatch()) {
                ModelApplication.getModelApplication().setZoomedOnMatch(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(match.getLocation().getLattitude(),
                        match.getLocation().getLongitude()), ZOOM));
            }
        }

        mMap.clear();
        allMarkersMap = new HashMap<>();
        for (int i = 0; i < markersOption.size(); ++i) {
            allMarkersMap.put(mMap.addMarker(markersOption.get(i)), otherUsers[i]);
        }
        ModelApplication.getModelApplication().setMarkerOpt(markersOption);
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(output, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(output);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        return output;
    }


}
