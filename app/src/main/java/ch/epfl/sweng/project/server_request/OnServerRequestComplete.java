package ch.epfl.sweng.project.server_request;

import org.springframework.http.ResponseEntity;


public interface OnServerRequestComplete {

    /**
     * The method is going to be called if the request succeed.
     *
     * @param responseServer is the object containing the response of the server.
     */
    void onSucess(ResponseEntity responseServer);


    /**
     * The Method is going to be called if the request fail.
     */
    void onFailed();

}
