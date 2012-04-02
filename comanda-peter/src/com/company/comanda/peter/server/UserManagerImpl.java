package com.company.comanda.peter.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.shared.OrderState;
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
            List<Long> menuItemIds, 
            List<Integer> menuItemQualifierIndexes,
            List<String> menuItemComments, String address, 
            Long tableId,
            String comments,
            BillType type,
            String billKeyString) {
        // TODO Check password
        final Date date = new Date();
        Key<User> userKey = new Key<User>(User.class, userId);
        User user = ofy.get(userKey);
        final Key<Restaurant> restaurantKey = new Key<Restaurant>(
                Restaurant.class,restaurantId);
        final String restaurantName = ofy.get(restaurantKey).getName();
        Key<Table> tableKey = null;
        Table table = null;
        if(type == BillType.IN_RESTAURANT){
            if(tableId == null){
                throw new IllegalArgumentException(
                        "tableId needed for IN_RESTAURANT orders");
            }
            tableKey = new Key<Table>(restaurantKey,Table.class, tableId);
            table = ofy.get(tableKey);
        }
        
        Bill bill = null;
        if(billKeyString != null){
            bill = ofy.get(new Key<Bill>(billKeyString));
            if(bill.getState() == BillState.CLOSED){
                throw new IllegalStateException(
                        "Trying to modify a CLOSED bill");
            }
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
            bill.setTableName(table != null?table.getName():null);
            bill.setRestaurantName(restaurantName);
            bill.setState(BillState.OPEN);
            bill.setPhoneNumber(user.getPhoneNumber());
            ofy.put(bill);
        }
        
        Key<Bill> billKey = new Key<Bill>(restaurantKey,Bill.class,bill.getId());
        billKeyString = billKey.getString();
        final int no_of_elements = menuItemIds.size();
        if(menuItemComments.size() != no_of_elements){
            throw new IllegalArgumentException("Different number of comments");
        }
        List<Order> newOrders = new ArrayList<Order>(no_of_elements);
        
        float totalAmount = bill.getTotalAmount();
        for(int i=0;i<no_of_elements;i++){
            
            final Key<MenuItem> menuItemKey = new Key<MenuItem>(restaurantKey,
                    MenuItem.class,menuItemIds.get(i));
            final int qualifierIndex = menuItemQualifierIndexes.get(i);
            MenuItem menuItem = ofy.get(menuItemKey);
            float price = menuItem.getPrices().get(qualifierIndex);
            Order newOrder = new Order(date, OrderState.ORDERED, 
                    menuItem.getName(),
                    price,
                    menuItemKey,
                            comments, billKey,
                            type);
            newOrder.setTable(tableKey);
            newOrders.add(newOrder);
            totalAmount = totalAmount + price;
        }
        bill.setTotalAmount(totalAmount);
        ofy.put(bill);
        ofy.put(newOrders);

        return billKeyString;
    }

    @Override
    public CodifiedData getData(String code) {
        CodifiedData result = null;

        Key<Table> tableKey = Table.parseCode(code);
        Table table = ofy.get(tableKey);
        Restaurant restaurant = ofy.get(table.getRestaurant());
        
        result = new CodifiedData();
        result.restaurant = restaurant;
        result.table = table;

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

    @Override
    public List<Bill> getBills(long userId, String password) {
        //TODO: Authenticate user
        Key<User> userKey = new Key<User>(User.class, userId);
        return ofy.query(Bill.class).filter("user", userKey).order("-openDate").list();
    }

    @Override
    public List<Order> getOrders(long userId, String password, String billKeyString) {
        //TODO: Authenticate user
        Key<Bill> billKey = new Key<Bill>(billKeyString);
        return ofy.query(Order.class).ancestor(billKey).list();
    }

}
