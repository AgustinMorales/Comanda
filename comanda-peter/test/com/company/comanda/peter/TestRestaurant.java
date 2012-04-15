package com.company.comanda.peter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.shared.Qualifiers;
import com.company.comanda.peter.server.RestaurantAgent;
import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.SessionAttributesFactory;
import com.company.comanda.peter.server.UserManager;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.server.notification.NotificationManager;
import com.company.comanda.peter.stubs.FirstOperationOnlyPolicy;
import com.company.comanda.peter.stubs.NotificationManagerStub;
import com.company.comanda.peter.stubs.SessionAttributesHashMap;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

public class TestRestaurant {

    private static final String REST_NAME = "This is the name";
    private static final String REST_PASSWORD = "This is the password";
    
    private static final String CATEGORY_NAME = "This is the category";
    
    private RestaurantManager manager;
    
    private long restaurantId;
    private long categoryId;
    private ComandaAdmin admin;
    
    private static final Logger log = 
            LoggerFactory.getLogger(TestRestaurant.class);
    
    
    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
            .setAlternateHighRepJobPolicyClass(FirstOperationOnlyPolicy.class));

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
        createRestaurant();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
        ((SessionAttributesHashMap)
                injector.getInstance(
                        SessionAttributesFactory.class).create()).clear();
    }
    
    void createCategory(){
        categoryId = createCategory(CATEGORY_NAME);
    }
    
    long createCategory(String name){
        return 
                manager.getAgent().addOrModifyMenuCategory(
                        null, name);
    }
    
    void createRestaurant(){
        manager = 
                injector.getInstance(RestaurantManager.class);
        admin = injector.getInstance(ComandaAdmin.class);
        
        restaurantId = admin.createOrModifyRestaurant(null,
                "Full name", REST_NAME, REST_PASSWORD,
                "Puerto de envalira, 1, sevilla","Description",
                null, "678",0,0, 0, null);
        
    }
    @Test
    public void testNewCategory(){
        manager.login(REST_NAME, REST_PASSWORD);
        createCategory();
        List<MenuCategory> categories = 
                manager.getAgent().getCategories();
        assertEquals(5,categories.size());
        MenuCategory category = categories.get(4);
        assertEquals(CATEGORY_NAME, category.getName());
        assertEquals(restaurantId, category.
                getRestaurant().getId());
    }
    @Test
    public void testModifyCategory(){
        manager.login(REST_NAME, REST_PASSWORD);
        createCategory();
        
        String NEW_NAME = "NEW CATEGORY NAME";
        manager.getAgent().addOrModifyMenuCategory(
                categoryId, NEW_NAME);
        List<MenuCategory> categories = 
                manager.getAgent().getCategories();
        assertEquals(5,categories.size());
        MenuCategory category = categories.get(4);
        assertEquals(NEW_NAME, category.getName());
        assertEquals(restaurantId, category.
                getRestaurant().getId());
    }
    @Test
    public void testNoAgent(){
        assertNull(manager.getAgent());
    }
    @Test
    public void testLogin(){
        assertNotNull(manager.login(REST_NAME, REST_PASSWORD));
        assertNotNull(manager.getAgent());
    }
    
    @Test
    public void testWrongUsername(){
        assertNull(manager.login(REST_NAME + ".", REST_PASSWORD));
        assertNull(manager.getAgent());
    }
    @Test
    public void testWrongPassword(){
        assertNull(manager.login(REST_NAME, REST_PASSWORD + "."));
        assertNull(manager.getAgent());
    }
    @Test
    public void testWrongUsernameAndPassword(){
        assertNull(manager.login(REST_NAME + ".", REST_PASSWORD + "."));
        assertNull(manager.getAgent());
    }
    @Test
    public void testLoginTwice(){
        assertNotNull(manager.login(REST_NAME, REST_PASSWORD));
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
        createCategory();
        
        RestaurantAgent agent = manager.getAgent();
        
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final float ITEM_PRICE = 345.56f;
        final String BLOB_KEY = null;
        List<Float> prices = new ArrayList<Float>(1);
        List<String> qualifiers = new ArrayList<String>(1);
        prices.add(ITEM_PRICE);
        qualifiers.add(Qualifiers.SINGLE.toString());
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                prices, qualifiers, BLOB_KEY, categoryId,
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        List<MenuItem> items = agent.getMenuItems();
        assertEquals(1, items.size());
        
        MenuItem item = items.get(0);
        
        assertEquals(ITEM_NAME, item.getName());
        assertEquals(ITEM_DESCRIPTION, 
                item.getDescription());
        assertEquals(ITEM_PRICE, item.getPrices().get(0),0);
        assertEquals("", item.getImageString());
        
        
    }

    @Test
    public void testAddMenuItemSeveralQualifiers() {
        
        manager.login(REST_NAME, REST_PASSWORD);
        createCategory();
        
        RestaurantAgent agent = manager.getAgent();
        
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final float ITEM_PRICE_TAPA = 345.56f;
        final float ITEM_PRICE_HALF= 3450.56f;
        final float ITEM_PRICE_FULL = 81923.43f;
        
        final String BLOB_KEY = null;
        List<Float> prices = new ArrayList<Float>(3);
        List<String> qualifiers = new ArrayList<String>(3);
        prices.add(ITEM_PRICE_TAPA);
        prices.add(ITEM_PRICE_HALF);
        prices.add(ITEM_PRICE_FULL);
        qualifiers.add(Qualifiers.TAPA.toString());
        qualifiers.add(Qualifiers.HALF.toString());
        qualifiers.add(Qualifiers.FULL.toString());
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                prices, qualifiers, BLOB_KEY, categoryId,
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        List<MenuItem> items = agent.getMenuItems();
        assertEquals(1, items.size());
        
        MenuItem item = items.get(0);
        
        assertEquals(ITEM_NAME, item.getName());
        assertEquals(ITEM_DESCRIPTION, 
                item.getDescription());
        assertEquals(3, item.getPrices().size());
        assertEquals(3, item.getQualifiers().size());
        assertEquals(ITEM_PRICE_TAPA, item.getPrices().get(0),0);
        assertEquals(ITEM_PRICE_HALF, item.getPrices().get(1),0);
        assertEquals(ITEM_PRICE_FULL, item.getPrices().get(2),0);
        assertEquals(Qualifiers.TAPA.toString(), item.getQualifiers().get(0));
        assertEquals(Qualifiers.HALF.toString(), item.getQualifiers().get(1));
        assertEquals(Qualifiers.FULL.toString(), item.getQualifiers().get(2));
        assertEquals("", item.getImageString());
        
        
    }
    
    @Test
    public void testDeleteMenuItem(){
        manager.login(REST_NAME, REST_PASSWORD);
        createCategory();
        RestaurantAgent agent = manager.getAgent();
        
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final float ITEM_PRICE = 345f;
        final String BLOB_KEY = null;
        List<Float> prices = new ArrayList<Float>(1);
        List<String> qualifiers = new ArrayList<String>(1);
        prices.add(ITEM_PRICE);
        qualifiers.add(Qualifiers.SINGLE.toString());
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                prices, qualifiers, BLOB_KEY, categoryId,
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        List<MenuItem> items = agent.getMenuItems();
        
        MenuItem item = items.get(0);
        
        agent.deleteMenuItems(new long[]{item.getId()});
        
        assertEquals(0, agent.getMenuItems().size());
        try{
            agent.getMenuItem(item.getId());
                fail();
        }
        catch (NotFoundException e) {
        }
    }
    
    @Test
    public void testModifyMenuItem(){
        manager.login(REST_NAME, REST_PASSWORD);
        createCategory();
        RestaurantAgent agent = manager.getAgent();
        
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final float ITEM_PRICE = 345.45f;
        final String BLOB_KEY = null;
        List<Float> prices = new ArrayList<Float>(1);
        List<String> qualifiers = new ArrayList<String>(1);
        prices.add(ITEM_PRICE);
        qualifiers.add(Qualifiers.SINGLE.toString());
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                prices, qualifiers, BLOB_KEY, categoryId,
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        MenuItem item = agent.getMenuItems().get(0);
        
        final String NEW_ITEM_NAME = "nuevonombre";
        final String NEW_ITEM_DESCRIPTION = "Pescado description new";
        final float NEW_ITEM_PRICE = 35.23f;
        final String NEW_BLOB_KEY = null;
        List<Float> newPrices = new ArrayList<Float>(1);
        List<String> newQualifiers = new ArrayList<String>(1);
        newPrices.add(NEW_ITEM_PRICE);
        newQualifiers.add(Qualifiers.SINGLE.toString());
        final long itemId = item.getId();
        agent.addOrModifyMenuItem(item.getId(), 
                NEW_ITEM_NAME, NEW_ITEM_DESCRIPTION, 
                newPrices, newQualifiers, NEW_BLOB_KEY, categoryId,
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        MenuItem modifiedItem = agent.getMenuItems().get(0);
        
        assertEquals(NEW_ITEM_NAME, modifiedItem.getName());
        assertEquals(NEW_ITEM_DESCRIPTION, 
                modifiedItem.getDescription());
        assertEquals(NEW_ITEM_PRICE, 
                modifiedItem.getPrices().get(0),0);
        assertEquals("", modifiedItem.getImageString());
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
        
        assertNotSame(0, table.getCode().length());
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
    
    @Test
    public void testTwoCategories(){
        final String SECOND_CATEGORY_NAME = "Another category";
        manager.login(REST_NAME, REST_PASSWORD);
        createCategory();
        
        RestaurantAgent agent = manager.getAgent();
        
        final long secondCategoryId = 
                createCategory(SECOND_CATEGORY_NAME);
        final String ITEM_NAME = "pescado";
        final String ITEM_DESCRIPTION = "Pescado description";
        final float ITEM_PRICE = 345f;
        final String BLOB_KEY = null;
        List<Float> prices = new ArrayList<Float>(1);
        List<String> qualifiers = new ArrayList<String>(1);
        prices.add(ITEM_PRICE);
        qualifiers.add(Qualifiers.SINGLE.toString());
        agent.addOrModifyMenuItem(null, 
                ITEM_NAME, ITEM_DESCRIPTION, 
                prices, qualifiers, BLOB_KEY, categoryId,
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        
        final String SECOND_ITEM_NAME = "carne";
        final String SECOND_ITEM_DESCRIPTION = "carne description";
        final float SECOND_ITEM_PRICE = 3435f;
        final String SECOND_BLOB_KEY = null;
        List<Float> secondPrices = new ArrayList<Float>(1);
        List<String> secondQualifiers = new ArrayList<String>(1);
        secondPrices.add(SECOND_ITEM_PRICE);
        secondQualifiers.add(Qualifiers.SINGLE.toString());
        agent.addOrModifyMenuItem(null, 
                SECOND_ITEM_NAME, SECOND_ITEM_DESCRIPTION, 
                secondPrices, secondQualifiers, SECOND_BLOB_KEY, secondCategoryId,
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        List<MenuItem> categoryOneItems = agent.getMenuItems(categoryId);
        List<MenuItem> categoryTwoItems = agent.getMenuItems(secondCategoryId);
        
        assertEquals(1, categoryOneItems.size());
        assertEquals(1, categoryTwoItems.size());
        
        assertEquals(ITEM_NAME, categoryOneItems.get(0).getName());
        assertEquals(SECOND_ITEM_NAME, categoryTwoItems.get(0).getName());
        
        
    }

    @Test
    public void testDeleteCategory(){
        manager.login(REST_NAME, REST_PASSWORD);
        List<MenuCategory> categories = manager.getAgent().getCategories();
        assertEquals(4, categories.size());
        MenuCategory category = categories.get(2);
        final String name = category.getName();
        manager.getAgent().deleteCategories(new long[]{category.getId()});
        categories = manager.getAgent().getCategories();
        assertEquals(3, categories.size());
        for(MenuCategory currentCategory : categories){
            assertNotSame(name, currentCategory.getName());
        }
    }
}
