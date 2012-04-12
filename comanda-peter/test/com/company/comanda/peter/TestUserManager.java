package com.company.comanda.peter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.comanda.peter.shared.Qualifiers;
import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.SessionAttributesFactory;
import com.company.comanda.peter.server.UserManager;
import com.company.comanda.peter.server.UserManager.CodifiedData;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.server.notification.NotificationManager;
import com.company.comanda.peter.shared.BillState;
import com.company.comanda.peter.shared.OrderState;
import com.company.comanda.peter.shared.BillType;
import com.company.comanda.peter.stubs.NotificationManagerStub;
import com.company.comanda.peter.stubs.SessionAttributesHashMap;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class TestUserManager {

    
    private static final String REST_NAME = "This is the name";
    private static final String REST_PASSWORD = "This is the password";
    private static final double SANTA_JUSTA_LATITUDE = 37.390925;
    private static final double SANTA_JUSTA_LONGITUDE = -5.976627;
    private static final double NERVION_LATITUDE = 37.387413;
    private static final double NERVION_LONGITUDE = -5.971584;
    private static final double DOS_HERMANAS_LATITUDE = 37.289350;
    private static final double DOS_HERMANAS_LONGITUDE = -5.922661;
    private static final int NO_OF_MENU_ITEMS = 10;
    private static final int NO_OF_TABLES = 10;
    private static final String PHONE_NUMBER = "987654321";
    private static final String USER_PASSWORD = "userpassword";
    private static final String CATEGORY_NAME = "Category name";
    
    private RestaurantManager manager;
    private UserManager userManager;
    
    private long restaurantId;
    private long userId;
    private long categoryId;
    
    
    private String[] tableCodes;
    
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
            .setDefaultHighRepJobPolicyUnappliedJobPercentage(0));

    private static final Injector injector = Guice.createInjector(
            new BusinessModule(),
            new AbstractModule(){

                @Override
                protected void configure() {
                    install(new FactoryModuleBuilder()
                    .implement(SessionAttributes.class, SessionAttributesHashMap.class)
                    .build(SessionAttributesFactory.class));
                    bind(NotificationManager.class).to(NotificationManagerStub.class);
                }

            });


    @Before
    public void setUp() throws Exception {
        helper.setUp();
        userManager = injector.getInstance(UserManager.class);
        createRestaurant();
        manager.login(REST_NAME, REST_PASSWORD);
        createCategory();
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
                        SessionAttributesFactory.class).create()).clear();
    }
    
    void createCategory(){
        categoryId = manager.getAgent().addOrModifyMenuCategory(null, 
                CATEGORY_NAME);
    }
    
    void createRestaurant(){
        manager = 
                injector.getInstance(RestaurantManager.class);
        ComandaAdmin admin = injector.getInstance(ComandaAdmin.class);
        
        restaurantId = admin.createOrModifyRestaurant(null,
                "name", REST_NAME, REST_PASSWORD,
                "Avenida Kansas City, 1, Sevilla",
                "Description", null,
                "678",0,0);
        
    }
    
    void createMenuItems(){
        for(int i=1;i<NO_OF_MENU_ITEMS;i++){
        	List<Float> prices = new ArrayList<Float>(1);
        	List<String> qualifiers = new ArrayList<String>(1);
        	prices.add((float)i);
        	qualifiers.add(Qualifiers.SINGLE.toString());
            manager.getAgent().addOrModifyMenuItem(null, 
                    "itemName" + i, "description" + i, 
                    prices, qualifiers, null,
                    categoryId,
                    new ArrayList<String>(), new ArrayList<Float>(), null);
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
            tableCodes[i] = tables.get(i).getCode();
        }
    }
    
    void registerUser(){
        userId = userManager.registerUser(PHONE_NUMBER, USER_PASSWORD, "");
    }
    
    @Test
    public void testChangeBillState(){
        List<MenuItem> items = userManager.getMenuItems(restaurantId);
        
        final long itemId = items.get(0).getId();
        
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        final ArrayList<String> itemComments = new ArrayList<String>();
        final ArrayList<Integer> qualifiers = new ArrayList<Integer>(1);
        final ArrayList<Integer> noOfItems = new ArrayList<Integer>(1);
        final ArrayList<List<Integer>> extras = new ArrayList<List<Integer>>(1);
        qualifiers.add(0);
        noOfItems.add(1);
        itemIds.add(itemId);
        extras.add(new ArrayList<Integer>(0));
        itemComments.add("Comentarios");
        
        final String billKeyString1 = userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers, noOfItems, itemComments, extras, "Test address", 
                null, "Comentarios generales",
                BillType.DELIVERY,
                null);
        
        final String billKeyString2 = userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers, noOfItems,
                itemComments, extras, "Test address2", 
                null, "Comentarios generales2",
                BillType.DELIVERY,
                null);
        
        List<Bill> inRestaurant = manager.getAgent().getBills(null, 
                BillType.IN_RESTAURANT);
        Assert.assertEquals(0, inRestaurant.size());
        
        List<Bill> allBills = manager.getAgent().getBills(null, null);
        Assert.assertEquals(2, allBills.size());
        
        List<Bill> delivery = manager.getAgent().getBills(null, BillType.DELIVERY);
        Assert.assertEquals(2, delivery.size());
        
        List<Bill> delivered = manager.getAgent().getBills(BillState.DELIVERED, 
                BillType.DELIVERY);
        Assert.assertEquals(0, delivered.size());
        
        List<Bill> open = manager.getAgent().getBills(BillState.OPEN, 
                BillType.DELIVERY);
        Assert.assertEquals(2, open.size());
        
        //Now, change state
        
        manager.getAgent().changeBillState(billKeyString1, BillState.DELIVERED, 
                null);
        
        delivery = manager.getAgent().getBills(null, BillType.DELIVERY);
        Assert.assertEquals(2, delivery.size());
        
        delivered = manager.getAgent().getBills(BillState.DELIVERED, 
                BillType.DELIVERY);
        Assert.assertEquals(1, delivered.size());
        
        open = manager.getAgent().getBills(BillState.OPEN, 
                BillType.DELIVERY);
        Assert.assertEquals(1, open.size());
        
        Assert.assertEquals("Comentarios generales", delivered.get(0).getComments());
        Assert.assertEquals("Comentarios generales2", open.get(0).getComments());
    }
    
    @Test
    public void testPlaceOrder() {
        final String code = tableCodes[0];
        
        CodifiedData data = userManager.getData(code);
        
        final long restaurantId = data.restaurant.getId();
        final long tableId = data.table.getId();
        final String tableKeyString = data.table.getKeyString();
        
        List<MenuItem> items = userManager.getMenuItems(restaurantId);
        
        final long itemId = items.get(0).getId();
        
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        final ArrayList<String> itemComments = new ArrayList<String>();
        final ArrayList<Integer> qualifiers = new ArrayList<Integer>(1);
        final ArrayList<Integer> noOfItems = new ArrayList<Integer>(1);
        final ArrayList<List<Integer>> extras = new ArrayList<List<Integer>>(1);
        
        qualifiers.add(0);
        noOfItems.add(1);
        itemIds.add(itemId);
        itemComments.add("Comentarios");
        extras.add(new ArrayList<Integer>(0));
        
        final String billKeyString = userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers, noOfItems, itemComments, extras, null, 
                tableId, "Comentarios generales",
                BillType.IN_RESTAURANT,
                null);
        
        List<Order> ordersFromRestaurant = manager.
                getAgent().getOrders(BillType.IN_RESTAURANT, null, tableKeyString, null);
        List<Order> deliveriesFromRestaurant = manager.
                getAgent().getOrders(BillType.DELIVERY, null, tableKeyString, null);
        
        assertEquals(1, ordersFromRestaurant.size());
        assertEquals(0, deliveriesFromRestaurant.size());
        
        List<Order> ordersFromUser = 
                userManager.getOrders(userId, 
                        USER_PASSWORD, billKeyString);
        
        assertEquals(1, ordersFromUser.size());
        
        List<Bill> billsFromUser = userManager.getBills(userId, USER_PASSWORD);
        assertEquals(1, billsFromUser.size());
        
    }

    
    @Test
    public void testTwoOrdersOneBill() {
        final String code = tableCodes[0];
        
        CodifiedData data = userManager.getData(code);
        
        final long restaurantId = data.restaurant.getId();
        final long tableId = data.table.getId();
        final String tableKeyString = data.table.getKeyString();
        
        List<MenuItem> items = userManager.getMenuItems(restaurantId);
        
        final long itemId = items.get(0).getId();
        
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        final ArrayList<String> itemComments = new ArrayList<String>();
        final ArrayList<Integer> qualifiers = new ArrayList<Integer>(1);
        final ArrayList<Integer> noOfItems = new ArrayList<Integer>(1);
        final ArrayList<List<Integer>> extras = new ArrayList<List<Integer>>(1);
        
        qualifiers.add(0);
        noOfItems.add(1);
        itemIds.add(itemId);
        itemComments.add("Comentarios");
        extras.add(new ArrayList<Integer>(0));
        
        final String billKeyString = userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers, noOfItems, itemComments, extras,
                null, 
                tableId, "Comentarios generales",
                BillType.IN_RESTAURANT,
                null);
        
        userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers, noOfItems, itemComments, extras,
                null, 
                tableId, "Comentarios generales2",
                BillType.IN_RESTAURANT,
                billKeyString);
        
        List<Order> ordersFromRestaurant = manager.
                getAgent().getOrders(BillType.IN_RESTAURANT, null, tableKeyString, null);
        
        assertEquals(2, ordersFromRestaurant.size());
        
        List<Order> ordersFromUser = 
                userManager.getOrders(userId, 
                        USER_PASSWORD, billKeyString);
        
        assertEquals(2, ordersFromUser.size());
        
        List<Bill> billsFromUser = userManager.getBills(userId, USER_PASSWORD);
        assertEquals(1, billsFromUser.size());
        
    }
    
    
    @Test
    public void testTwoOrdersTwoBills() {
        final String code = tableCodes[0];
        
        CodifiedData data = userManager.getData(code);
        
        final long restaurantId = data.restaurant.getId();
        final long tableId = data.table.getId();
        final String tableKeyString = data.table.getKeyString();
        
        List<MenuItem> items = userManager.getMenuItems(restaurantId);
        
        final long itemId = items.get(0).getId();
        
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        final ArrayList<String> itemComments = new ArrayList<String>();
        final ArrayList<Integer> qualifiers = new ArrayList<Integer>(1);
        final ArrayList<Integer> noOfItems = new ArrayList<Integer>(1);
        final ArrayList<List<Integer>> extras = new ArrayList<List<Integer>>(1);
        
        qualifiers.add(0);
        noOfItems.add(1);
        itemIds.add(itemId);
        itemComments.add("Comentarios");
        extras.add(new ArrayList<Integer>(0));
        
        final String billKeyString1 = userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers, noOfItems, itemComments, extras,
                null, 
                tableId, "Comentarios generales",
                BillType.IN_RESTAURANT,
                null);
        
        final String billKeyString2 = userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers, noOfItems, itemComments, extras,
                null, 
                tableId, "Comentarios generales2",
                BillType.IN_RESTAURANT,
                null);
        
        List<Order> ordersFromRestaurant1 = manager.
                getAgent().getOrders(null, null, null, billKeyString1);
        
        assertEquals(1, ordersFromRestaurant1.size());
        
        List<Order> ordersFromRestaurant2 = manager.
                getAgent().getOrders(null, null, null, billKeyString2);
        
        assertEquals(1, ordersFromRestaurant2.size());
        
        List<Order> ordersFromUser1 = 
                userManager.getOrders(userId, 
                        USER_PASSWORD, billKeyString1);
        
        assertEquals(1, ordersFromUser1.size());
        
        List<Order> ordersFromUser2 = 
                userManager.getOrders(userId, 
                        USER_PASSWORD, billKeyString2);
        
        assertEquals(1, ordersFromUser2.size());
        
        List<Bill> billsFromUser = userManager.getBills(userId, USER_PASSWORD);
        assertEquals(2, billsFromUser.size());
        
    }
    
    @Test
    public void testModifyOrder(){
        final String code = tableCodes[0];
        
        CodifiedData data = userManager.getData(code);
        
        final long restaurantId = data.restaurant.getId();
        final long tableId = data.table.getId();
        final String tableKeyString = data.table.getKeyString();
        
        List<MenuItem> items = userManager.getMenuItems(restaurantId);
        
        final long itemId = items.get(0).getId();
        
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        final ArrayList<String> itemComments = new ArrayList<String>();
        final ArrayList<Integer> qualifiers = new ArrayList<Integer>(1);
        final ArrayList<Integer> noOfItems = new ArrayList<Integer>(1);
        final ArrayList<List<Integer>> extras = new ArrayList<List<Integer>>(1);
        
        qualifiers.add(0);
        noOfItems.add(1);
        itemIds.add(itemId);
        itemComments.add("Comentarios");
        extras.add(new ArrayList<Integer>(0));
        
        userManager.placeOrder(userId, USER_PASSWORD, 
                restaurantId, itemIds, qualifiers,
                noOfItems,
                itemComments, extras, null, 
                tableId, "Comentarios generales",
                BillType.IN_RESTAURANT, null);
        
        List<Order> orders = manager.
                getAgent().getOrders(BillType.IN_RESTAURANT,
                        null, tableKeyString, null);
        
        assertEquals(1, orders.size());
        manager.getAgent().changeOrderState(orders.get(0).getKeyString(), 
                OrderState.ACCEPTED);
        
        List<Order> allOrders = manager.
                getAgent().getOrders(BillType.IN_RESTAURANT,
                        null, tableKeyString, null);
        
        List<Order> acceptedOrders = manager.
                getAgent().getOrders(BillType.IN_RESTAURANT,
                        OrderState.ACCEPTED, tableKeyString, null);
        
        List<Order> orderedOrders = manager.
                getAgent().getOrders(
                        BillType.IN_RESTAURANT, 
                        OrderState.ORDERED, tableKeyString, null);
        
        assertEquals(1, allOrders.size());
        assertEquals(0, orderedOrders.size());
        assertEquals(1, acceptedOrders.size());
    }
    
    @Test
    public void testGeographicSearchFound(){
        List<Restaurant> restaurants = userManager.searchRestaurant(
                NERVION_LATITUDE, NERVION_LONGITUDE, 10, 10000);
        
        assertEquals(1, restaurants.size());
    }
    
    @Test
    public void testGeographicSearchTooFar(){
        List<Restaurant> restaurants = userManager.searchRestaurant(
                DOS_HERMANAS_LATITUDE, DOS_HERMANAS_LONGITUDE, 10, 10000);
        
        assertEquals(0, restaurants.size());
    }
    
    @Test
    public void testGeographicSearchWideArea(){
        List<Restaurant> restaurants = userManager.searchRestaurant(
                DOS_HERMANAS_LATITUDE, DOS_HERMANAS_LONGITUDE, 10, 100000);
        
        assertEquals(1, restaurants.size());
    }
}
