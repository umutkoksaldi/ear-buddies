//package ch.epfl.sweng.project.test_module_request;
//
//
//import android.support.test.InstrumentationRegistry;
//import android.support.test.rule.ActivityTestRule;
//import android.test.ActivityInstrumentationTestCase2;
//
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import org.junit.Rule;
//import org.junit.Test;
//
//import java.util.List;
//
//import ch.epfl.sweng.project.util_constant.GlobalTestSettings;
//import ch.epfl.sweng.project.model.User;
//import ch.epfl.sweng.project.view.activity.MainActivity;
//import ch.epfl.sweng.project.model.ModelApplication;
//
//public class TestSendLocation extends ActivityInstrumentationTestCase2<MainActivity> {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
//            MainActivity.class);
//
//    public TestSendLocation() {
//        super(MainActivity.class);
//    }
//
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        injectInsrumentation(InstrumentationRegistry.getInstrumentation());
//        GlobalTestSettings.createMockUser();
//        ModelApplication.getModelApplication().setTest();
//    }
//
//
//    @Test
//    public void testMarker() throws InterruptedException {
//        Thread.sleep(5000);
//        User[] others = ModelApplication.getModelApplication().getOtherUsers();
//        List<MarkerOptions> markerOpt = ModelApplication.getModelApplication().getMarkerOpt();
//        assertEquals("Shoud have the same number of marker as user", others.length, markerOpt.size());
//        MarkerOptions m = markerOpt.get(0);
//        assertNotNull("Marker should not be null", m);
//    }
//}
