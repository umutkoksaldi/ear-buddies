package ch.epfl.sweng.project.models;


import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public final class ModelApplication {

    private static ModelApplication modelApplication = null;
    private Music music = new Music();
    private User user = new User();
    private User[] mOtherUsers;
    private String mTest = "/";
    private List<MarkerOptions> markerOpt;
    boolean testingApp;

    private ModelApplication() {}

    // allow to have one instance of the class shared in multiple activity.
    public static synchronized ModelApplication getModelApplication()
    {
        if (modelApplication == null){
            modelApplication = new ModelApplication();
        }
        return modelApplication;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User[] getOtherUsers(){
        return mOtherUsers;
    }

    public void setOtherUsers(User[] users){
        if(!testingApp){
            mOtherUsers = users;
        }
    }

    public void setTest(){
        mTest = "Test/";
    }

    public String getTestState(){
        return mTest;
    }

    public List<MarkerOptions> getMarkerOpt() {
        return new ArrayList<>(markerOpt);
    }

    public void setMarkerOpt(List<MarkerOptions> aMarkerOption) {
        markerOpt = new ArrayList<>(aMarkerOption);
    }

    public void setTestingApp(boolean testing) {
        this.testingApp = testing;
    }
}
