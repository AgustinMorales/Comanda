package com.company.comanda.peter.server.admin;

import java.util.List;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.company.comanda.peter.server.model.Restaurant;
import com.googlecode.objectify.Objectify;

public class ComandaAdminImpl implements ComandaAdmin {

    private Objectify ofy;
    
    @Inject
    public ComandaAdminImpl(Objectify ofy){
        this.ofy = ofy;
    }
    
    @Override
    public long createRestaurant(String name, String password,
            double latitude, double longitude) {
        Restaurant restaurant = new Restaurant();
        
        restaurant.setName(name);
        String hashedPassword = BCrypt.hashpw(password, 
                BCrypt.gensalt());
        
        restaurant.setHashedPassword(hashedPassword);
        
        Point point = new Point(latitude, longitude);
        
        List<String> cells = GeocellManager.generateGeoCell(point);
        
        restaurant.setLatitude(latitude);
        restaurant.setLongitude(longitude);
        restaurant.setGeoCells(cells);
        
        ofy.put(restaurant);
        
        return restaurant.getId();

    }

}
