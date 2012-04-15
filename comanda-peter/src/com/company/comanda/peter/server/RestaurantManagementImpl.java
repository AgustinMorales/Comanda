package com.company.comanda.peter.server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import com.company.comanda.peter.server.model.LoginToken;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.shared.Constants;
import com.googlecode.objectify.Objectify;

public class RestaurantManagementImpl implements RestaurantManager {

    private Objectify ofy;
    private SessionAttributesFactory attributesFactory;
    private RestaurantAgentFactory agentFactory;
    private SecureRandom random;

    @Inject
    public RestaurantManagementImpl(Objectify ofy, 
            SessionAttributesFactory attributesFactory,
            RestaurantAgentFactory agentFactory){
        this.ofy = ofy;
        this.attributesFactory = attributesFactory;
        this.agentFactory = agentFactory;
        this.random = new SecureRandom();
    }

    @Override
    public String login(String login, String password){
        if(attributesFactory.create().getAttribute(Constants.RESTAURANT_ID) != null){
            throw new IllegalStateException("Already logged in");
        }
        String result = null;
        Restaurant restaurant = doLogin(login, password);
        if(restaurant != null){
            String tokenString = new BigInteger(130, random).toString(32);
        	List<LoginToken> tokens = ofy.query(LoginToken.class).filter("login", login).list();
        	LoginToken token = null;
        	if(tokens.size() == 1){
        	    token = tokens.get(0);
        	}
        	else{
        	    token = new LoginToken();
        	    token.setLogin(login);
        	}
        	token.setToken(tokenString);
        	ofy.put(token);
        	result = tokenString;
            validateLogin(restaurant);
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
            result = getAgent(restaurantId);
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

    @Override
    public String login(String tokenString) {
        if(attributesFactory.create().getAttribute(Constants.RESTAURANT_ID) != null){
            throw new IllegalStateException("Already logged in");
        }
        String result = null;
        List<LoginToken> tokens = 
                ofy.query(LoginToken.class).filter("token", tokenString).list();
        if(tokens.size() == 1){
            LoginToken token = tokens.get(0);
            String username = token.getLogin();
            List<Restaurant> restaurants = 
                    ofy.query(Restaurant.class).filter("login", username).list();
            if(restaurants.size() == 1){
                Restaurant restaurant = restaurants.get(0);
                validateLogin(restaurant);
                result = restaurant.getLogin();
            }
        }
        return result;
    }
    
    private void validateLogin(Restaurant restaurant){
        attributesFactory.create().setAttribute(
                Constants.RESTAURANT_ID,restaurant.getId());
    }
}
