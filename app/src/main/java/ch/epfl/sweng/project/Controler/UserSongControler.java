package ch.epfl.sweng.project.Controler;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.project.Model.Music;
import ch.epfl.sweng.project.Model.User;

/**
 * Created by Antoine Merino on 14/12/2016.
 */

public class UserSongControler {

    private static UserSongControler userSongControler = null;
    private ArrayList<User> userList = new ArrayList<>();
    private HashMap<Long, Music> songMap = new HashMap<>();

    public static UserSongControler getUserSongControler() {
        if (userSongControler == null) {
            userSongControler = new UserSongControler();
        }
        return userSongControler;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public HashMap<Long, Music> getSongMap() {
        return songMap;
    }

    public void setSongMap(HashMap<Long, Music> songMap) {
        this.songMap = songMap;
    }

}
