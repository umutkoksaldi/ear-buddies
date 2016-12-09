package ch.epfl.sweng.project;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;


import ch.epfl.sweng.project.Fragment.PresentationAppFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.app.FragmentManager;
import android.util.Log;

public class TestConnexionFragment extends ActivityInstrumentationTestCase2<Login> {

    private FragmentManager fragmentManager;

    public TestConnexionFragment() {
        super(Login.class);
    }

    @Override
    public void setUp(){
        getActivity();
        injectInsrumentation(InstrumentationRegistry.getInstrumentation());
        fragmentManager = getActivity().getFragmentManager();
    }

//    public void testPerformClick() {
//
//        try {
//            Thread.sleep(1000);
//            PresentationAppFragment presentationFragmentById = (PresentationAppFragment) fragmentManager.findFragmentById
//                    (R.id.containFragmentPresentation);
//            assertEquals("drawableId should be equals", presentationFragmentById.getDrawableId(), R.mipmap.music_image);
//            assertEquals("drawableId should be equals", presentationFragmentById.getPresentationTextId(),
//                    R.string.presentation_music);
//            assertEquals("drawableId should be equals", presentationFragmentById.getStateLocation(), 1);
//
//            onView(withId(R.id.containFragmentPresentation)).perform(click());
//
//            Thread.sleep(1000);
//            presentationFragmentById = (PresentationAppFragment) fragmentManager.findFragmentById
//                    (R.id.containFragmentPresentation);
//            assertEquals("drawableId should be equals", presentationFragmentById.getDrawableId(), R.mipmap.people_image);
//            assertEquals("drawableId should be equals", presentationFragmentById.getPresentationTextId(),
//                    R.string.presentation_people);
//            assertEquals("drawableId should be equals", presentationFragmentById.getStateLocation(), 2);
//        }
//        catch (Exception e){
//            Log.e("testPerformClick()",e.getMessage());
//            assertFalse(true);
//        }
//    }


    public void testperformClicks() {
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
