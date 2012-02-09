package com.company.comanda.peter.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.client.services.RestaurantLoginService;
import com.company.comanda.peter.server.RestaurantManagement.WrongUserNameOrPasswordException;
import com.company.comanda.peter.shared.Constants;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Singleton
public class RestaurantLoginServiceImpl extends RemoteServiceServlet implements RestaurantLoginService {

    private static final Logger log = 
            Logger.getLogger(RestaurantLoginServiceImpl.class.getName());
    /**
     * 
     */
    private static final long serialVersionUID = -7255781538823503521L;

    private RestaurantManagement manager;
    private RestaurantAgentFactory agentFactory;
    
    @Inject
    public RestaurantLoginServiceImpl(RestaurantManagement manager,
            RestaurantAgentFactory agentFactory){
        this.manager = manager;
        this.agentFactory = agentFactory;
    }
    
    @Override
    public boolean login(String username, String password) {
        boolean result = false;
        try{
            long restaurantId = manager.login(username, password);
            
            RestaurantAgent agent = agentFactory.create(restaurantId);
            
            getThreadLocalRequest().getSession().
            setAttribute(Constants.RESTAURANT_AGENT, agent);
            result = true;
        }
        catch(WrongUserNameOrPasswordException e){
            log.log(Level.INFO, "Restaurant login failed",e);
        }
        return result;
    }

}
