package ch.epfl.sweng.project;

import ch.epfl.sweng.project.Model.ModelApplication;
import ch.epfl.sweng.project.Model.User;

/**
 * Created by Antoine Merino on 10/11/2016.
 */

public class GlobalTestSettings {
    public static final String MOCK_ID_FACEBOOK = "121620614972695";
    public static final String MOCK_ACCESS_TOKEN_FACEBOOK =
            "EAAOZCzloFDqEBAHGnY8Q6I4d6fJRy9c6FWYZAqNxp2ChFBvpv8ZAycQC7a0oT21ZBp0Ku" +
                    "IbZCIUkLWSH4Ev7pIQrjlzAxvrfznhXZAeb8A3ZCZBDks8WekNs4WgtfteZCMhUPQx5ZBPmbBMfwBgjqqAeaHOjtYFe38VYfXV35ZCnQ0y" +
                    "ZBzPSDzCKDBBMkGhWA8ZAyrJAcBZA6LCi5XtgZDZD";
    public static final String MOCK_USER_PROFILE_PICTURE = "https://scontent-amt2-1.xx.fbcdn.net/v/t1" +
            ".0-9/419921_334072353301892_257076548_n" +
            ".jpg?oh=1da08f8d32b10b20958a3df2f18096fa&oe=5886B16B";
    public static final String MOCK_USER_COVER_PICTURE = "";
    public static final String MOCK_USER_EMAIL = "please_send_spam@gmail.com";
    public static final int MOCK_USER_AGE = 22;
    public static final String MOCK_USER_FIRST_NAME = "Alex";
    public static final String MOCK_USER_LAST_NAME = "Terrieur";


    public static void createFakeUser() {
        User mUser = new User();
        mUser.setLocation(new ch.epfl.sweng.project.Model.Location(0, 0));
        mUser.setAge(MOCK_USER_AGE);
        mUser.setBackgroundPicture(MOCK_USER_COVER_PICTURE);
        mUser.setProfilePicture(MOCK_USER_PROFILE_PICTURE);
        mUser.setEmail(MOCK_USER_EMAIL);
        mUser.setFirstname(MOCK_USER_FIRST_NAME);
        mUser.setLastname(MOCK_USER_LAST_NAME);
        mUser.setIdApiConnection(Long.parseLong(GlobalTestSettings.MOCK_ID_FACEBOOK));
        ModelApplication.getModelApplication().setUser(mUser);
    }
}


