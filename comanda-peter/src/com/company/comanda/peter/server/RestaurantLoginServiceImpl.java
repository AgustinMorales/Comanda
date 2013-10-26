package com.company.comanda.peter.server;


import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.client.services.RestaurantLoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Singleton
public class RestaurantLoginServiceImpl extends RemoteServiceServlet implements RestaurantLoginService {

    private static final Logger log = 
            LoggerFactory.getLogger(RestaurantLoginServiceImpl.class);
    /**
     * 
     */
    private static final long serialVersionUID = -7255781538823503521L;

    private RestaurantManager manager;
    
    @Inject
    public RestaurantLoginServiceImpl(RestaurantManager manager){
        this.manager = manager;
    }
    
    @Override
    public String login(String login, String password) {
        return manager.login(login, password);
    }

}
