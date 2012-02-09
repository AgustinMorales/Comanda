package com.company.comanda.peter.server;

import java.util.List;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import com.company.comanda.peter.server.model.Restaurant;
import com.googlecode.objectify.Objectify;

public class RestaurantManagementImpl implements RestaurantManagement {

    private Objectify ofy;

    @Inject
    public RestaurantManagementImpl(Objectify ofy){
        this.ofy = ofy;
    }

    @Override
    public long login(String username, String password) 
            throws WrongUserNameOrPasswordException{
        List<Restaurant> restaurants = ofy.
                query(Restaurant.class).filter("name", username).list();
        int resultSize = restaurants.size();
        if(resultSize > 1){
            throw new IllegalStateException(
                    "More than one restaurant with name: " + username);
        }
        else if(resultSize == 0){
            throw new WrongUserNameOrPasswordException();
        }
        assert resultSize == 1;
        Restaurant restaurant = restaurants.get(0);
        
        if(BCrypt.checkpw(password, restaurant.getHashedPassword()) == 
                false){
            throw new WrongUserNameOrPasswordException();
        }

        return restaurant.getId();
    }

}
