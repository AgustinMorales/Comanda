package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.server.model.User;
import com.company.comanda.peter.shared.OrderState;
import com.google.inject.assistedinject.Assisted;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

public class RestaurantAgentImpl implements RestaurantAgent {

    private static final Logger log = 
            Logger.getLogger(RestaurantAgentImpl.class.getName());
    private final Objectify ofy;
    private final long restaurantId;
    
    @Inject
    public RestaurantAgentImpl(Objectify ofy, @Assisted long restaurantId){
        this.ofy = ofy;
        this.restaurantId = restaurantId;
    }
    
    @Override
    public List<MenuItem> getMenuItems() {
        List<MenuItem> result = new ArrayList<MenuItem>();
        Iterable<MenuItem> menuItems = ofy.query(MenuItem.class).
                ancestor(new Key<Restaurant>(
                        Restaurant.class, restaurantId));
        for(MenuItem currentMenuItem : menuItems){
            result.add(currentMenuItem);
        }
        return result;
    }

    @Override
    public void addOrModifyMenuItem(Long itemId, String itemName,
            String description, String priceString, String imageBlobkey) {
        MenuItem item = null;
        Key<Restaurant> parentRestaurant = 
                new Key<Restaurant>(Restaurant.class, restaurantId);
        if(itemId != null ){
            item = ofy.get(
                    new Key<MenuItem>(
                            parentRestaurant,MenuItem.class, itemId));
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

    @Override
    public void deleteMenuItems(long[] keyIds) {
        List<Key<MenuItem>> itemsToDelete = 
                new ArrayList<Key<MenuItem>>(keyIds.length);
        for(long currentId : keyIds){
            itemsToDelete.add(new Key<MenuItem>(
                    new Key<Restaurant>(Restaurant.class, restaurantId), 
                    MenuItem.class,currentId));
        }
        ofy.delete(itemsToDelete);

    }

    @Override
    public List<Order> getOrders(OrderState state, String tableName) {
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

    @Override
    public void changeOrderState( 
            long orderId, OrderState newState) {
        List<Order> orders = ofy.query(Order.class).
                filter("id", orderId).list();
        final int size = orders.size();
        if(size > 1){
            throw new IllegalStateException(
                    "More than one order with id: " + orderId);
        }
        if(size==0){
            String errorMsg = String.format(
                    "Could not order with ID: %s",orderId);
            log.warning(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        
        Order order = orders.get(0);
        order.setState(newState);
        ofy.put(order);
    }

    @Override
    public Table getTable(Key<Table> tableKey) {
        return ofy.get(tableKey);
    }

    @Override
    public MenuItem getMenuItem(Key<MenuItem> menuItemKey) {
        return ofy.get(menuItemKey);
    }

}
