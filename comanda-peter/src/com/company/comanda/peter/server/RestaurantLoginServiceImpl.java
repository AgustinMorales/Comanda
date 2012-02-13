package com.company.comanda.peter.server;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.client.services.RestaurantLoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Singleton
public class RestaurantLoginServiceImpl extends RemoteServiceServlet implements RestaurantLoginService {

    private static final Logger log = 
            Logger.getLogger(RestaurantLoginServiceImpl.class.getName());
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
    public boolean login(String username, String password) {
        return manager.login(username, password);
    }

}
