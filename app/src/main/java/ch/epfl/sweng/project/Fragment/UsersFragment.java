package ch.epfl.sweng.project.Fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ServerRequest.OnServerRequestComplete;
import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UsersFragment extends Fragment {

    public UserListAdapter adapter;
    private ArrayList<User> userList;
    private HashMap<User, Music> songMap = new HashMap<>();
    private User[] usersAround;
    private String[] userNames;
    private String[] userDescription;
    private String[] images;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_userslist, container, false);

        Resources res = getResources();


        usersAround = ModelApplication.getModelApplication().getOtherUsers();
        if (usersAround == null) {
            Log.i("No users", "" + 0);
            return view;
        }
        // Fill the user list with what the application model contains
        userList = new ArrayList<>(Arrays.asList(usersAround));
        for (User user : userList) {
            sendGet(GlobalSetting.MUSIC_API, user);
        }

        userNames = new String[usersAround.length];
        userDescription = new String[usersAround.length];
        images = new String[usersAround.length];

        // Get each user's name, description and profile picture
        for (int i = 0; i < usersAround.length; i++) {
            images[i] = usersAround[i].getProfilePicture();
            userNames[i] = usersAround[i].getFirstname();
            userDescription[i] = usersAround[i].getLastname();
        }

        // Find the recycler view to fill it with users
        recyclerView = (RecyclerView) view.findViewById(R.id.user_recyclerview);
        // Disable nested scrolling, otherwise scrolling experience is messed up (you can try removing this line to
        // see the difference)
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Connect the recycler view with the actual user list through an adapter so it will be updated automatically
        adapter = new UserListAdapter(userList, songMap, getApplicationContext());
        recyclerView.setAdapter(adapter);


        return view;
    }


    private void sendGet(@SuppressWarnings("SameParameterValue") String
                                 requestApi, final User user) {
        Log.d("UserListAdapter", "sendGet");
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {

                    //modelApplication.setMusic((Music) response.getBody());
                    Music music = (Music) response.getBody();
                    songMap.put(user, music);
                    adapter.notifyDataSetChanged();
                } else {
                    // Erreur pas pu communiquer avec le serveur
                    Log.e("UserFragment", "onSuccess() != Code 200 (good answer)");
                }
            }

            @Override
            public void onFailed() {
                Log.e("UserFragment", "onFailed() : could not retreive the info from the server about the song");
            }
        });


        // the interface is already initiate above
        String requestURL = GlobalSetting.URL + requestApi + user.getCurrentMusicId();
        Log.d("UserFragment", "GET Request : " + requestURL);
        serviceHandler.doGet(requestURL, Music.class);
    }
}