package com.company.comanda.peter.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface RestaurantLoginService extends RemoteService {
    boolean login(String login, String password);
}
