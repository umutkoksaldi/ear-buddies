package ch.epfl.sweng.project.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.Webview.CustomTabActivityHelper;
import ch.epfl.sweng.project.Webview.WebviewFallback;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UsersFragment extends Fragment {

    public VivzAdapter adapter;
    private User[] usersAround;
    private String[] userNames;
    private String[] userDescription;
    private String[] images;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_userslist, container, false);

        Resources res = getResources();


        usersAround = ModelApplication.getModelApplication().getOtherUsers();
        if (usersAround == null) {
            Log.i("No users", "" + 0);
            return view;
        }

        userNames = new String[usersAround.length];
        userDescription = new String[usersAround.length];
        images = new String[usersAround.length];

        int userIndex;
        for (userIndex = 0; userIndex < usersAround.length; userIndex++) {
            images[userIndex] = usersAround[userIndex].getProfilePicture();
            userNames[userIndex] = usersAround[userIndex].getFirstname();
            userDescription[userIndex] = usersAround[userIndex].getLastname();
        }

        ListView list = (ListView) view.findViewById(R.id.listView);

        adapter = new VivzAdapter(getContext(), userNames, images, userDescription);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long
                                                id) {
                                            int profileNumber = position + 1;
                                            Toast.makeText(getActivity(), "You clicked on profile number: " +
                                                    profileNumber, Toast.LENGTH_SHORT).show();
                                        }
                                    }
        );

        return view;
    }


    public static class UserListAdapter extends RecyclerView.Adapter<UsersFragment.UserListAdapter
            .UserViewHolder> {

        private ArrayList<User> userList;

        public UserListAdapter(ArrayList<User> userList, Context context) {
            this.userList = userList;
        }

        @Override
        public UsersFragment.UserListAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_userslist, parent, false);
            UsersFragment.UserListAdapter.UserViewHolder viewHolder = new UsersFragment.UserListAdapter
                    .UserViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(UsersFragment.UserListAdapter.UserViewHolder holder, int position) {
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
            holder.container.setOnClickListener(new UsersFragment.UserOnClickListener(null));
        }


        public User getSongByPosition(int pos) {
            return userList.get(pos);
        }

        @Override
        public int getItemCount() {
            return userList.size();
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

//ListView calls the Adapter

    public static class VivzAdapter extends ArrayAdapter<String> {

        private Context context;
        private String[] images;
        private String[] titleArray;
        private String[] descriptionArray;

        public VivzAdapter(Context c, String[] titles, String[] imgs, String[] desc) {
            //pay attention to single_row_userSong(before it was textView2)
            super(c, R.layout.single_row_userslist, R.id.tvUserSong, titles);
            context = c;
            images = imgs;
            titleArray = titles;
            descriptionArray = desc;
        }

        public String getSongByPosition(int pos) {
            String song = "";
            if (descriptionArray == null) Log.i("UsersFragment", "No songs are being listening");
            else if (descriptionArray.length < pos + 1) Log.i(" UsersFragment ", "Song Not Found");
            else song = descriptionArray[pos];
            return song;
        }

        public String getImageByPosition(int pos) {
            String picture = "";
            if (images == null) Log.i("UsersFragment", "No pictures to show");
            else if (images.length < pos + 1) Log.i(" UsersFragment ", "Picture Not Found");
            else picture = images[pos];
            return picture;
        }

        public String getNameByPosition(int pos) {
            String name = "";
            if (titleArray == null) Log.i("UsersFragment", "No names to show");
            else if (titleArray.length < pos + 1) Log.i(" UsersFragment ", "Name Not Found");
            else name = titleArray[pos];
            return name;

        }

        //This metod is called each time a row has to be displayed to a user
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            ViewHolderForUsersFragment holder;
            View row = convertView;
            //If row is empty we have to create new objects
            //Make the view holders in order to optimize the list
            //This is being done only for the first time we want to display the list of users
            if (row == null) {
                //Create a java object from xml
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Contains the reference to Relative Layout in single_row
                row = inflater.inflate(R.layout.single_row_userslist, parent, false);
                holder = new ViewHolderForUsersFragment(row);
                row.setTag(holder);

            }
            // if row is not empty then we are going to recycle
            else {
                holder = (ViewHolderForUsersFragment) row.getTag();
            }

            new DownloadImageTask(holder.userPicture).execute(images[position]);
            holder.userName.setText(titleArray[position]);
            holder.userSong.setText(descriptionArray[position]);

            return row;

        }

        class ViewHolderForUsersFragment {
            ImageView userPicture;
            TextView userName;
            TextView userSong;

            ViewHolderForUsersFragment(View view) {

                userPicture = (ImageView) view.findViewById(R.id.ivUserPicture);
                userName = (TextView) view.findViewById(R.id.tvUserName);
                userSong = (TextView) view.findViewById(R.id.tvUserSong);
            }

        }
    }
}