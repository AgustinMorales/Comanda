package com.company.comanda.peter.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.LocationCapableRepositorySearch;
import com.beoui.geocell.model.Point;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.server.model.User;
import com.company.comanda.peter.shared.OrderState;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;

public class UserManagerImpl implements UserManager {

    private static final Logger log = Logger.
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
    public void placeOrder(long userId, String password, long restaurantId,
            long menuItemId, long tableId) {
        // TODO Check password
        
        Key<Restaurant> restaurantKey = new Key<Restaurant>(
                Restaurant.class,restaurantId);
        Key<MenuItem> menuItemKey = new Key<MenuItem>(restaurantKey, 
                MenuItem.class, menuItemId);
        Key<Table> tableKey = new Key<Table>(restaurantKey,Table.class, tableId);
        MenuItem menuItem = ofy.get(menuItemKey);
        if(menuItem == null){
            String errorMsg = String.format("Could not place order for item ID: %s",menuItemId);
            log.warning(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        Order newOrder = new Order(new Date(), OrderState.ORDERED,
                tableKey, menuItemKey);
        newOrder.setUser(new Key<User>(User.class, userId));
        ofy.put(newOrder);

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
                log.info("Could not find restaurant. Code='" + 
                        code + "'");
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

}
