package ch.epfl.sweng.project.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Webview.CustomTabActivityHelper;
import ch.epfl.sweng.project.Webview.WebviewFallback;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Antoine Merino on 13/12/2016.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter
        .UserViewHolder> {

    private ArrayList<User> userList;

    public UserListAdapter(ArrayList<User> userList, Context context) {
        this.userList = userList;
    }

    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_userslist, parent, false);
        UserListAdapter.UserViewHolder viewHolder = new UserListAdapter
                .UserViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserListAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getFirstname());
        String song;
        // TODO Make the request to the server
        holder.song.setText("Listening: <coming soon...>");

        String profilePictureUrl = user.getProfilePicture();
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            new DownloadImageTask(holder.profilePicture).execute(profilePictureUrl);
        }
        // Should open a website, replace the null argument with the desired url
        holder.container.setOnClickListener(new UserOnClickListener(null));
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    private static class UserOnClickListener implements View.OnClickListener {
        // Currently, this listener redirects to a browser page contained the parsed URL
        // You can change this, let say, to redirect the user to another fragment !
        // Ask Antoine
        String url;

        public UserOnClickListener(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            if (url == null) {
                // No lastfm page associated with the current song
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string
                        .no_user_facebook_page_found), Toast.LENGTH_SHORT).show();
            } else {
                // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
                // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                // and launch the desired Url with CustomTabsIntent.launchUrl()
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                Activity activity = (Activity) v.getContext();
                CustomTabActivityHelper.openCustomTab(
                        activity,
                        customTabsIntent,
                        Uri.parse(url),
                        new WebviewFallback()
                );
            }
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected TextView song;
        protected ImageView profilePicture;
        protected RelativeLayout container;

        public UserViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvUserName);
            song = (TextView) itemView.findViewById(R.id.tvUserSong);
            profilePicture = (ImageView) itemView.findViewById(R.id.ivUserPicture);
            container = (RelativeLayout) itemView.findViewById(R.id.single_row_user);
        }
    }

}

