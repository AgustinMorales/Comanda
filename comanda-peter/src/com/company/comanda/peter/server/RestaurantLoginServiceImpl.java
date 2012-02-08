package com.company.comanda.peter.server;

import com.company.comanda.peter.client.services.RestaurantLoginServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RestaurantLoginServiceImpl extends RemoteServiceServlet implements RestaurantLoginServiceAsync {

    /**
     * 
     */
    private static final long serialVersionUID = -7255781538823503521L;

    @Override
    public void login(String username, String password,
            AsyncCallback<Boolean> callback) {
        

    }

}
