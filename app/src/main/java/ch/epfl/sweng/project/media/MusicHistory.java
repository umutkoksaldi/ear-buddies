package ch.epfl.sweng.project.media;

import java.util.ArrayList;
import java.util.List;

import Util.GlobalSetting;
import ch.epfl.sweng.project.Model.Music;

/**
 * Created by Antoine Merino on 18/11/2016.
 */

public final class MusicHistory {

    private static MusicHistory musicHistory = null;
    private int length = GlobalSetting.MUSIC_HISTORY_MAX_LENGTH;
    private List<Music> musicHistoryList = new ArrayList<>();

    private MusicHistory() {
    }

    // allow to have one instance of the class shared in multiple activity.
    public static synchronized MusicHistory getMusicHistory() {
        if (musicHistory == null) {
            musicHistory = new MusicHistory();
        }
        return musicHistory;
    }

    public void setLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("The length of music history should a positive number higher than zero");
        }

        if (length > GlobalSetting.MUSIC_HISTORY_MAX_LENGTH) {
            throw new IllegalArgumentException("The length of music history should not exceed the limit allowed (" +
                    GlobalSetting.MUSIC_HISTORY_MAX_LENGTH
                    + ")");
        }
        this.length = length;
    }

    public void updateFromServer() {
        // The server part is currently not implemented so  it's currently faked with example musics
        musicHistoryList.clear();
        musicHistoryList.add(new Music("Rihanna", "What's my name"));
        musicHistoryList.add(new Music("Rihanna", "Rude boy"));
        musicHistoryList.add(new Music("Rihanna", "Umbrella"));
        musicHistoryList.add(new Music("Rihanna", "Don't stop the music"));
        musicHistoryList.add(new Music("Rihanna", "Russian roulette"));
        musicHistoryList.add(new Music("Rihanna", "We found love"));
        musicHistoryList.add(new Music("Rihanna", "Live your life"));
        musicHistoryList.add(new Music("Rihanna", "Love the way you lie"));
        musicHistoryList.add(new Music("Rihanna", "Take a bow"));
        musicHistoryList.add(new Music("Rihanna", "Disturbia"));
    }

    public List<Music> getHistory() {
        return musicHistoryList;
    }

}
