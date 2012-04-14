package com.company.comanda.peter.server.admin;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.junit.experimental.categories.Categories;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Restaurant;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.googlecode.objectify.Key;
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


    public long createOrModifyRestaurant(String restaurantKeyString,
            String name, String login, 
            String password, String description, String imageBlob,
            String phone,
            String address,
            double latitude, double longitude,
            float deliveryCost,
            float minimumForDelivery,
            double maxDeliveryDistance,
            String copyMenuItemsFromRestKeyString) {
        Restaurant restaurant = null;
        if(restaurantKeyString != null){
            restaurant = ofy.get(new Key<Restaurant>(restaurantKeyString));
            if(restaurant == null){
                throw new IllegalArgumentException("Restaurant not found. KeyString: " +
                        restaurantKeyString);
            }
        }
        else{
            if(ofy.query(Restaurant.class).filter("login", login).
                    list().size() > 0){
                throw new IllegalArgumentException("Duplicate login");
            }
            restaurant = new Restaurant();
            if(password == null){
                throw new IllegalArgumentException("Password must not be null");
            }
        }
        restaurant.setName(name);
        restaurant.setLogin(login);
        restaurant.setDescription(description);
        try{
            if(imageBlob != null && imageBlob.length() > 0){
                restaurant.setImageUrl(
                        imagesService.getServingUrl(
                                new BlobKey(imageBlob)));
            }
        }
        catch(IllegalArgumentException e){
            log.info("Looks like we have no image, leaving to null");
        }
        if(password != null){
            String hashedPassword = BCrypt.hashpw(password, 
                    BCrypt.gensalt());

            restaurant.setHashedPassword(hashedPassword);
        }
        Point point = new Point(latitude, longitude);

        List<String> cells = GeocellManager.generateGeoCell(point);

        restaurant.setLatitude(latitude);
        restaurant.setLongitude(longitude);
        restaurant.setGeoCells(cells);
        restaurant.setPhone(phone);
        restaurant.setAddress(address);
        restaurant.setDeliveryCost(deliveryCost);
        restaurant.setMaxDeliveryDistance(maxDeliveryDistance);
        restaurant.setMinimumForDelivery(minimumForDelivery);

        ofy.put(restaurant);

        if(copyMenuItemsFromRestKeyString != null){
            copyFromRestaurant(restaurant, copyMenuItemsFromRestKeyString);
        }
        return restaurant.getId();

    }

    @Override
    public long createOrModifyRestaurant(
            String restaurantKeyString, String name, String login,
            String password, String address,
            String description, String imageBlob,
            String phone,
            float deliveryCost,
            float minimumForDelivery,
            double maxDeliveryDistance,
            String copyMenuItemsFromRestKeyString) {
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
        return createOrModifyRestaurant(restaurantKeyString,
                name, login, password, description, 
                imageBlob, phone, address, latitude, longitude,
                deliveryCost,
                minimumForDelivery,
                maxDeliveryDistance,
                copyMenuItemsFromRestKeyString);
    }


    @Override
    public List<Restaurant> getRestaurants() {
        return ofy.query(Restaurant.class).list();
    }

    private void copyFromRestaurant(Restaurant to, String fromKeyString){
        Key<Restaurant> fromKey = new Key<Restaurant>(fromKeyString);
        Key<Restaurant> toKey = new Key<Restaurant>(Restaurant.class, to.getId());
        List<MenuCategory> categories = ofy.query(
                MenuCategory.class).ancestor(fromKey).list();
        List<MenuItem> menuItems = ofy.query(MenuItem.class).ancestor(fromKey).list();
        HashMap<Key<MenuCategory>, MenuCategory> categoriesMap = 
                new HashMap<Key<MenuCategory>, MenuCategory>(categories.size());
        if(categories.size() == 0){
            log.warn("Zero categories for source restaurant: {}", fromKeyString);
        }
        for(MenuCategory category : categories){
            Key<MenuCategory> key = new Key<MenuCategory>(fromKey, 
                    MenuCategory.class, category.getId());
            category.setId(null);
            category.setRestaurant(toKey);
            categoriesMap.put(key, category);
        }
        ofy.put(categories);
        for(MenuItem menuItem : menuItems){
            menuItem.setId(null);
            menuItem.setParent(toKey);
            MenuCategory newCategory = categoriesMap.get(menuItem.getCategory());
            Key<MenuCategory> categoryKey = 
                    new Key<MenuCategory>(toKey, MenuCategory.class, newCategory.getId());
            log.debug("Setting new category: {} for item: {}", 
                    categoryKey, menuItem.getName());
            menuItem.setCategory(categoryKey);
        }
        ofy.put(menuItems);

    }
}
