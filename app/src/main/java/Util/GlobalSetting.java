package Util;


@SuppressWarnings("WeakerAccess")
public class GlobalSetting {


    // this one for reaching the server on heroku
    public static final String URL = "https://sw-eng-go.herokuapp.com";

    /*
    // if you run the server on local you this configuration.
    public static final String LOCALHOSTEMUl = "http://10.0.2.2:";
    public static final String PORT = "3000";
    public static final String URLSERVEREMUL = LOCALHOSTEMUl+PORT;
    */


    // html code
    public static final int GOOD_AWNSER = 200;


    // Api Definition
    public static final String  USER_API = "/api/Users/";
    //public static final String UPDATE_CONNEXION_USER = "/api/Users/update/";
}
