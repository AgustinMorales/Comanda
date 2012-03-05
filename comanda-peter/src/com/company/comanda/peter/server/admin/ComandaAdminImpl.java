package com.company.comanda.peter.server.admin;

import java.util.List;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.company.comanda.peter.server.model.Restaurant;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
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
    
    private ImagesService imagesService;
    
    @Inject
    public ComandaAdminImpl(Objectify ofy){
        this.ofy = ofy;
        imagesService = ImagesServiceFactory.getImagesService();
    }
    
    
    public long createRestaurant(String name, String login, 
            String password, String description, String imageBlob,
            double latitude, double longitude) {
        if(ofy.query(Restaurant.class).filter("login", login).
                list().size() > 0){
            throw new IllegalArgumentException("Duplicate login");
        }
        Restaurant restaurant = new Restaurant();
        
        restaurant.setName(name);
        restaurant.setLogin(login);
        restaurant.setDescription(description);
        if(imageBlob != null && imageBlob.length() > 0){
            restaurant.setImageUrl(
                    imagesService.getServingUrl(
                            new BlobKey(imageBlob)));
        }
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
    public long createRestaurant(String name, String login,
            String password, String address,
            String description, String imageBlob) {
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
        return createRestaurant(name, login, password, description, 
                imageBlob, latitude, longitude);
    }

}
