package ch.epfl.sweng.project.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.R;

public class ProfileFrag extends Fragment implements View.OnClickListener{

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    ImageView coverPict;
    TextView name;
    ImageView profilePict;
    TextView description;
    LinearLayout tasteList;
    Button tastePicker;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View profile = inflater.inflate(R.layout.frag_profile, container, false);

        coverPict = (ImageView) profile.findViewById(R.id.user_profile_photo);
        new DownloadImageTask(coverPict).execute(modelApplication.getUser().getProfilePicture());
        name = (TextView) profile.findViewById(R.id.user_profile_name);
        //TODO Get name from the database
        name.setText(modelApplication.getUser().getFirstname());
        profilePict = (ImageView) profile.findViewById(R.id.header_cover_image);
        new DownloadImageTask(profilePict).execute(modelApplication.getUser().getBackgroundPicture());
        description = (TextView) profile.findViewById(R.id.user_description);
        description.setText(modelApplication.getUser().getDescription());

        tastePicker = (Button) profile.findViewById(R.id.taste_picker);
        tasteList = (LinearLayout) profile.findViewById(R.id.taste_list);

        tastePicker.setOnClickListener(this);

        return profile;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_taste)
                .setItems(R.array.music_taste_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 1: tastePicker.setText(R.string.rock); break;
                            case 2: tastePicker.setText(R.string.metal); break;
                            case 3: tastePicker.setText(R.string.pop); break;
                            case 4: tastePicker.setText(R.string.classic); break;
                            case 5: tastePicker.setText(R.string.jazz); break;
                            case 6: tastePicker.setText(R.string.edm); break;
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}