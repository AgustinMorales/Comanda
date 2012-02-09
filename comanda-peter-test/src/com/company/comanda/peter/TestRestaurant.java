package com.company.comanda.peter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.comanda.peter.server.RestaurantAgent;
import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.stubs.SessionAttributesHashMap;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestRestaurant {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
            .setDefaultHighRepJobPolicyUnappliedJobPercentage(0));

    private static final Injector injector = Guice.createInjector(
            new BusinessModule(),
            new AbstractModule(){

                @Override
                protected void configure() {
                    bind(SessionAttributes.class).
                    to(SessionAttributesHashMap.class);

                }

            });


    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
        ((SessionAttributesHashMap)
                injector.getInstance(
                        SessionAttributes.class)).clear();
    }
    
    @Test
    public void testNoAgent(){
        RestaurantManager manager = 
                injector.getInstance(RestaurantManager.class);
        ComandaAdmin admin = injector.getInstance(ComandaAdmin.class);
        
        final String NAME = "This is the name";
        final String PASSWORD = "This is the password";
        
        admin.createRestaurant(NAME, PASSWORD);
        
        assertNull(manager.getAgent());
    }
    
    @Test
    public void testSameAgent(){
        RestaurantManager manager = 
                injector.getInstance(RestaurantManager.class);
        ComandaAdmin admin = injector.getInstance(ComandaAdmin.class);
        
        final String NAME = "This is the name";
        final String PASSWORD = "This is the password";
        
        long restaurantId = admin.createRestaurant(NAME, PASSWORD);
        
        manager.login(NAME, PASSWORD);
        
        assertTrue(manager.getAgent() == manager.getAgent());
    }
    
    @Test
    public void test() {
        RestaurantManager manager = 
                injector.getInstance(RestaurantManager.class);
        ComandaAdmin admin = injector.getInstance(ComandaAdmin.class);
        
        final String NAME = "This is the name";
        final String PASSWORD = "This is the password";
        
        long restaurantId = admin.createRestaurant(NAME, PASSWORD);
        
        assertTrue(manager.login(NAME, PASSWORD));
        assertFalse(manager.login(NAME + ".", PASSWORD));
        
        RestaurantAgent agent = manager.getAgent();
        
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final String ITEM_PRICE = "345";
        final String BLOB_KEY = "ABCD";
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                ITEM_PRICE, BLOB_KEY);
        
        List<MenuItem> items = agent.getMenuItems();
        assertEquals(1, items.size());
        
    }

}
