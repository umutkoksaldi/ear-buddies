package ch.epfl.sweng.project.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Login;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFrag extends Fragment implements View.OnClickListener{

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    ImageView coverPict;
    TextView name;
    ImageView profilePict;
    TextView description;
    LinearLayout tasteList;
    Button tastePicker;
    ImageButton musicHistoryButton;
    Button rangeButton;
    Button deleteButton;
    Button logOut;
    ImageButton optionsButton;
    ServiceHandler serviceHandler;

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
        rangeButton = (Button) profile.findViewById(R.id.range_button);
        deleteButton = (Button) profile.findViewById(R.id.delete_button);
        optionsButton = (ImageButton) profile.findViewById(R.id.options_button);
        logOut = (Button) profile.findViewById(R.id.log_out);

        logOut.setOnClickListener(this);
        rangeButton.setOnClickListener(this);
        tastePicker.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        optionsButton.setOnClickListener(this);

        serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {
                    Toast.makeText(getApplicationContext(), getString(R.string.success_message), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_connexion_facebook), Toast
                            .LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed() {
                // Toast.makeText(getApplicationContext(), getString(R.string.error_connexion_facebook), Toast
                //       .LENGTH_SHORT).show();
            }
        });


        // Music history button
        musicHistoryButton = (ImageButton) profile.findViewById(R.id.musicHistoryButton);
        musicHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View row) {
                //int profileNumber = position + 1;
                Fragment musicHistoryFragment = new MusicHistoryFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_up)
                        .replace(R.id.music_history_fragment_container, musicHistoryFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        return profile;
    }

    @Override
    public void onClick(View v) {
        if (v == tastePicker) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.pick_taste)
                    .setItems(R.array.music_taste_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    tastePicker.setText(R.string.rock);
                                    //modelApplication.getUser().setTaste("Rock");
                                    break;
                                case 1:
                                    tastePicker.setText(R.string.metal);
                                    //modelApplication.getUser().setTaste("Metal");
                                    break;
                                case 2:
                                    tastePicker.setText(R.string.pop);
                                    //modelApplication.getUser().setTaste("Pop");
                                    break;
                                case 3:
                                    tastePicker.setText(R.string.classic);
                                    //modelApplication.getUser().setTaste("Classic");
                                    break;
                                case 4:
                                    tastePicker.setText(R.string.jazz);
                                    //modelApplication.getUser().setTaste("Jazz");
                                    break;
                                case 5:
                                    tastePicker.setText(R.string.edm);
                                    //modelApplication.getUser().setTaste("EDM");
                                    break;
                            }
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (v == rangeButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.set_range)
                    .setItems(R.array.range_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    rangeButton.setText(R.string.hundred_meters);
                                    //modelApplication.getUser().setRange(100);
                                    break;
                                case 1:
                                    rangeButton.setText(R.string.twofifty_meters);
                                    //modelApplication.getUser().setRange(250);
                                    break;
                                case 2:
                                    rangeButton.setText(R.string.fivehundred_meters);
                                    //modelApplication.getUser().setRange(500);
                                    break;
                                case 3:
                                    rangeButton.setText(R.string.one_kilometer);
                                    //modelApplication.getUser().setRange(1000);
                                    break;
                            }
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (v == deleteButton){
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.delete_user_alert)
                    .setMessage(getString(R.string.delete_warning) +
                            getString(R.string.delete_message))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if (v == logOut) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.log_out_message)
                    .setMessage(R.string.log_out_warning)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), Login.class));
                            getActivity().finish();
                            logOutFace();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if (v == optionsButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.user_options)
                    .setItems(R.array.user_opt_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle(R.string.enter_name);

                                    final EditText input = new EditText(getActivity());

                                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                                    builder.setView(input);

                                    builder.setPositiveButton(android.R.string.yes, new DialogInterface
                                            .OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            modelApplication.getUser().setFirstname(input.getText().toString());
                                            modelApplication.getUser().setLastname(getString(R.string.emtpy_string));
                                            Map<String, String> params = new HashMap<>();
                                            params.put("firstname", input.getText().toString());
                                            params.put("lastname", "");

                                            serviceHandler.doPut(params, GlobalSetting.URL + GlobalSetting.USER_API +
                                                    modelApplication.getUser().getIdApiConnection());
                                            name.setText(input.getText().toString());
                                        }
                                    });
                                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener
                                            () {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
                                    break;
                                }
                                case 1:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle(R.string.description_message);

                                    final EditText input = new EditText(getActivity());

                                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                                    builder.setView(input);

                                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener
                                            () {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            modelApplication.getUser().setDescrition(input.getText().toString());
                                            Map<String, String> params = new HashMap<>();
                                            params.put("description", input.getText().toString());

                                            serviceHandler.doPut(params, GlobalSetting.URL + GlobalSetting.USER_API +
                                                    modelApplication.getUser().getIdApiConnection());
                                            description.setText(input.getText().toString());
                                        }
                                    });
                                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener
                                            () {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
                                    break;

                            }
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void logOutFace() {
        Log.i("logOutFace()", "Disconnection facebook");
        LoginManager.getInstance().logOut();
    }

    private void deleteUser() {
        serviceHandler.doDelete(GlobalSetting.URL + GlobalSetting.USER_API + modelApplication.getUser().getIdApiConnection());
        startActivity(new Intent(getActivity(), Login.class));
        getActivity().finish();
        logOutFace();
    }
}