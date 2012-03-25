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
    public boolean login(String login, String password){
        if(attributesFactory.create().getAttribute(Constants.RESTAURANT_ID) != null){
            throw new IllegalStateException("Already logged in");
        }
        boolean result = false;
        Restaurant restaurant = doLogin(login, password);
        if(restaurant != null){
        	result = true;
            attributesFactory.create().setAttribute(
                    Constants.RESTAURANT_ID,restaurant.getId());
        }
        //FIXME: Should be deleted when we can add categories from the interface
        RestaurantAgent agent = getAgent();
        if(agent != null){
            if(agent.getCategories().size() == 0){
                agent.addOrModifyMenuCategory(null, "Entrantes");
                agent.addOrModifyMenuCategory(null, "Principales");
                agent.addOrModifyMenuCategory(null, "Bebidas");
                agent.addOrModifyMenuCategory(null, "Postres");
            }
        }
        return result;
    }

    public RestaurantAgent getAgent(){
        RestaurantAgent result = null;
        Long restaurantId = (Long)attributesFactory.create().
                getAttribute(Constants.RESTAURANT_ID);
        if(restaurantId != null){
            getAgent(restaurantId);
        }
        return result;
    }

    private RestaurantAgent getAgent(long restaurantId){
    	return agentFactory.create(restaurantId);
    }
    
    private Restaurant doLogin(String username, String password){
    	Restaurant result = null;
    	List<Restaurant> restaurants = ofy.
                query(Restaurant.class).filter("login", username).list();
        int resultSize = restaurants.size();
        if(resultSize > 1){
            throw new IllegalStateException(
                    "More than one restaurant with login: " + username);
        }
        else if (resultSize == 1){
            Restaurant restaurant = restaurants.get(0);
            if(BCrypt.checkpw(password, restaurant.getHashedPassword())){
                result = restaurant;
            }
        }
        return result;
    }
    
	@Override
	public RestaurantAgent getAgent(String login, String password) {
		Restaurant restaurant = doLogin(login, password);
		RestaurantAgent result = null;
		if(restaurant != null){
			result = getAgent(restaurant.getId());
		}
		return result;
	}
}
