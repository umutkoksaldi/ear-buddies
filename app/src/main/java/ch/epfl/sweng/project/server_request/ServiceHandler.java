package ch.epfl.sweng.project.server_request;

import android.util.Log;

import java.util.HashMap;


public class ServiceHandler {

    private final OnServerRequestComplete mListner;

    public ServiceHandler(OnServerRequestComplete alistener) {
        mListner = alistener;
    }

    private void doServerRequest(Object params, String url, int requestType, Class<Object> aClass) {
        try {
            new BackgroundRequest(params, url, requestType, mListner, aClass).execute();
        } catch (Exception e) {
            Log.w("doPost()", "error in computation");
        }
    }

    public void doPost(Object params, String url, Class aClazz) throws IllegalArgumentException {
        if (params == null || url == null) {
            Log.w("doPost()", "null parameters");
            throw new IllegalArgumentException("Null parameters");
        }
        //noinspection unchecked
        doServerRequest(params, url, SettingRequest.POST_REQUEST, aClazz);
    }

    @SuppressWarnings("unused")
    public void doGet(String url, Class aClazz) throws IllegalArgumentException {
        if (url == null) {
            Log.w("doGet()", "null parameters");
            throw new IllegalArgumentException("Null parameters");
        }
        Log.d("doGet()", "URL = " + url);
        //noinspection unchecked
        doServerRequest(new HashMap<String, String>(), url, SettingRequest.GET_REQUEST, aClazz);
    }

    @SuppressWarnings("unused")
    public void doDelete(String url) throws IllegalArgumentException {
        if (url == null) {
            Log.w("doDelete()", "null parameters");
            throw new IllegalArgumentException("Null parameters");
        }
        doServerRequest(new HashMap<String, String>(), url, SettingRequest.DELETE_REQUEST, Object.class);
    }

    @SuppressWarnings("unused")
    public void doPut(Object params, String url) throws IllegalArgumentException {
        if (params == null || url == null) {
            Log.w("doPut()", "null parameters");
            throw new IllegalArgumentException("Null parameters");
        }
        doServerRequest(params, url, SettingRequest.PUT_REQUEST, Object.class);
    }
}