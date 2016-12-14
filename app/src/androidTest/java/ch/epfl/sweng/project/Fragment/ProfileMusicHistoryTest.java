package ch.epfl.sweng.project.Fragment;

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;
import android.test.ActivityInstrumentationTestCase2;

import com.facebook.FacebookSdk;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.project.Model.Music;

import static android.support.test.InstrumentationRegistry.getContext;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Antoine Merino on 29/11/2016.
 */



@RunWith(AndroidJUnit4.class)
public class ProfileMusicHistoryTest  {


    private MusicListAdapter testAdapter;
    private ArrayList<Music> musics;
    private ProfileFrag profileFragment;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Before
    public  void setUp() throws Exception {

        profileFragment = new ProfileFrag();
        musics = new ArrayList<>();
        musics.add(new Music("Artist 1", "Track 1"));
        musics.add(new Music("Artist 2", "Track 2"));
        testAdapter = new MusicListAdapter(musics, getContext());
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
