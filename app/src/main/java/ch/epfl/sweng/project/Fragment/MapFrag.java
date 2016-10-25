package ch.epfl.sweng.project.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sweng.project.R;

public class MapFrag extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment sMapFragment;
    private double latitude = 46.5186995;
    private double longitude = 6.5619528;
    private int zoom = 18;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sMapFragment = SupportMapFragment.newInstance();

        sMapFragment.getMapAsync(this);
        android.support.v4.app.FragmentManager sFm = getFragmentManager();

        if (!sMapFragment.isAdded())
            sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
        else
            sFm.beginTransaction().show(sMapFragment).commit();

        return inflater.inflate(R.layout.frag_map, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));

    }
}
