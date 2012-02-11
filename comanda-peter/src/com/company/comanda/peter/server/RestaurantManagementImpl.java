package com.company.comanda.peter.server;

import java.util.List;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.shared.Constants;
import com.googlecode.objectify.Objectify;

public class RestaurantManagementImpl implements RestaurantManager {

    private Objectify ofy;
    private SessionAttributesFactory attributesFactory;
    private RestaurantAgentFactory agentFactory;

    @Inject
    public RestaurantManagementImpl(Objectify ofy, 
            SessionAttributesFactory attributesFactory,
            RestaurantAgentFactory agentFactory){
        this.ofy = ofy;
        this.attributesFactory = attributesFactory;
        this.agentFactory = agentFactory;
    }

    @Override
    public boolean login(String username, String password){
        if(attributesFactory.create().getAttribute(Constants.RESTAURANT_AGENT) != null){
            throw new IllegalStateException("Already logged in");
        }
        boolean result = false;
        List<Restaurant> restaurants = ofy.
                query(Restaurant.class).filter("name", username).list();
        int resultSize = restaurants.size();
        if(resultSize > 1){
            throw new IllegalStateException(
                    "More than one restaurant with name: " + username);
        }
        else if (resultSize == 1){
            Restaurant restaurant = restaurants.get(0);
            if(BCrypt.checkpw(password, restaurant.getHashedPassword())){
                result = true;
                attributesFactory.create().setAttribute(
                        Constants.RESTAURANT_AGENT, 
                        agentFactory.create(restaurant.getId()));
            }
        }
        return result;
    }

    public RestaurantAgent getAgent(){
        return (RestaurantAgent)attributesFactory.create().
                getAttribute(Constants.RESTAURANT_AGENT);
    }
}
