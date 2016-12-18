package ch.epfl.sweng.project.view.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.medias.MusicHistory;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.server_request.OnServerRequestComplete;
import ch.epfl.sweng.project.server_request.ServiceHandler;
import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.view.activity.LoginActivity;
import ch.epfl.sweng.project.view.adapter_view.MusicListAdapter;
import ch.epfl.sweng.project.view.util_view.DownloadImageTask;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final ModelApplication modelApplication = ModelApplication.getModelApplication();
    // Music history
    private MusicHistory musicHistory;
    private MusicListAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    // Enable swipe to refresh the fragment profile (music history)
    private SwipeRefreshLayout swipeContainer;


    private ImageView profilePicture;
    private TextView name;
    private ImageView coverPicture;
    private TextView description;
    private LinearLayout tasteList;
    private ImageButton tastePicker;
    private TextView taste;
    private ImageButton rangeButton;
    private ImageButton menuButton;
    private TextView range;
    private ImageButton optionsButton;
    private ServiceHandler serviceHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ProfileFragment", "onCreateView()");

        View profile = inflater.inflate(R.layout.frag_profile, container, false);
        profilePicture = (ImageView) profile.findViewById(R.id.user_profile_photo);
        new DownloadImageTask(profilePicture).execute(modelApplication.getUser().getProfilePicture());
        name = (TextView) profile.findViewById(R.id.user_profile_name);
        name.setText(modelApplication.getUser().getFirstname());
        coverPicture = (ImageView) profile.findViewById(R.id.header_cover_image);
        new DownloadImageTask(coverPicture).execute(modelApplication.getUser().getBackgroundPicture());
        description = (TextView) profile.findViewById(R.id.user_description);
        description.setText(modelApplication.getUser().getDescription());
        tastePicker = (ImageButton) profile.findViewById(R.id.button_profile_music_tag);
        taste = (TextView) profile.findViewById(R.id.tv_profile_music_tag);
        //String musicTaste = modelApplication.getUser().getSetting().getMusicTaste();
        //taste.setText();
        rangeButton = (ImageButton) profile.findViewById(R.id.button_profile_radar);
        range = (TextView) profile.findViewById(R.id.tv_profile_radar);
        menuButton = (ImageButton) profile.findViewById(R.id.button_profile_menu);
        rangeButton.setOnClickListener(this);
        tastePicker.setOnClickListener(this);
        menuButton.setOnClickListener(this);

        // Setting up the music history
        musicHistory = MusicHistory.getMusicHistory();
        ArrayList<Music> musicList = musicHistory.getHistory();
        recyclerView = (RecyclerView) profile.findViewById(R.id.music_history_recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MusicListAdapter(musicList, getApplicationContext());
        recyclerView.setAdapter(adapter);


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
                Log.e("Profile", "Cannot connect to server");
            }
        });

        musicHistory.updateFromServer(adapter, swipeContainer);

        swipeContainer = (SwipeRefreshLayout) profile.findViewById(R.id.profile_swipe_container);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.primary),
                getResources().getColor(R.color.color_accent),
                getResources().getColor(R.color.primary_dark),
                getResources().getColor(R.color.color_accent));

        return profile;
    }

    @Override
    public void onRefresh() {
        //UpdateMusicHistoryTask task = new UpdateMusicHistoryTask();
        //task.execute(adapter);
        musicHistory.updateFromServer(adapter, swipeContainer);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(menuButton)) {
            showMoreMenu(v);
        } else if (v.equals(tastePicker)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.choose_your_music_taste)
                    .setItems(R.array.music_taste_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    taste.setText(R.string.rock);
                                    //modelApplication.getUser().setTaste("Rock");
                                    break;
                                case 1:
                                    taste.setText(R.string.metal);
                                    //modelApplication.getUser().setTaste("Metal");
                                    break;
                                case 2:
                                    taste.setText(R.string.pop);
                                    //modelApplication.getUser().setTaste("Pop");
                                    break;
                                case 3:
                                    taste.setText(R.string.classic);
                                    //modelApplication.getUser().setTaste("Classic");
                                    break;
                                case 4:
                                    taste.setText(R.string.jazz);
                                    //modelApplication.getUser().setTaste("Jazz");
                                    break;
                                case 5:
                                    taste.setText(R.string.edm);
                                    //modelApplication.getUser().setTaste("EDM");
                                    break;
                            }
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (v.equals(rangeButton)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.set_range)
                    .setItems(R.array.range_array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    range.setText(R.string.hundred_meters);
                                    //modelApplication.getUser().setRange(100);
                                    break;
                                case 1:
                                    range.setText(R.string.twofifty_meters);
                                    //modelApplication.getUser().setRange(250);
                                    break;
                                case 2:
                                    range.setText(R.string.fivehundred_meters);
                                    //modelApplication.getUser().setRange(500);
                                    break;
                                case 3:
                                    range.setText(R.string.one_kilometer);
                                    //modelApplication.getUser().setRange(1000);
                                    break;
                            }
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    // Handling all menus contained in the fragment
    public void showMoreMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.music_taste_picker, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_name:
                menuEditName();
                return true;
            case R.id.menu_edit_description:
                menuEditDescription();
                return true;
            case R.id.menu_logout:
                menuLogout();
                return true;
            case R.id.menu_delete_account:
                menuDeleteAccount();
                return true;
            default:
                return false;
        }
    }

    private void menuEditName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.enter_name);

        final EditText input = new EditText(getActivity());
        input.setText(modelApplication.getUser().getFirstname());
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
    }

    private void menuEditDescription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.description_message);

        final EditText input = new EditText(getActivity());
        String description;
        if (modelApplication.getUser().getDescription() != null) {
            description = modelApplication.getUser().getDescription();
        } else {
            description = getString(R.string.default_description);
        }
        input.setText(description);
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
                ProfileFragment.this.description.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void menuLogout() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.log_out_message)
                .setMessage(R.string.log_out_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        logOutFace();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.ic_warning_primary_24dp)
                .show();
    }

    private void menuDeleteAccount() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_user_alert)
                .setMessage(getString(R.string.delete_warning) +
                        getString(R.string.delete_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUser();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.ic_warning_primary_24dp)
                .show();
    }

    private void logOutFace() {
        Log.i("logOutFace()", "Disconnection facebook");
        LoginManager.getInstance().logOut();
    }

    private void deleteUser() {
        serviceHandler.doDelete(GlobalSetting.URL + GlobalSetting.USER_API + modelApplication.getUser().getIdApiConnection());
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
        logOutFace();
    }


}