package ch.epfl.sweng.project.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;


/**
 * Created by Dusan Viktor on 2016-11-18.
 */


public class DetailsFragment extends Fragment {

    private ImageView picture;
    private TextView description;
    private TextView nameDetails;
    private User user;


    private FragmentManager manager;

    public void setUser(User user) {
        this.user = user;
    }


    @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_fragment, container, false);
        final Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        nameDetails = (TextView) view.findViewById(R.id.details_name);
        picture = (ImageView) view.findViewById(R.id.details_fragment_picture);
        description = (TextView) view.findViewById(R.id.details_description);

        onUserClick();

        return view;
    }


   public void onUserClick() {
       nameDetails.setText(user.getFirstname());
       description.setText(user.getDescription());
       new DownloadImageTask(picture).execute(user.getProfilePicture());
   }

}
