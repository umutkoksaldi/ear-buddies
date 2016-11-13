package ch.epfl.sweng.project.Fragment;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;
import ch.epfl.sweng.project.R;


public class UsersFragment extends Fragment {

    private User[] usersAround;
    private String[] userNames;
    private String[] userDescription;
    private String[] images;
    public VivzAdapter adapter;

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


//ListView calls the Adapter

    public static class VivzAdapter extends ArrayAdapter<String> {

        private Context context;
        private String[] images;
        private String[] titleArray;
        private String[] descriptionArray;

        public VivzAdapter(Context c, String[] titles, String[] imgs, String[] desc) {
            //pay attention to single_row_userSong(before it was textView2)
            super(c, R.layout.single_row_userslist, R.id.single_row_usersong, titles);
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

        class ViewHolderForUsersFragment {
            ImageView userPicture;
            TextView userName;
            TextView userSong;

            ViewHolderForUsersFragment(View view) {

                userPicture = (ImageView) view.findViewById(R.id.single_row_userpicture);
                userName = (TextView) view.findViewById(R.id.single_row_username);
                userSong = (TextView) view.findViewById(R.id.single_row_usersong);
            }

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
    }
}