package com.company.comanda.peter.server;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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

    public UserManagerImpl(Objectify ofy){
        this.ofy = ofy;
    }

    @Override
    public void placeOrder(long userId, String password, long restaurantId,
            long menuItemId, long tableId) {
        // TODO Check password
        Key<Restaurant> restaurantKey = new Key<Restaurant>(
                Restaurant.class,restaurantId);
        Key<MenuItem> menuItemKey = new Key<MenuItem>(restaurantKey, 
                MenuItem.class, menuItemId);
        Key<Table> tableKey = new Key<Table>(Table.class, tableId);
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

}
