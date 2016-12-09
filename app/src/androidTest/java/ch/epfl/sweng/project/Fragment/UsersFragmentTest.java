package ch.epfl.sweng.project.ModuleRequest;


import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.project.ActivityForFragmentsTest;
import ch.epfl.sweng.project.Fragment.UsersFragment;
import ch.epfl.sweng.project.MainActivity;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by Dusan Viktor on 2016-11-04.
 * Using AcivityForFragments to be the activity that is testing the Blank Fragment
 *
 */

public class UsersFragmentTest extends ActivityInstrumentationTestCase2<ActivityForFragmentsTest> {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    private ActivityForFragmentsTest myFragmentActivity;
    private UsersFragment myFragment;
    private UsersFragment.VivzAdapter testAdapter;
    private String[] names;
    private String[] desc;
    private String[] imgs;

    public UsersFragmentTest() {
        super(ActivityForFragmentsTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFragmentActivity = getActivity();
        myFragment = new UsersFragment();

        names = new String[2];
        desc = new String[2];
        imgs = new String[2];


        names[0] = "Name1";
        desc[0] = "Song1"  ;
        imgs[0] = "URL-Picture1";

        names[1] = "Namew";
        desc[1] = "Song2";
        imgs[1] = "URL-Picture2";

        testAdapter = new UsersFragment.VivzAdapter(getContext(),names, imgs, desc);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(myFragmentActivity);
        assertNotNull(myFragment);
        assertNotNull(testAdapter);
    }

    @Test
    public void testAdapterNameandSongDesc(){
        /*assertEquals("Names should match", names[0],testAdapter.getNameByPosition(0));
        assertEquals("Song names should match", desc[0],testAdapter.getSongByPosition(0));
        assertEquals("Pictures should match", imgs[0],testAdapter.getImageByPosition(0));

        assertEquals("Names should match", names[1],testAdapter.getNameByPosition(1));
        assertEquals("Song names should match", desc[1],testAdapter.getSongByPosition(1));
        assertEquals("Pictures should match", imgs[1],testAdapter.getImageByPosition(1));
    */}

    @Test
    public void testForEmptyProfile(){
        assertEquals("Should be empty String", "",testAdapter.getImageByPosition(2));
        assertEquals("Should be empty String", "",testAdapter.getNameByPosition(2));
        assertEquals("Should be empty String", "",testAdapter.getSongByPosition(2));
    }

}