package ch.epfl.sweng.project.models;

import ch.epfl.sweng.project.R;

import static ch.epfl.sweng.project.util_constant.GlobalSetting.TAG_MUSIC_TABLE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Antoine Merino on 28/10/2016.
 */

public class Music {
    private String id;
    private String artist;
    private String name;
    private String url;
    private String tag;
    private String urlPicture;


    public Music() {

    }

    public Music(String artist, String name) {
        this.artist = artist;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    /**
     * Set a tag in the song accordingly to the user language.
     * Eg. If the tag is "metal", it will be set as "Metal" if the locale is English
     * @param tag must match one of the tags known by the server
     */
    public void setTag(String tag) {
        String[] formatedTags = getApplicationContext().getResources().getStringArray(R.array.music_taste_array);
        String result = "unknown";
        int i = 0;
        while (i < TAG_MUSIC_TABLE.length) {
            if (TAG_MUSIC_TABLE[i].equals(tag)) {
                result = formatedTags[i];
                break;
            }
            ++i;
        }
        this.tag = result;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
