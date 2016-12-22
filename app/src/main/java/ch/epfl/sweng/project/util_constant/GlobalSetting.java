package ch.epfl.sweng.project.util_constant;


import ch.epfl.sweng.project.BuildConfig;

@SuppressWarnings("WeakerAccess")
public final class GlobalSetting {

    // Music tags. MUST BE THE SAME ON THE SERVER
    public static final String[] TAG_MUSIC_TABLE = {"pop", "rock", "rap", "metal", "hiphop"};

    // Map location delays, in milliseconds
    public static final int MAP_LOCATION_INTERVAL = 10000;
    public static final int MAP_LOCATION_FASTEST_INTERVAL = 5000;

    // Marker bitmap size on the map
    public static final int MARKER_SIZE = 20;

    // ID of the fragments used by the Pager
    public static final int FRAGMENT_USERS = 0;
    public static final int FRAGMENT_MAP = 1;
    public static final int FRAGMENT_PROFILE = 2;

    // Size of the cache containing the requests result, in bytes (used for downloading images)
    public static final int HTTP_SIZE_CACHE = 10 * 1024 * 1024; // 10 MB
    // this one for reaching the server on heroku
    public static final String URL = "https://sw-eng-go.herokuapp.com";

    // for local server
    //public static final String URL = "http://192.168.0.14:3000";


    // html code
    public static final int GOOD_ANSWER = 200;


    // Api Definition
    public static final String MUSIC_API = "/api/Musics/";
    public static final String USER_API = "/api/Users/";
    public static final String MUSIC_HISTORY_API = "/api/Musics/history/";

    // Api parameters
    public static final String ACCESS_TOKEN = "accesToken";
    public static final String ID = "id";

    // Related to music history
    public static final int MUSIC_HISTORY_MAX_LENGTH = 10;

    // Related to users fragment
    public static final String MAP_REFRESHED = BuildConfig.APPLICATION_ID + ".maprefreshed";

    private GlobalSetting() {
    }
}
