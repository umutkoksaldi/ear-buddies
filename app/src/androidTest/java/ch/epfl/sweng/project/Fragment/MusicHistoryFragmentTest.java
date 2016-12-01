package ch.epfl.sweng.project.Fragment;

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.project.ActivityForFragmentsTest;
import ch.epfl.sweng.project.Model.Music;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by Antoine Merino on 29/11/2016.
 */

public class MusicHistoryFragmentTest extends ActivityInstrumentationTestCase2<ActivityForFragmentsTest> {

    private ActivityForFragmentsTest myFragmentActivity;
    private MusicHistoryFragment myFragment;
    private MusicHistoryFragment.MusicListAdapter testAdapter;
    private ArrayList<Music> musics;

    public MusicHistoryFragmentTest() {
        super(ActivityForFragmentsTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFragmentActivity = getActivity();
        myFragment = new MusicHistoryFragment();

        musics = new ArrayList<>();
        musics.add(new Music("Artist 1", "Track 1"));
        musics.add(new Music("Artist 2", "Track 2"));

        testAdapter = new MusicHistoryFragment.MusicListAdapter(musics, getContext());
    }

    @Test
    public void testPreConditions() {
        assertNotNull(myFragmentActivity);
        assertNotNull(myFragment);
        assertNotNull(testAdapter);
    }

    @Test
    public void testAdapterArtistAndSongName() {
        assertEquals("Artist names should match", musics.get(0).getArtist(), testAdapter.getSongByPosition(0)
                .getArtist());
        assertEquals("Artist names should match", musics.get(0).getName(), testAdapter.getSongByPosition(0).getName());
        assertEquals("Artist names should match", musics.get(1).getArtist(), testAdapter.getSongByPosition(1)
                .getArtist());
        assertEquals("Artist names should match", musics.get(1).getName(), testAdapter.getSongByPosition(1).getName());
    }
}
