package com.company.comanda.peter.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RestaurantLoginServiceAsync {
    void login(String login, String password, 
            AsyncCallback<String> callback);
    
}
