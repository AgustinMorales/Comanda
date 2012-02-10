package com.company.comanda.peter;

import static org.junit.Assert.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.comanda.peter.server.RestaurantAgent;
import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.stubs.FirstOperationOnlyPolicy;
import com.company.comanda.peter.stubs.SessionAttributesHashMap;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

public class TestRestaurant {

    private static final String REST_NAME = "This is the name";
    private static final String REST_PASSWORD = "This is the password";
    
    private RestaurantManager manager;
    
    private long restaurantId;
    
    private static final Logger log = 
            Logger.getLogger(TestRestaurant.class.getName()
                    );
    
    
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
            .setAlternateHighRepJobPolicyClass(FirstOperationOnlyPolicy.class));

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
        createRestaurant();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
        ((SessionAttributesHashMap)
                injector.getInstance(
                        SessionAttributes.class)).clear();
    }
    
    void createRestaurant(){
        manager = 
                injector.getInstance(RestaurantManager.class);
        ComandaAdmin admin = injector.getInstance(ComandaAdmin.class);
        
        restaurantId = admin.createRestaurant(REST_NAME, REST_PASSWORD);
        
    }
    @Test
    public void testNoAgent(){
        assertNull(manager.getAgent());
    }
    @Test
    public void testLogin(){
        assertTrue(manager.login(REST_NAME, REST_PASSWORD));
        assertNotNull(manager.getAgent());
    }
    
    @Test
    public void testSameAgent(){
        manager.login(REST_NAME, REST_PASSWORD);
        assertSame(manager.getAgent(), manager.getAgent());
    }
    @Test
    public void testWrongUsername(){
        assertFalse(manager.login(REST_NAME + ".", REST_PASSWORD));
        assertNull(manager.getAgent());
    }
    @Test
    public void testWrongPassword(){
        assertFalse(manager.login(REST_NAME, REST_PASSWORD + "."));
        assertNull(manager.getAgent());
    }
    @Test
    public void testWrongUsernameAndPassword(){
        assertFalse(manager.login(REST_NAME + ".", REST_PASSWORD + "."));
        assertNull(manager.getAgent());
    }
    @Test
    public void testLoginTwice(){
        assertTrue(manager.login(REST_NAME, REST_PASSWORD));
        try{
            manager.login(REST_NAME, REST_PASSWORD);
            fail();
        }
        catch(IllegalStateException e){
            
        }
    }
    @Test
    public void testAddMenuItem() {
        
        manager.login(REST_NAME, REST_PASSWORD);
        
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
        
        MenuItem item = items.get(0);
        
        assertEquals(ITEM_NAME, item.getName());
        assertEquals(ITEM_DESCRIPTION, 
                item.getDescription());
        assertEquals(Integer.parseInt(ITEM_PRICE), item.getPrice());
        assertEquals(BLOB_KEY, item.getImageString());
        
        
    }

    @Test
    public void testDeleteMenuItem(){
        manager.login(REST_NAME, REST_PASSWORD);
        
        RestaurantAgent agent = manager.getAgent();
        
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final String ITEM_PRICE = "345";
        final String BLOB_KEY = "ABCD";
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                ITEM_PRICE, BLOB_KEY);
        
        List<MenuItem> items = agent.getMenuItems();
        
        MenuItem item = items.get(0);
        
        agent.deleteMenuItems(new long[]{item.getId()});
        
        assertEquals(0, agent.getMenuItems().size());
        try{
            agent.getMenuItem(new Key<MenuItem>(
                new Key<Restaurant>(Restaurant.class, restaurantId),
                MenuItem.class,item.getId()));
                fail();
        }
        catch (NotFoundException e) {
        }
    }
    
    @Test
    public void testModifyMenuItem(){
        manager.login(REST_NAME, REST_PASSWORD);
        
        RestaurantAgent agent = manager.getAgent();
        
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final String ITEM_PRICE = "345";
        final String BLOB_KEY = "ABCD";
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                ITEM_PRICE, BLOB_KEY);
        
        MenuItem item = agent.getMenuItems().get(0);
        
        final String NEW_ITEM_NAME = "nuevonombre";
        final String NEW_ITEM_DESCRIPTION = "Pescado description new";
        final String NEW_ITEM_PRICE = "35";
        final String NEW_BLOB_KEY = "ABCDEFG";
        
        final long itemId = item.getId();
        agent.addOrModifyMenuItem(item.getId(), 
                NEW_ITEM_NAME, NEW_ITEM_DESCRIPTION, 
                NEW_ITEM_PRICE, NEW_BLOB_KEY);
        
        MenuItem modifiedItem = agent.getMenuItems().get(0);
        
        assertEquals(NEW_ITEM_NAME, modifiedItem.getName());
        assertEquals(NEW_ITEM_DESCRIPTION, 
                modifiedItem.getDescription());
        assertEquals(Integer.parseInt(NEW_ITEM_PRICE), 
                modifiedItem.getPrice());
        assertEquals(NEW_BLOB_KEY, modifiedItem.getImageString());
        assertEquals(itemId, (long)modifiedItem.getId());
    }
    
    @Test
    public void testAddTable(){
        manager.login(REST_NAME, REST_PASSWORD);
        
        RestaurantAgent agent = manager.getAgent();
        
        final String TABLE_NAME = "This is the new table";
        final long returnedId = agent.addTable(TABLE_NAME);
        
        List<Table> tables = agent.getTables();
        
        assertEquals(1, tables.size());
        
        Table table = tables.get(0);
        
        assertEquals(returnedId, (long)table.getId());
        
        assertEquals(8, table.getCode().length());
    }
    
    @Test
    public void testDifferentTableCodes(){
        manager.login(REST_NAME, REST_PASSWORD);
        
        RestaurantAgent agent = manager.getAgent();
        
        final String TABLE1 = "table1";
        final String TABLE2 = "table2";
        
        agent.addTable(TABLE1);
        agent.addTable(TABLE2);
        
        List<Table> tables = agent.getTables();
        
        assertEquals(2, tables.size());
        
        Table table1 = tables.get(0);
        Table table2 = tables.get(1);
        
        assertTrue(table1.getCode().equals(
                table2.getCode()) == false);
    }
}
