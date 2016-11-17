package ch.epfl.sweng.project.Model;


public class ModelApplication {

    private static ModelApplication modelApplication = null;
    private Music music = new Music();
    private User user = new User();
    private User[] mOtherUsers;
    private String mTest = "/";

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
        mOtherUsers = users;
    }

    public void setTest(){
        mTest = "Test/";
    }

    public String getTestState(){
        return mTest;
    }
}
