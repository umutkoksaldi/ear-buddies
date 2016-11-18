package ch.epfl.sweng.project.media;

import org.junit.Test;

import Util.GlobalSetting;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by Antoine Merino on 18/11/2016.
 */

public class MusicHistoryTest {

    MusicHistory musicHistory = MusicHistory.getMusicHistory();

    @Test
    public void testSingletonInitialization() {
        boolean initialized = false;
        if (musicHistory != null) {
            initialized = true;
        }
        assertThat(initialized, is(true));
    }

    @Test
    public void historyLengthHigherThanZero() {
        boolean behavesAsExpected = false;
        try {
            musicHistory.setLength(0);
        } catch (IllegalArgumentException e) {
            // It's expected, we don't want to put a zero length for the history
            behavesAsExpected = true;
        }
        assertThat(behavesAsExpected, is(true));
    }

    @Test
    public void historyLengthShouldBePositive() {
        boolean behavesAsExpected = false;
        try {
            musicHistory.setLength(-5);
        } catch (IllegalArgumentException e) {
            // It's expected, we don't want to put a negative length for the history
            behavesAsExpected = true;
        }
        assertThat(behavesAsExpected, is(true));
    }

    @Test
    public void historyLengthShouldNotExceedMaxLength() {
        boolean behavesAsExpected = false;
        try {
            musicHistory.setLength(GlobalSetting.MUSIC_HISTORY_MAX_LENGTH + 1);
        } catch (IllegalArgumentException e) {
            // It's expected, we don't want to put the music history length above the limit
            behavesAsExpected = true;
        }
        assertThat(behavesAsExpected, is(true));
    }

}
