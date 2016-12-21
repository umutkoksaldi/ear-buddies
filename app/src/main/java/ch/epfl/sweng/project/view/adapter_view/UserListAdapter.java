package ch.epfl.sweng.project.view.adapter_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.controlers.UserDetailsControler;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.models.User;
import ch.epfl.sweng.project.view.fragment.UsersFragment;
import ch.epfl.sweng.project.view.util_view.DownloadImageTask;

/**
 * Created by Antoine Merino on 13/12/2016.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter
        .UserViewHolder> {

    private ArrayList<User> userList;
    private HashMap<Long, Music> songMap;
    private UsersFragment usersFragment;

    public UserListAdapter(ArrayList<User> userList, HashMap<Long, Music> songMap, UsersFragment usersFragment, Context
            context) {
        this.userList = userList;
        this.songMap = songMap;
        this.usersFragment = usersFragment;
    }

    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_userslist, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getFirstname());
        String userDescription = user.getDescription();
        Music song = songMap.get(user.getIdApiConnection());
        String songName = null;
        if (song != null) {
            songName = song.getArtist() + " - " + song.getName();
        }

        // Fill lines 2 and 3 depending of the content available for this user
        if (userDescription != null && !userDescription.isEmpty()) {
            holder.line2.setText(userDescription);
            if (songName != null && !songName.isEmpty()) {
                holder.line3.setText("♫ " + songName);
            }
        } else {
            if (songName != null && !songName.isEmpty()) {
                holder.line2.setText("♫ " + songName);
            }
        }


        String profilePictureUrl = user.getProfilePicture();
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            new DownloadImageTask(holder.profilePicture).execute(profilePictureUrl);
        }
        // Go to the details of the user
        holder.container.setOnClickListener(new UserOnClickListener(user, usersFragment));
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    private static class UserOnClickListener implements View.OnClickListener {

        private User user;
        private UsersFragment usersFragment;


        public UserOnClickListener(User user, UsersFragment usersFragment) {
            this.user = user;
            this.usersFragment = usersFragment;
        }

        @Override
        public void onClick(View v) {
            UserDetailsControler userDetailsControler = UserDetailsControler.getConnectionControler();
            userDetailsControler.openDetailsFragment(usersFragment, user);
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected TextView line2;
        protected TextView line3;
        protected ImageView profilePicture;
        protected RelativeLayout container;

        public UserViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvUserName);
            line2 = (TextView) itemView.findViewById(R.id.tvUserLine2);
            line3 = (TextView) itemView.findViewById(R.id.tvUserLine3);
            profilePicture = (ImageView) itemView.findViewById(R.id.ivUserPicture);
            container = (RelativeLayout) itemView.findViewById(R.id.single_row_user);
        }
    }


}

