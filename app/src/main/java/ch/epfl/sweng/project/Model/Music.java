package ch.epfl.sweng.project.Model;

/**
 * Created by Antoine Merino on 28/10/2016.
 */

public class Music {
    private String musicName;
    private String artistName;


    public Music() {

    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Music) {
            Music that = (Music) other;
            result = (getArtistName() == that.getArtistName() && getMusicName() == that.getMusicName());
        }
        return result;
    }
}
