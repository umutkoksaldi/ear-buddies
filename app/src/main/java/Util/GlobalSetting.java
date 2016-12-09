package Util;


@SuppressWarnings("WeakerAccess")
public class GlobalSetting {

    // Size of the cache containing the requests result, in bytes (used for downloading images)
    public static final int HTTP_SIZE_CACHE = 10 * 1024 * 1024; // 10 MB
    // this one for reaching the server on heroku
    public static final String URL = "https://sw-eng-go.herokuapp.com";

    // for local server
    //public static final String URL = "http://192.168.0.164:3000";


    // html code
    public static final int GOOD_ANSWER = 200;


    // Api Definition
    public static final String MUSIC_API = "/api/Musics/";
    public static final String USER_API = "/api/Users/";
    public static final String MUSIC_HISTORY_API = "/api/Musics/history/";
    //public static final String UPDATE_CONNEXION_USER = "/api/Users/update/";

    // Related to music history
    public static final int MUSIC_HISTORY_MAX_LENGTH = 10;
}
