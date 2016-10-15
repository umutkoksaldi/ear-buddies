package ch.epfl.sweng.project.ServerRequest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;



class BackgroundRequest extends AsyncTask<String, Void , Object> {
    private final String mUrl;
    private final int mRequest_type;
    private final OnServerRequestComplete mListener;
    private final Class mClazz;
    private final Object mParams;


    /**
     *
     * @param aParams           contain the key value of the request.
     * @param url               destination url.
     * @param aRequest_type     type of the request.
     * @param aListener         the callback method.
     * @param aClass            class represent the type.
     */
    public BackgroundRequest(Object aParams,
                             String url,
                             int aRequest_type,
                             OnServerRequestComplete aListener,
                             Class aClass){
        this.mUrl = url;
        this.mRequest_type = aRequest_type;
        this.mListener = aListener;
        this.mClazz = aClass;
        this.mParams = aParams;
    }


    @SuppressWarnings("EmptyMethod")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(String... urls) {

        Log.d("doInBackground", "in Background, url = " + mUrl);
        Log.d("doInBackground", "in Background, class = " + mClazz);
        Log.d("doInBackground", "in Background, request Type = " + mRequest_type);

        // initialize the RestTemplate in order
        ResponseEntity<Object> response = null;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // do request in background.
        switch (mRequest_type) {

            // Do a post Request
            case SettingRequest.POST_REQUEST:
                try {
                    //noinspection unchecked
                    System.out.print(mParams.toString());
                    //noinspection unchecked
                    response = restTemplate.postForEntity(mUrl, mParams, mClazz);
                    Log.i("doInBackground()","the user does a post request");
                    break;
                }
                catch(Exception e) {
                    // The request doesn't work, so we call the fail method of the listener.
                    Log.e("Problem during post", e.getMessage(), e);
                }
                break;

            // Do a get request.
            case SettingRequest.GET_REQUEST:
                try {
                    //noinspection unchecked
                    response = restTemplate.getForEntity(mUrl, mClazz);
                    Log.i("doInBackground()","the user does a get request");
                    break;
                }
                catch(Exception e) {
                    // The request doesn't work, so we call the fail method of the listener
                    Log.e("Problem during get", e.getMessage(), e);
                }
                break;

            // Do a put request
            case SettingRequest.PUT_REQUEST:
                try {
                    restTemplate.put(mUrl, mParams);
                    Log.i("doInBackground()","call put service");
                    break;
                }
                catch(Exception e) {
                    // The request doesn't work, so we call the fail method of the listener
                    Log.e("Problem during PUT", e.getMessage(), e);
                }
                break;

            // Do a delete response
            case SettingRequest.DELETE_REQUEST:
                try {
                    restTemplate.delete(mUrl);
                    Log.i("doInBackground()","call delete service");
                }
                catch(Exception e) {
                    // The request doesn't work, so we call the fail method of the listener
                    Log.e("Problem during DELETE", e.getMessage(), e);
                }
                break;
        }
        // return the response of the server.
        return response;

    }

    /**
     * Call the callback method depending of the result of the request.
     * @param responseServer response of the server
     */
    @Override
    protected void onPostExecute(Object responseServer) {
        super.onPostExecute(responseServer);

        // If the responseServer is null, a exception have been raised before.
        if(responseServer == null){
            mListener.onFailed();
        }
        // In the case where the request has sent properly.
        else{
            mListener.onSucess((ResponseEntity) responseServer);
        }
    }

}