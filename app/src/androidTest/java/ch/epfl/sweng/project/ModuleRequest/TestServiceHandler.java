package ch.epfl.sweng.project.ModuleRequest;

import org.junit.Test;

import ch.epfl.sweng.project.ServerRequest.ServiceHandler;

/**
 * Created by adupeyrat on 17/12/2016.
 */

public class TestServiceHandler {

    @Test(expected = IllegalArgumentException.class)
    public void doPost (){
        ServiceHandler servicehandler = new ServiceHandler(null);
        servicehandler.doPost(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doPut (){
        ServiceHandler servicehandler = new ServiceHandler(null);
        servicehandler.doPut(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doDelete (){
        ServiceHandler servicehandler = new ServiceHandler(null);
        servicehandler.doDelete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doGet (){
        ServiceHandler servicehandler = new ServiceHandler(null);
        servicehandler.doGet(null, null);
    }

}
