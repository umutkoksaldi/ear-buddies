package ch.epfl.sweng.project.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.project.R;

/**
 * Created by adupeyrat on 03/12/2016.
 */

public class PresentationAppFragment extends Fragment {

    private int presentationTextId;
    private int drawableId;
    private int stateLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // associate fragment to xml
        View view = inflater.inflate(R.layout.frag_presentation_app, container, false);

        // set components
        TextView textView = (TextView) view.findViewById(R.id.description_app_text);
        textView.setText(presentationTextId);

        ImageView imageView = (ImageView) view.findViewById(R.id.description_app_picture);
        imageView.setImageResource(drawableId);


        // get the three dot at the bottom of the fragment
        ImageView imageDotRight = (ImageView) view.findViewById(R.id.dotRight);
        ImageView imageDotCenter = (ImageView) view.findViewById(R.id.dotCenter);
        ImageView imageDotLeft = (ImageView) view.findViewById(R.id.dotLeft);

        int dotSelected = R.drawable.dot_selected;
        int dot = R.drawable.dot;

        // render image depending of the position
        switch (stateLocation) {
            case 1:
                imageDotRight.setImageResource(dotSelected);
                imageDotCenter.setImageResource(dot);
                imageDotLeft.setImageResource(dot);
                break;
            case 2:
                imageDotRight.setImageResource(dot);
                imageDotCenter.setImageResource(dotSelected);
                imageDotLeft.setImageResource(dot);
                break;
            case 3:
                imageDotRight.setImageResource(dot);
                imageDotCenter.setImageResource(dot);
                imageDotLeft.setImageResource(dotSelected);
                break;
            default:
                imageDotRight.setImageResource(dot);
                imageDotCenter.setImageResource(dot);
                imageDotLeft.setImageResource(dot);
                break;
        }
        return view;
    }

    public void setPresentation(int presentationTextId) {
        this.presentationTextId = presentationTextId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public int getStateLocation() {
        return stateLocation;
    }

    public void setStateLocation(int stateLocation) {
        this.stateLocation = stateLocation;
    }

    public int getPresentationTextId() {
        return presentationTextId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}