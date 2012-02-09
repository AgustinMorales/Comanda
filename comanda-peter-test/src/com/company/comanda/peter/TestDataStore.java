package com.company.comanda.peter;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.comanda.peter.server.ItemsManager;
import com.company.comanda.peter.server.model.MenuItem;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestDataStore {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
            .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));


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
    public void testAddMenuItem() {
        long resturantId = itemsManager.addRestaurant();
        itemsManager.addOrModifyMenuItem(null, 
                "item1", "description1", 
                "300", "image1", resturantId);
        List<MenuItem> menuItems = itemsManager.getMenuItems(resturantId);

        assertEquals(1, menuItems.size());
    }

    @Test
    public void testModifyMenuItem() {
        long resturantId = itemsManager.addRestaurant();
        itemsManager.addOrModifyMenuItem(null, 
                "item1", "description1", 
                "300", "image1", resturantId);
        List<MenuItem> menuItems = itemsManager.getMenuItems(resturantId);

        assertEquals(1, menuItems.size());

        MenuItem addedMenuItem = menuItems.get(0);

        itemsManager.addOrModifyMenuItem(
                addedMenuItem.getId(), 
                "item1_modified", 
                null, 
                null, 
                null, 
                resturantId);

        menuItems = itemsManager.getMenuItems(resturantId);

        assertEquals(1, menuItems.size());

        MenuItem modifiedMenuItem = menuItems.get(0);

        assertEquals("item1_modified", modifiedMenuItem.getName());
    }

}        
