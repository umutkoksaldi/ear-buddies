package ch.epfl.sweng.project.test_module_request;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.project.server_request.OnServerRequestComplete;
import ch.epfl.sweng.project.server_request.ServiceHandler;
import ch.epfl.sweng.project.util_constant.GlobalSetting;
import ch.epfl.sweng.project.view.activity.MainActivity;

import static ch.epfl.sweng.project.util_constant.GlobalTestSettings.MEDIUM_REQUEST_DELAY;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestDeleteAndPut extends ActivityInstrumentationTestCase2<MainActivity> {
    private final String NEW_NAME = "Tester";
    private final String ID = "121620614972695";
    private ServiceHandler serviceHandler;
    public TestDeleteAndPut() {
        super(MainActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInsrumentation(InstrumentationRegistry.getInstrumentation());
        serviceHandler = new ServiceHandler(new OnServerRequestComplete() {

            @Override
            public void onSucess(ResponseEntity response) {

                // We associated the user to the new.
                assertEquals("Should answer with a GoodAnswer", GlobalSetting.GOOD_ANSWER, Integer.parseInt
                        (response.getStatusCode()
                                .toString()));
            }

            @Override
            public void onFailed() {
                assertTrue("Error in request", false);
            }
        });
    }

    @Test
    public void putNewName() throws InterruptedException {


        Map<String, String> params = new HashMap<>();
        params.put("firstname", NEW_NAME);
        params.put("lastname", "");

        serviceHandler.doPut(params, GlobalSetting.URL + GlobalSetting.USER_API + ID);
        Thread.sleep(MEDIUM_REQUEST_DELAY);

    }

    @Test
    public void deleteUser() throws InterruptedException {
        serviceHandler.doDelete(GlobalSetting.URL + GlobalSetting.USER_API + ID);
        Thread.sleep(MEDIUM_REQUEST_DELAY);
    }

}
