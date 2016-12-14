package ch.epfl.sweng.project.testUi;

import android.app.FragmentManager;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.project.Fragment.PresentationAppFragment;
import ch.epfl.sweng.project.Login;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.ModuleRequest.MockUserRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class TestConnexionFragment {

    @Rule
    public MockUserRule<Login> mActivityRule = new MockUserRule<>(Login.class);
    private FragmentManager fragmentManager;

    @Test
    public void testPerformClick() {

        try {
            Thread.sleep(1000);
            fragmentManager = mActivityRule.getActivity().getFragmentManager();
            PresentationAppFragment presentationFragmentById = (PresentationAppFragment) fragmentManager
                    .findFragmentById
                    (R.id.containFragmentPresentation);
            assertEquals("drawableId should be equals", presentationFragmentById.getDrawableId(), R.mipmap.music_image);
            assertEquals("drawableId should be equals", presentationFragmentById.getPresentationTextId(),
                    R.string.presentation_music);
            assertEquals("drawableId should be equals", presentationFragmentById.getStateLocation(), 1);

            onView(withId(R.id.containFragmentPresentation)).perform(click());

            Thread.sleep(1000);
            presentationFragmentById = (PresentationAppFragment) fragmentManager.findFragmentById
                    (R.id.containFragmentPresentation);
            assertEquals("drawableId should be equals", presentationFragmentById.getDrawableId(), R.mipmap.people_image);
            assertEquals("drawableId should be equals", presentationFragmentById.getPresentationTextId(),
                    R.string.presentation_people);
            assertEquals("drawableId should be equals", presentationFragmentById.getStateLocation(), 2);
        }
        catch (Exception e){
            Log.e("testPerformClick()",e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    public void testPerformClicks() {
        fragmentManager = mActivityRule.getActivity().getFragmentManager();
        try {
            onView(withId(R.id.containFragmentPresentation)).perform(click());
            onView(withId(R.id.containFragmentPresentation)).perform(click());
            onView(withId(R.id.containFragmentPresentation)).perform(click());
            onView(withId(R.id.containFragmentPresentation)).perform(click());
            onView(withId(R.id.containFragmentPresentation)).perform(click());

            Thread.sleep(1000);
            PresentationAppFragment presentationFragmentById = (PresentationAppFragment) fragmentManager.findFragmentById
                    (R.id.containFragmentPresentation);
            assertEquals("drawableId should be equals",presentationFragmentById.getDrawableId(), R.mipmap.interaction_image);
            assertEquals("drawableId should be equals", presentationFragmentById.getPresentationTextId(),
                    R.string.presentation_interaction);
            assertEquals("drawableId should be equals", presentationFragmentById.getStateLocation(),3);
        }
        catch (Exception e){
            Log.e("testPerformClick()",e.getMessage());
            assertFalse(true);
        }
    }


}
