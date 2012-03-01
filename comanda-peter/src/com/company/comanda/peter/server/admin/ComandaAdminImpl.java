package com.company.comanda.peter.server.admin;

import java.util.List;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.company.comanda.peter.server.model.Restaurant;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.googlecode.objectify.Objectify;

public class ComandaAdminImpl implements ComandaAdmin {

    private static final Logger log = 
            LoggerFactory.getLogger(ComandaAdminImpl.class);
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

    @Override
    public long createRestaurant(String name, String password, String address) {
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("es").getGeocoderRequest();
        GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
        if(geocoderResponse.getResults().size() == 0){
            throw new IllegalArgumentException("Could not geocode address");
        }
        GeocoderResult result = geocoderResponse.getResults().get(0);
        double latitude = result.getGeometry().
                getLocation().getLat().doubleValue();
        double longitude = result.getGeometry().
                getLocation().getLng().doubleValue();
        log.info("Address geocoded: {} -> lat='{}', long='{}'",
                new Object[]{address, latitude, longitude});
        return createRestaurant(name, password, latitude, longitude);
    }

}
