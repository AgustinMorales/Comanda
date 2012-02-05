package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.shared.OrderState;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class ItemsManager {

    private static final Logger log = Logger.getLogger(ItemsManager.class.getName());

    private Objectify ofy = ObjectifyService.begin();

    private static ItemsManager instance;


    public static final ItemsManager me(){
        if (instance == null){
            instance = new ItemsManager();
        }
        return instance;
    }

    private ItemsManager(){
        ObjectifyService.register(Restaurant.class);
        ObjectifyService.register(MenuItem.class);
        ObjectifyService.register(Order.class);
    }


    public List<MenuItem> getMenuItems(long restaurantId){
        List<MenuItem> result = new ArrayList<MenuItem>();
        Iterable<MenuItem> menuItems = ofy.query(MenuItem.class).
                ancestor(new Key<Restaurant>(
                        Restaurant.class, restaurantId));
        for(MenuItem currentMenuItem : menuItems){
            result.add(currentMenuItem);
        }
        return result;
    }

    public void placeOrder(long restaurantId,long keyId, String table){

        MenuItem menuItem = ofy.get(
                new Key<MenuItem>(new Key<Restaurant>(
                        Restaurant.class, restaurantId), 
                        MenuItem.class, keyId));
        if(menuItem == null){
            String errorMsg = String.format("Could not place order for item ID: %s",keyId);
            log.warning(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        Order newOrder = new Order(menuItem.getName(), table, new Date(), OrderState.ORDERED);
        ofy.put(newOrder);
    }

    public void modifyOrder(long keyId, OrderState newState){
        Order order = ofy.get(new Key<Order>(Order.class, keyId));
        if(order == null){
            String errorMsg = String.format("Could not order with ID: %s",keyId);
            log.warning(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        order.setState(newState);
        ofy.put(order);
    }

    public void deleteMenuItems(long restaurantId, long[] keyIds){
        List<Key<MenuItem>> itemsToDelete = new ArrayList<Key<MenuItem>>(keyIds.length);
        for(long currentId : keyIds){
            itemsToDelete.add(new Key<MenuItem>(
                    new Key<Restaurant>(Restaurant.class, restaurantId), 
                    MenuItem.class,currentId));
        }
        ofy.delete(itemsToDelete);
    }

    public Long addRestaurant(){
        Restaurant restaurant = new Restaurant();
        ofy.put(restaurant);
        return restaurant.getId();
    }

    public void addOrModifyMenuItem(Long itemId,
            String itemName,
            String description,
            String priceString,
            String imageBlobkey,
            Long restaurantId){
        MenuItem item = null;
        Key<Restaurant> parentRestaurant = new Key<Restaurant>(Restaurant.class, restaurantId);
        if(itemId != null ){
            item = ofy.get(new Key<MenuItem>(parentRestaurant,MenuItem.class, itemId));
        }
        else{
            if(itemName == null ||
                    priceString == null ||
                    imageBlobkey == null ||
                    description == null){
                throw new IllegalArgumentException("Missing data");
            }
            item = new MenuItem();
            item.setParent(parentRestaurant);
        }
        if(itemName != null){
            item.setName(itemName);
        }
        if(priceString != null){
            item.setPrice(Integer.parseInt(priceString));
        }
        if(description != null){
            item.setDescription(description);
        }
        if(imageBlobkey != null){
            item.setImageString(imageBlobkey);
        }
        //persist
        ofy.put(item);
    }

    public List<Order> getOrders(
            OrderState state, String tableName){
        Query<Order> query = ofy.query(Order.class).order("-date");
        List<Order> orders = null;
        if (state != null){
            query.filter("state", state);
        }
        if(tableName != null){
            query.filter("table", tableName);
        }
        orders = query.list();
        return orders;
    }
}
