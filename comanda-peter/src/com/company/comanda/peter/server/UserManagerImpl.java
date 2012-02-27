package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.LocationCapableRepositorySearch;
import com.beoui.geocell.model.Point;
import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.server.model.User;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.BillType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;

public class UserManagerImpl implements UserManager {

    private static final Logger log = LoggerFactory.
            getLogger(UserManagerImpl.class.getName());
    private Objectify ofy;
    private RestaurantAgentFactory agentFactory;

    public class OfyEntityLocationCapableRepositorySearchImpl implements
    LocationCapableRepositorySearch<Restaurant> {



        @Override
        public List<Restaurant> search(List<String> geocells) {
            return ofy.query(Restaurant.class)
                    .filter("geocells in ", geocells).list();
        }

    }
    @Inject
    public UserManagerImpl(Objectify ofy, 
            RestaurantAgentFactory agentFactory){
        this.ofy = ofy;
        this.agentFactory = agentFactory;
    }

    @Override
    public String placeOrder(long userId, String password, long restaurantId,
            List<Long> menuItemIds, List<String> menuItemComments, String address, 
            long tableId,
            String comments,
            BillType type,
            String billKeyString) {
        // TODO Check password
        final Date date = new Date();
        Key<User> userKey = new Key<User>(User.class, userId);
        final Key<Restaurant> restaurantKey = new Key<Restaurant>(
                Restaurant.class,restaurantId);
        Key<Table> tableKey = null;
        if(type == BillType.IN_RESTAURANT){
            tableKey = new Key<Table>(restaurantKey,Table.class, tableId);
        }
        Table table = ofy.get(tableKey);
        Bill bill = null;
        if(billKeyString != null){
            bill = ofy.get(new Key<Bill>(billKeyString));
        }
        else{
            //FIXME: Are there any modifiable Bill fields?
            bill = new Bill();
            bill.setUser(userKey);
            bill.setAddress(address);
            bill.setTable(tableKey);
            bill.setType(type);
            bill.setOpenDate(date);
            bill.setRestaurant(restaurantKey);
            bill.setComments(comments);
            bill.setTableName(table.getName());
            ofy.put(bill);
        }
        
        Key<Bill> billKey = new Key<Bill>(restaurantKey,Bill.class,bill.getId());
        final int no_of_elements = menuItemIds.size();
        if(menuItemComments.size() != no_of_elements){
            throw new IllegalArgumentException("Different number of comments");
        }
        List<Order> newOrders = new ArrayList<Order>(no_of_elements);
        
        for(int i=0;i<no_of_elements;i++){
            
            final Key<MenuItem> menuItemKey = new Key<MenuItem>(restaurantKey,
                    MenuItem.class,menuItemIds.get(i));
            MenuItem menuItem = ofy.get(menuItemKey);
            Order newOrder = new Order(date, OrderState.ORDERED, 
                    menuItem.getName(),
                    menuItem.getPrice(),
                    menuItemKey,
                            comments, billKey);
            newOrder.setTable(tableKey);
            newOrders.add(newOrder);
        }
        ofy.put(newOrders);

        return billKeyString;
    }

    @Override
    public CodifiedData getData(String code) {
        CodifiedData result = null;

        int totalLength = code.length();
        int restaurantLength = totalLength - 
                (Table.TABLE_CODE_ID_PART_WIDTH +
                        Table.TABLE_CODE_RANDOM_PART_WIDTH);

        long restaurantId = Long.parseLong(
                code.substring(0, restaurantLength));
        String table_code = code.substring(restaurantLength);

        Key<Restaurant> restaurantKey = 
                new Key<Restaurant>(Restaurant.class, restaurantId);

        List<Table> tables = ofy.query(Table.class).
                filter("code", table_code).
                ancestor(restaurantKey).list();

        if(tables.size() == 1){
            try{
                Restaurant restaurant = ofy.get(restaurantKey);
                Table table = tables.get(0);
                result = new CodifiedData();
                result.restaurant = restaurant;
                result.table = table;
            }
            catch(NotFoundException e){
                log.info("Could not find restaurant. Code='{}'", 
                        code);
            }
        }
        else{
            if(tables.size() > 1){
                throw new IllegalStateException(
                        "More than one table with the same code. " +
                                "Restaurant: '" + restaurantId + 
                                "'. Code: '" + table_code + "'");
            }
        }

        return result;
    }

    @Override
    public long registerUser(String phoneNumber, 
            String password, String validationCode) {
        
        List<User> users = ofy.query(User.class).
                filter("phoneNumber", phoneNumber).list();
        
        //TODO: Check validation code

        User user = null;
        if(users.size() == 0){
            user = new User();
            user.setPhoneNumber(phoneNumber);
        }
        else{
            assert users.size() == 1;
            user = users.get(0);
        }
        user.setPassword(password);
        ofy.put(user);

        return user.getId();
    }

    @Override
    public List<MenuItem> getMenuItems(long restaurantId) {
        RestaurantAgent agent = agentFactory.create(restaurantId);
        return agent.getMenuItems(null);
    }

    @Override
    public List<Restaurant> searchRestaurant(double latitude, double longitude,
            int maxResults, double maxDistance) {
        LocationCapableRepositorySearch<Restaurant> ofySearch = 
                new OfyEntityLocationCapableRepositorySearchImpl();

        return GeocellManager.proximityFetch(
                new Point(latitude, longitude), 
                maxResults, maxDistance, ofySearch);
    }

    @Override
    public List<MenuCategory> getMenuCategories(long restaurantId) {
        return agentFactory.create(restaurantId).getCategories();
    }

}
