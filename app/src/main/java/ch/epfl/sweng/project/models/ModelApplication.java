package ch.epfl.sweng.project.models;


import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public final class ModelApplication {

    private static ModelApplication modelApplication = null;
    boolean testingApp;
    private Music music = new Music();
    private User user = new User();
    private User[] mOtherUsers;
    private String mTest = "/";
    private List<MarkerOptions> markerOpt;
    private Music lastMatchedMusic;
    private boolean matchDisplayed = false;
    private User matchedUser = null;
    private boolean zoomedOnMatch = false;

    private ModelApplication() {
    }

    // allow to have one instance of the class shared in multiple activity.
    public static synchronized ModelApplication getModelApplication() {
        if (modelApplication == null) {
            modelApplication = new ModelApplication();
        }
        return modelApplication;
    }

    public Music getLastMatchedMusic() {
        return lastMatchedMusic;
    }

    public void setLastMatchedMusic(Music lastMatchedMusic) {
        this.lastMatchedMusic = lastMatchedMusic;
    }

    public boolean isMatchDisplayed() {
        return matchDisplayed;
    }

    public void setMatchDisplayed(boolean matchDisplayed) {
        this.matchDisplayed = matchDisplayed;
    }

    public User getMatchedUser() {
        return matchedUser;
    }

    public void setMatchedUser(User matchedUser) {
        this.matchedUser = matchedUser;
    }

    public boolean isZoomedOnMatch() {
        return zoomedOnMatch;
    }

    public void setZoomedOnMatch(boolean zoomedOnMatch) {
        this.zoomedOnMatch = zoomedOnMatch;
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

    public User[] getOtherUsers() {
        return mOtherUsers;
    }

    public void setOtherUsers(User[] users) {
        if (!testingApp) {
            mOtherUsers = users;
        }
    }

    public void setTest() {
        mTest = "Test/";
    }

    public String getTestState() {
        return mTest;
    }

    public List<MarkerOptions> getMarkerOpt() {
        return new ArrayList<>(markerOpt);
    }

    public void setMarkerOpt(List<MarkerOptions> aMarkerOption) {
        markerOpt = new ArrayList<>(aMarkerOption);
    }

    public void setTestingApp(boolean testing) {
        testingApp = testing;
    }
}
