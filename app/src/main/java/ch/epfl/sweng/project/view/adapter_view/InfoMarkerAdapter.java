package ch.epfl.sweng.project.view.adapter_view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ch.epfl.sweng.project.R;

public class InfoMarkerAdapter implements GoogleMap.InfoWindowAdapter {
    private View mView;

    public InfoMarkerAdapter(LayoutInflater inflater){
        mView = inflater.inflate(R.layout.info_marker, null);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView title = (TextView) mView.findViewById(R.id.title_info_marker);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) mView.findViewById(R.id.snippet_info_marker);
        snippet.setText(marker.getSnippet());
        return mView;
    }
}
