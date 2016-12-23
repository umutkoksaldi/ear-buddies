package ch.epfl.sweng.project.util_constant;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.project.models.Location;
import ch.epfl.sweng.project.models.ModelApplication;
import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.models.Setting;
import ch.epfl.sweng.project.models.User;

/**
 * Created by Antoine Merino on 10/11/2016.
 */

public final class GlobalTestSettings {


    // ************************************************************************************************************************************************
    // Main activity

    // ************************************************************************************************************************************************
    // Time to wait for requests, in milliseconds
    public static final int SHORT_REQUEST_DELAY = 2000;
    public static final int MEDIUM_REQUEST_DELAY = 5000;
    public static final int LONG_REQUEST_DELAY = 10000;


    // Dialog buttons
    public static final int BUTTON_OK = android.R.id.button1;
    public static final int BUTTON_CANCEL = android.R.id.button2;

    // Mock Users Data
    public static final String MOCK_ID_FACEBOOK = "121620614972695";
    public static final String MOCK_ACCESS_TOKEN_FACEBOOK =
            "EAAOZCzloFDqEBAHGnY8Q6I4d6fJRy9c6FWYZAqNxp2ChFBvpv8ZAycQC7a0oT21ZBp0Ku" +
                    "IbZCIUkLWSH4Ev7pIQrjlzAxvrfznhXZAeb8A3ZCZBDks8WekNs4WgtfteZCMhUPQx5ZBPmbBMfwBgjqqAeaHOjtYFe38VYfXV35ZCnQ0y" +
                    "ZBzPSDzCKDBBMkGhWA8ZAyrJAcBZA6LCi5XtgZDZD";
    public static final String MOCK_USER_PROFILE_PICTURE = "https://scontent-amt2-1.xx.fbcdn.net/v/t1" +
            ".0-9/419921_334072353301892_257076548_n" +
            ".jpg?oh=1da08f8d32b10b20958a3df2f18096fa&oe=5886B16B";

    // ************************************************************************************************************************************************
    public static final String MOCK_USER_COVER_PICTURE = "";
    public static final String MOCK_USER_EMAIL = "please_send_spam@gmail.com";
    public static final int MOCK_USER_AGE = 22;
    public static final String MOCK_USER_FIRST_NAME = "Sweng";
    public static final String MOCK_USER_LAST_NAME = "Rocks";
    public static final String MOCK_USER_DESCRIPTION = "Swaggy Swaggy SwEng";
    public static final long MOCK_USER_SONG_ID = 1;


    // Sample song to test
    public static final String ARTIST_NAME_REQUEST = "rihana";
    public static final String MUSIC_NAME_REQUEST = "rude boy";
    public static final int ID_MUSIC = 1;

    // This is what the server should answer
    public static final String ARTIST_NAME_RESPONSE = "Rihanna";
    public static final String MUSIC_NAME_RESPONSE = "Rude Boy";

    public static String USERS_TAB = "People";
    public static String PROFILE_TAB = "Profile";
    public static String MAP_TAB = "Map";

    public static List<String> NAME_USER = Arrays.asList("Name Test 1", "Name Test 2", "Name Test 3",
            "Name Test 4", "Name Test 5");

    public static int MAX_ITERATION_BIND = 100;

    private GlobalTestSettings() {
    }
    // ************************************************************************************************************************************************

    public static void createMockUser() {
        User mUser = new User();
        mUser.setLocation(new ch.epfl.sweng.project.models.Location(0, 0));
        mUser.setSetting(new Setting());
        mUser.setAge(MOCK_USER_AGE);
        mUser.setBackgroundPicture(MOCK_USER_COVER_PICTURE);
        mUser.setProfilePicture(MOCK_USER_PROFILE_PICTURE);
        mUser.setEmail(MOCK_USER_EMAIL);
        mUser.setCurrentMusicId(ID_MUSIC);
        mUser.setFirstname(MOCK_USER_FIRST_NAME);
        mUser.setLastname(MOCK_USER_LAST_NAME);
        mUser.setIdApiConnection(Long.parseLong(GlobalTestSettings.MOCK_ID_FACEBOOK));
        ModelApplication.getModelApplication().setUser(mUser);
    }

    public static void createMockUsers() {

        Location location = new Location(40, 32);
        User[] users = new User[NAME_USER.size()];
        for (int i = 0; i < NAME_USER.size(); i++) {
            User mUser = new User();
            mUser.setLocation(location);
            mUser.setSetting(new Setting());
            mUser.setFirstname(NAME_USER.get(i));
            mUser.setAge(MOCK_USER_AGE);
            mUser.setCurrentMusicId(ID_MUSIC);
            mUser.setBackgroundPicture(MOCK_USER_COVER_PICTURE);
            mUser.setProfilePicture(MOCK_USER_PROFILE_PICTURE);
            mUser.setEmail(MOCK_USER_EMAIL);
            mUser.setLastname(MOCK_USER_LAST_NAME);
            mUser.setIdApiConnection(Long.parseLong(GlobalTestSettings.MOCK_ID_FACEBOOK));
            users[i] = mUser;
        }

        ModelApplication.getModelApplication().setOtherUsers(users);

    }

    public static void mockMusicPlayed() {
        Music music = new Music();
        music.setId("1");
        music.setArtist("Rihanna");
        music.setName("Umbrella");
        music.setUrl("https://www.last.fm/music/Rihanna/_/Umbrella");
        music.setTag("pop");
        music.setUrlPicture("https://lastfm-img2.akamaized.net/i/u/34s/a60d9d9ae55226699420b52ab28d3ad0.png");
        ModelApplication.getModelApplication().setMusic(music);
    }
}


