package com.company.comanda.peter.server;

import java.util.List;

import javax.inject.Inject;

import com.company.comanda.peter.server.model.Restaurant;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

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

        return 0;
    }

}
