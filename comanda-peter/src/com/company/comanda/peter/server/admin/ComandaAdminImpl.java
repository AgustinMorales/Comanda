package com.company.comanda.peter.server.admin;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import com.company.comanda.peter.server.model.Restaurant;
import com.googlecode.objectify.Objectify;

public class ComandaAdminImpl implements ComandaAdmin {

    private Objectify ofy;
    
    @Inject
    public ComandaAdminImpl(Objectify ofy){
        this.ofy = ofy;
    }
    
    @Override
    public long createRestaurant(String name, String password) {
        Restaurant restaurant = new Restaurant();
        
        restaurant.setName(name);
        String hashedPassword = BCrypt.hashpw(password, 
                BCrypt.gensalt());
        
        restaurant.setHashedPassword(hashedPassword);
        
        ofy.put(restaurant);
        
        return restaurant.getId();

    }

}
