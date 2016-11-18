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


}
