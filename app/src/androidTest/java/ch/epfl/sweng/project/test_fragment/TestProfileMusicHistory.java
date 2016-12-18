package ch.epfl.sweng.project.test_fragment;

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.project.models.Music;
import ch.epfl.sweng.project.view.adapter_view.MusicListAdapter;
import ch.epfl.sweng.project.view.fragment.ProfileFragment;

import static android.support.test.InstrumentationRegistry.getContext;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Antoine Merino on 29/11/2016.
 */



@RunWith(AndroidJUnit4.class)
public class TestProfileMusicHistory {


    private MusicListAdapter testAdapter;
    private ArrayList<Music> musics;
    private ProfileFragment profileFragment;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Before
    public  void setUp() throws Exception {

        profileFragment = new ProfileFragment();
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
