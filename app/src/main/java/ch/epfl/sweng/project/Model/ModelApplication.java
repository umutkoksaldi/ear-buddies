package ch.epfl.sweng.project.Model;


public class ModelApplication {

    private static ModelApplication modelApplication = null;
    private User user;
    private User[] mOtherUsers;


    private ModelApplication() {}


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // allow to have one instance of the class shared in multiple activity.
    public static synchronized ModelApplication getModelApplication()
    {
        if (modelApplication == null)
        { 	modelApplication = new ModelApplication();
        }
        return modelApplication;
    }

    public User[] getOtherUsers(){
        return mOtherUsers;
    }

    public void setOtherUsers(User[] users){
        mOtherUsers = users;
    }
}
