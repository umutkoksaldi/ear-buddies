package ch.epfl.sweng.project.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.test.ActivityInstrumentationTestCase2;

import com.facebook.FacebookSdk;

import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.project.ActivityForFragmentsTest;
import ch.epfl.sweng.project.Model.Music;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by Antoine Merino on 29/11/2016.
 */

public class ProfileMusicHistoryTest extends ActivityInstrumentationTestCase2<ActivityForFragmentsTest> {

    private ActivityForFragmentsTest myFragmentActivity;
    private MusicListAdapter testAdapter;
    private ArrayList<Music> musics;
    private ProfileFrag profileFragment;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ProfileMusicHistoryTest() {
        super(ActivityForFragmentsTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        myFragmentActivity = getActivity();
        FacebookSdk.sdkInitialize(myFragmentActivity.getApplicationContext());
        profileFragment = new ProfileFrag();

        musics = new ArrayList<>();
        musics.add(new Music("Artist 1", "Track 1"));
        musics.add(new Music("Artist 2", "Track 2"));

        testAdapter = new MusicListAdapter(musics, getContext());
    }

    @Test
    public void testPreConditions() {
        assertNotNull(myFragmentActivity);
        assertNotNull(profileFragment);
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
