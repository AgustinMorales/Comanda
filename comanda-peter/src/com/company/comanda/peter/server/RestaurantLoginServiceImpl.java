package com.company.comanda.peter.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.company.comanda.peter.client.services.RestaurantLoginServiceAsync;
import com.company.comanda.peter.shared.Constants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Singleton
public class RestaurantLoginServiceImpl extends RemoteServiceServlet implements RestaurantLoginServiceAsync {

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
    public void login(String username, String password,
            AsyncCallback<Boolean> callback) {
        long restaurantId = manager.login(username, password);
        
        RestaurantAgent agent = agentFactory.create(restaurantId);
        
        getThreadLocalRequest().getSession().
        setAttribute(Constants.RESTAURANT_AGENT, agent);

    }

}
