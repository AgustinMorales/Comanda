package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.inject.Inject;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.OrderState;
import com.google.inject.assistedinject.Assisted;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

public class RestaurantAgentImpl implements RestaurantAgent {

    private static final Logger log = 
            Logger.getLogger(RestaurantAgentImpl.class.getName());
    private final Objectify ofy;
    private final Key<Restaurant> restaurantKey;
    private final Random random;
    
    @Inject
    public RestaurantAgentImpl(Objectify ofy, @Assisted long restaurantId){
        this.ofy = ofy;
        this.random = new Random();
        this.restaurantKey = new Key<Restaurant>(Restaurant.class,
                restaurantId);
    }
    
    @Override
    public List<MenuItem> getMenuItems() {
        List<MenuItem> result = new ArrayList<MenuItem>();
        Iterable<MenuItem> menuItems = ofy.query(MenuItem.class).
                ancestor(restaurantKey);
        for(MenuItem currentMenuItem : menuItems){
            result.add(currentMenuItem);
        }
        return result;
    }

    @Override
    public void addOrModifyMenuItem(Long itemId, String itemName,
            String description, String priceString, String imageBlobkey) {
        MenuItem item = null;
        if(itemId != null ){
            item = ofy.get(
                    new Key<MenuItem>(
                            restaurantKey,MenuItem.class, itemId));
        }
        else{
            if(itemName == null ||
                    priceString == null ||
                    imageBlobkey == null ||
                    description == null){
                throw new IllegalArgumentException("Missing data");
            }
            item = new MenuItem();
            item.setParent(restaurantKey);
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
                    restaurantKey, 
                    MenuItem.class,currentId));
        }
        ofy.delete(itemsToDelete);

    }

    @Override
    public List<Order> getOrders(OrderState state, Long tableId) {
        Query<Order> query = ofy.query(Order.class).order("-date");
        List<Order> orders = null;
        if (state != null){
            query.filter("state", state);
        }
        if(tableId != null){
            query.filter("table", new Key<Table>(
                    restaurantKey,Table.class,(long)tableId));
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

    @Override
    public long addTable(String name) {
        Table table = new Table();
        table.setName(name);
        table.setRestaurant(restaurantKey);
        ofy.put(table);
        final long id = table.getId();
        final int random_part = random.nextInt(
                Table.TABLE_CODE_RANDOM_PART_MAX_VALUE);
        String code = String.format(
                "%0" + Table.TABLE_CODE_ID_PART_WIDTH + "d" +
                "%0" + Table.TABLE_CODE_RANDOM_PART_WIDTH + "d", 
                id, random_part);
        log.info("Setting table code to: " + code);
        table.setCode(code);
        ofy.put(table);
        return id;
    }

    @Override
    public List<Table> getTables() {
        return ofy.query(Table.class).ancestor(restaurantKey).list();
    }

    @Override
    public List<Order> getOrders(OrderState state, String tableName) {
        Long tableId = null;
        if(tableName != null){
            List<Table> tables = ofy.query(Table.class).filter("name", 
                    tableName).ancestor(restaurantKey).list();
            if(tables.size() == 1){
                tableId = tables.get(0).getId();
            }
            else if( tables.size() > 1){
                throw new IllegalStateException(
                        "More than one table with the same name");
            }
        }
        return getOrders(state, tableId);
    }

}
