package com.company.comanda.peter;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.comanda.peter.server.ItemsManager;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.shared.OrderState;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestItemsManager {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
            .setDefaultHighRepJobPolicyUnappliedJobPercentage(0));

    private ItemsManager itemsManager;


    @Before
    public void setUp() throws Exception {
        helper.setUp();
        itemsManager = ItemsManager.me();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void testPlaceOrder() {
        long restaurantId = itemsManager.addRestaurant();

        long keyId = addMenuItem(restaurantId);

        final String TABLE_NAME = "This is the table";

        itemsManager.placeOrder(restaurantId, keyId, TABLE_NAME);

        List<Order> orders = itemsManager.getOrders(null, null);

        assertEquals(1, orders.size());

        Order order = orders.get(0);

        assertEquals(TABLE_NAME, order.getTable());
        assertEquals(OrderState.ORDERED, order.getState());
        //TODO: Check order name
    }

    @Test
    public void testModifyOrder() {
        testPlaceOrder();

        List<Order> orders = itemsManager.getOrders(null, null);

        assertEquals(1, orders.size());

        Order order = orders.get(0);

        itemsManager.modifyOrder(order.getId(), OrderState.ACCEPTED);
        
        orders = itemsManager.getOrders(null, null);

        assertEquals(1, orders.size());

        order = orders.get(0);
        
        assertEquals(OrderState.ACCEPTED, order.getState());
    }

    @Test
    public void testDeleteMenuItems() {
        long restaurantId = itemsManager.addRestaurant();

        long keyId = addMenuItem(restaurantId);
        
        itemsManager.deleteMenuItems(restaurantId, new long[]{keyId});
        
        List<MenuItem> items = itemsManager.getMenuItems(restaurantId);
        
        assertEquals(0, items.size());
    }

    private long addMenuItem(long restaurantId){

        itemsManager.addOrModifyMenuItem(null, 
                "item1", "description1", 
                "300", "image1", restaurantId);
        List<MenuItem> menuItems = itemsManager.getMenuItems(restaurantId);

        assertEquals(1, menuItems.size());

        return menuItems.get(0).getId();

    }
}
