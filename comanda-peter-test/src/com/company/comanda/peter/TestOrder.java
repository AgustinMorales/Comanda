package com.company.comanda.peter;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.comanda.peter.server.GetTablesServlet;
import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.UserManager;
import com.company.comanda.peter.server.UserManager.CodifiedData;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.stubs.FirstOperationOnlyPolicy;
import com.company.comanda.peter.stubs.SessionAttributesHashMap;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestOrder {

    
    private static final String REST_NAME = "This is the name";
    private static final String REST_PASSWORD = "This is the password";
    private static final int NO_OF_MENU_ITEMS = 10;
    private static final int NO_OF_TABLES = 10;
    private static final String PHONE_NUMBER = "987654321";
    private static final String USER_PASSWORD = "userpassword";
    
    private RestaurantManager manager;
    private UserManager userManager;
    
    private long restaurantId;
    private long userId;
    
    
    private String[] tableCodes;
    
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
        userManager = injector.getInstance(UserManager.class);
        createRestaurant();
        manager.login(REST_NAME, REST_PASSWORD);
        createMenuItems();
        createTables();
        getCodes();
        registerUser();
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
    
    void createMenuItems(){
        for(int i=1;i<NO_OF_MENU_ITEMS;i++){
            manager.getAgent().addOrModifyMenuItem(null, 
                    "itemName" + i, "description" + i, 
                    "" + i, "imageBlobkey" + i);
        }
    }
    
    void createTables(){
        for(int i=1;i<NO_OF_TABLES;i++){
            manager.getAgent().addTable("table" + i);
        }
    }

    void getCodes(){
        List<Table> tables = manager.getAgent().getTables();
        tableCodes = new String[tables.size()];
        for(int i=0;i<tables.size();i++){
            tableCodes[i] = String.format(
                    "%08d%s", restaurantId,tables.get(i).getCode());
        }
    }
    
    void registerUser(){
        userId = userManager.registerUser(PHONE_NUMBER, USER_PASSWORD, "");
    }
    
    @Test
    public void testPlaceOrder() {
        final String code = tableCodes[0];
        
        CodifiedData data = userManager.getData(code);
        
        final long restaurantId = data.restaurant.getId();
        final long tableId = data.table.getId();
        
        List<MenuItem> items = userManager.getMenuItems(restaurantId);
        
        final long itemId = items.get(0).getId();
        
        userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemId, tableId);
        
        List<Order> orders = manager.
                getAgent().getOrders(null, tableId);
        
        assertEquals(1, orders.size());
        
    }

}
