package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.inject.assistedinject.Assisted;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

public class RestaurantAgentImpl implements RestaurantAgent {


    private static final String DELIVERY_TABLE_NAME = "Delivery";
    private static final Logger log = 
            LoggerFactory.getLogger(RestaurantAgentImpl.class);
    private final Objectify ofy;
    private final Key<Restaurant> restaurantKey;
    private final Random random;
    
    private ImagesService imagesService;

    private Long deliveryTableId;

    @Inject
    public RestaurantAgentImpl(Objectify ofy, @Assisted long restaurantId){
        this.ofy = ofy;
        this.random = new Random();
        this.restaurantKey = new Key<Restaurant>(Restaurant.class,
                restaurantId);
        imagesService = ImagesServiceFactory.getImagesService();

    }

    @Override
    public List<MenuItem> getMenuItems(Long categoryId) {
        List<MenuItem> result = new ArrayList<MenuItem>();
        Query<MenuItem> menuItems = ofy.query(MenuItem.class);
        if(categoryId != null){
            Key<MenuCategory> categoryKey = new Key<MenuCategory>(
                    restaurantKey, MenuCategory.class,(long)categoryId);
            menuItems.filter("category", categoryKey);
        }
        menuItems.ancestor(restaurantKey);
        for(MenuItem currentMenuItem : menuItems){
            result.add(currentMenuItem);
        }
        return result;
    }

    @Override
    public void addOrModifyMenuItem(Long itemId, String itemName,
            String description, String priceString, String imageBlobkey,
            Long categoryId) {
        MenuItem item = null;
        if(itemId != null ){
            item = ofy.get(
                    new Key<MenuItem>(
                            restaurantKey,MenuItem.class, itemId));
        }
        else{
            if(itemName == null ||
                    priceString == null ||
                    description == null ||
                    categoryId == null){
                throw new IllegalArgumentException("Missing data");
            }
            item = new MenuItem();
            item.setParent(restaurantKey);
        }
        if(itemName != null){
            item.setName(itemName);
        }
        if(priceString != null){
            item.setPrice(Float.parseFloat(priceString));
        }
        if(description != null){
            item.setDescription(description);
        }
        if(imageBlobkey != null){
            BlobKey blobKey = new BlobKey(imageBlobkey);
            String servingUrl = 
                    imagesService.getServingUrl(blobKey);
            item.setImageString(servingUrl);
        }
        else{
            item.setImageString("");
        }
        if(categoryId != null){
            item.setCategory(new Key<MenuCategory>(
                    restaurantKey,
                    MenuCategory.class,
                    categoryId));
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
    public List<Order> getOrders(BillType billType,
            OrderState state, String tableKeyString, String billKeyString) {
        Query<Order> query = ofy.query(Order.class).ancestor(restaurantKey).
                order("-date");
        List<Order> orders = null;
        if(billType != null){
            query.filter("billType", billType);
        }
        if (state != null){
            query.filter("state", state);
        }
        if(tableKeyString != null){
            query.filter("table",new Key<Table>(tableKeyString));
        }
        if(billKeyString != null){
            Key<Bill> billKey = new Key<Bill>(billKeyString);
            query.ancestor(billKey);
        }
        orders = query.list();
        return orders;
    }

    @Override
    public void changeOrderState( 
            String orderKeyString, OrderState newState) {
        Order order = ofy.get(new Key<Order>(orderKeyString));
        if(order == null){
            String errorMsg = String.format(
                    "Could not get order with keyString: %s",
                    orderKeyString);
            log.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        order.setState(newState);
        ofy.put(order);
    }

    @Override
    public Table getTable(Key<Table> tableKey) {
        return ofy.get(tableKey);
    }

    @Override
    public MenuItem getMenuItem(long menuItemId) {
        return ofy.get(new Key<MenuItem>(restaurantKey,
                MenuItem.class,menuItemId));
    }

    @Override
    public long addTable(String name) {
        Table table = new Table();
        table.setName(name);
        table.setRestaurant(restaurantKey);
        ofy.put(table);
        final long id = table.getId();
        return id;
    }

    @Override
    public List<Table> getTables() {
        return ofy.query(Table.class).ancestor(restaurantKey).list();
    }

    @Override
    public List<Order> getOrdersByTableName(BillType billType, 
            OrderState state, String tableName) {
        String tableKeyString = null;
        if(tableName != null){
            List<Table> tables = ofy.query(Table.class).filter("name", 
                    tableName).ancestor(restaurantKey).list();
            if(tables.size() == 1){
                tableKeyString = tables.get(0).getKeyString();
            }
            else if( tables.size() > 1){
                throw new IllegalStateException(
                        "More than one table with the same name");
            }
        }
        return getOrders(billType, state, tableKeyString, null);
    }

    @Override
    public long addOrModifyMenuCategory(Long categoryId, String name) {
        MenuCategory category = null;
        if(categoryId != null){
            Key<MenuCategory> key = new Key<MenuCategory>(
                    restaurantKey,
                    MenuCategory.class,
                    (long)categoryId);
            category = ofy.get(key);
        }
        else{
            category = new MenuCategory();
            if(name == null){
                throw new IllegalArgumentException(
                        "name must not be null");
            }
            category.setRestaurant(restaurantKey);
        }
        if(name != null){
            category.setName(name);
        }
        ofy.put(category);
        return category.getId();
    }

    @Override
    public List<MenuCategory> getCategories() {
        return ofy.query(MenuCategory.class).
                ancestor(restaurantKey).list();
    }

    @Override
    public List<MenuItem> getMenuItems() {
        return getMenuItems(null);
    }

    @Override
    public long getDeliveryTableId() {
        if(deliveryTableId == null){
            List<Table> tableList = ofy.query(Table.class).filter("name", DELIVERY_TABLE_NAME).
                    ancestor(restaurantKey).list();
            if(tableList.size()== 1){
                deliveryTableId = tableList.get(0).getId();
            }
            else {
                if(tableList.size() > 1){
                    throw new IllegalStateException();
                }
                deliveryTableId = addTable(DELIVERY_TABLE_NAME);
            }
        }
        return deliveryTableId;
    }

    @Override
    public Bill getBill(Key<Bill> billKey) {
        return ofy.get(billKey);
    }

    @Override
    public List<Bill> getBills(BillState state,
            BillType billType) {
        Query<Bill> query = ofy.query(Bill.class).
                ancestor(restaurantKey);
        if(state != null){
            query.filter("state", state);
        }
        if(billType != null){
            query.filter("type", billType);
        }
        return query.list();
    }

    @Override
    public void changeBillState(String billKeyString, BillState newState) {
        Key<Bill> key = new Key<Bill>(billKeyString);
        Bill bill = ofy.get(key);
        if(bill.getState() == BillState.CLOSED){
            throw new IllegalStateException(
                    "Trying to modify a closed Bill");
        }
        bill.setState(newState);
        ofy.put(bill);
    }


}
