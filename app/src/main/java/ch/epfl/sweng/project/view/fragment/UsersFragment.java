package ch.epfl.sweng.project.view.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.springframework.http.ResponseEntity;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.controlers.UserSongControler;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.server_request.OnServerRequestComplete;
import ch.epfl.sweng.project.server_request.ServiceHandler;
import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.view.adapter_view.UserListAdapter;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UsersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public UserListAdapter adapter;
    private UserSongControler userSongControler;
    private User[] usersAround;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("UsersFragment", "Received intent: " + intent.getAction());
            refreshUserList();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("UsersFragment", "onCreateView()");

        View view = inflater.inflate(R.layout.frag_userslist, container, false);


        // Register the receiver to update the user list at the same time of the map
        IntentFilter iF = new IntentFilter();
        iF.addAction(GlobalSetting.MAP_REFRESHED);
        Log.d("UsersFragment", "Broadcast receiver for action: " + GlobalSetting.MAP_REFRESHED);
        getContext().registerReceiver(mReceiver, iF);

        // Find the recycler view to fill it with users
        recyclerView = (RecyclerView) view.findViewById(R.id.user_recyclerview);
        // Disable nested scrolling, otherwise scrolling experience is messed up (you can try removing this line to
        // see the difference)
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Connect the recycler view with the actual user list through an adapter so it will be updated automatically
        userSongControler = UserSongControler.getUserSongControler();
        adapter = new UserListAdapter(userSongControler.getUserList(), userSongControler.getSongMap(), this,
                getApplicationContext());
        recyclerView.setAdapter(adapter);

        // Fill the user list with what the application model contains
        refreshUserList();

        // Set up the swipe-to-refresh
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.users_swipe_container);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.primary),
                getResources().getColor(R.color.color_accent),
                getResources().getColor(R.color.primary_dark),
                getResources().getColor(R.color.color_accent));

        return view;
    }

    private void refreshUserList() {
        usersAround = ModelApplication.getModelApplication().getOtherUsers();
        if (usersAround != null) {
            // Repopulate the list with new users
            userSongControler.getUserList().clear();
            for (User user : usersAround) {
                userSongControler.getUserList().add(user);
            }
            // We can already fill the recycler view
            adapter.notifyDataSetChanged();
            // Ask to the server the song of each user
            boolean noRequestMade = true;
            for (User user : userSongControler.getUserList()) {
                if (user.getCurrentMusicId() != 0) {
                    noRequestMade = false;
                    sendGet(GlobalSetting.MUSIC_API, user);
                }
                if (noRequestMade && swipeContainer != null) {
                    swipeContainer.setRefreshing(false);
                }
            }

        } else {
            if (swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }
            Log.i("UsersFragment", "No users around");
        }
    }


    private void sendGet(@SuppressWarnings("SameParameterValue") String
                                 requestApi, final User user) {
        Log.d("UsersFragment", "sendGet");
        ServiceHandler serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                if (Integer.parseInt(response.getStatusCode().toString()) == GlobalSetting.GOOD_ANSWER) {

                    //modelApplication.setMusic((Music) response.getBody());
                    Music music = (Music) response.getBody();
                    userSongControler.getSongMap().put(user.getIdApiConnection(), music);
                    adapter.notifyDataSetChanged();
                    if (swipeContainer != null) {
                        swipeContainer.setRefreshing(false);
                    }
                } else {
                    // Error, couldn't communicate with the server
                    Log.e("UsersFragment", "onSuccess() != Code 200 (good answer)");
                }
            }

            @Override
            public void onFailed() {
                Log.e("UsersFragment", "onFailed() : could not retreive the info from the server about the song");
            }
        });


        // the interface is already initiate above
        String requestURL = GlobalSetting.URL + requestApi + user.getCurrentMusicId();
        Log.d("UsersFragment", "GET Request : " + requestURL);
        serviceHandler.doGet(requestURL, Music.class);
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    @Override
    public void onRefresh() {
        Log.d("UsersFragment", "Swipe to refresh");
        refreshUserList();
    }
}