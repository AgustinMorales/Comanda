package com.company.comanda.peter;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.SessionAttributesFactory;
import com.company.comanda.peter.server.UserManager;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.notification.NotificationManager;
import com.company.comanda.peter.shared.Qualifiers;
import com.company.comanda.peter.stubs.NotificationManagerStub;
import com.company.comanda.peter.stubs.SessionAttributesHashMap;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

public class TestAdmin {

    private ComandaAdmin admin;
    private RestaurantManager manager;
    private UserManager userManager;
    private Objectify ofy;
    
    
    private static final Logger log = 
            LoggerFactory.getLogger(TestAdmin.class);
    
    
    
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
        admin = injector.getInstance(ComandaAdmin.class);
        manager = injector.getInstance(RestaurantManager.class);
        userManager = injector.getInstance(UserManager.class);
        ofy = injector.getInstance(Objectify.class);
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
        ((SessionAttributesHashMap)
                injector.getInstance(
                        SessionAttributesFactory.class).create()).clear();
    }
    
    @Test
    public void testGeoCreateRestaurant(){
        final long sevillaId = admin.createOrModifyRestaurant(null,"Sevillano",
                "sevillano", "sevillano", "Puerto de envalira, 1, Sevilla", 
                "description", null, "678",0,0, null);
        final long madrid1Id = admin.createOrModifyRestaurant(null, "En Madrid",
                "madrid", "madrid", "Lola Membrives, 13, Madrid", 
                "Description - Madrid", null, "678",0,3, null);
        final long madrid2Id = admin.createOrModifyRestaurant(null, "Otro de madrid",
                "madrid2", "madrid", "Lola Membrives, 12, Madrid",
                "description", null, "678",0,4, null);
        
        final UserManager userManager = 
                injector.getInstance(UserManager.class);
        
        List<Restaurant> enSevilla = 
                userManager.searchRestaurant(
                        37.375236, -5.957757, 
                        10, 5000);
        List<Restaurant> enMadrid =
                userManager.searchRestaurant(40.396928,
                        -3.717177, 10, 5000);
        
        Assert.assertEquals(1, enSevilla.size());
        Assert.assertEquals(2, enMadrid.size());
        
        Assert.assertEquals(sevillaId, 
                (long)enSevilla.get(0).getId());
        
        List<Long> madridIds = new ArrayList<Long>(2);
        
        for(Restaurant restaurant: enMadrid){
            madridIds.add(restaurant.getId());
        }
        
        Assert.assertTrue(madridIds.contains(madrid1Id));
        Assert.assertTrue(madridIds.contains(madrid2Id));
    }
    
    @Test
    public void testDuplicateLogin(){
        final long madrid1Id = admin.createOrModifyRestaurant(null,"En Madrid",
                "madrid", "madrid", "Lola Membrives, 13, Madrid", 
                "Description - Madrid", null, "678", 10,
                20, null);
        try{
            final long madrid2Id = admin.createOrModifyRestaurant(null, "Otro de madrid",
                    "madrid", "madrid", "Lola Membrives, 12, Madrid",
                    "description", null, "678",1,10, null);
            Assert.fail();
        }
        catch(IllegalArgumentException e){
        }
    }
    @Test
    public void testModify(){
        final long madrid1Id = admin.createOrModifyRestaurant(null,"En Madrid",
                "madrid", "madrid", "Lola Membrives, 13, Madrid", 
                "Description - Madrid", null, "678",2,3, null);
        List<Restaurant> restaurants = admin.getRestaurants();
        Assert.assertEquals(restaurants.size(), 1);
        Restaurant restaurant = restaurants.get(0);
        Assert.assertEquals(restaurant.getPhone(), "678");
        final long madridId2 = admin.createOrModifyRestaurant(
                restaurant.getKeyString(), "Otro", "loginOtro", null, "yoquese"
                , "descriptiooooon", null, "456",3,10, null);
        Assert.assertEquals(madrid1Id, madridId2);
        
        restaurants = admin.getRestaurants();
        Assert.assertEquals(restaurants.size(), 1);
        Assert.assertEquals(restaurants.get(0).getPhone(), "456");
    }
    
    @Test
    public void testCopy(){
        final long madridId = admin.createOrModifyRestaurant(null,"En Madrid",
                "madrid", "madrid", "Lola Membrives, 13, Madrid", 
                "Description - Madrid", null, "678",2,3, null);
        List<Restaurant> restaurants = admin.getRestaurants();
        Assert.assertEquals(restaurants.size(), 1);
        Restaurant madridRestaurant = restaurants.get(0);
        manager.login("madrid", "madrid");
        long categoryId = manager.getAgent().addOrModifyMenuCategory(null, "category1");
        ArrayList<Float> prices = new ArrayList<Float>(1);
        ArrayList<String> qualifiers= new ArrayList<String>(1);
        qualifiers.add(Qualifiers.SINGLE.toString());
        prices.add(33f);
        manager.getAgent().addOrModifyMenuItem(null, "cosa", "descriptionCosa", prices, qualifiers, null, categoryId, 
                new ArrayList<String>(), new ArrayList<Float>(), null);
        
        final long sevillaId = admin.createOrModifyRestaurant(null, "sevilla", "sevilla", "sevilla", 
                "Puerto de Envalira, 2, Sevilla", "Description - Sevilla", null, "567", 4, 5,
                madridRestaurant.getKeyString());
        
        Restaurant sevillaRestaurant = ofy.get(new Key<Restaurant>(Restaurant.class, sevillaId));
        
        Assert.assertNotSame(madridId, sevillaId);
        
        
        List<MenuCategory> madridCategories = userManager.getMenuCategories(madridId);
        List<MenuCategory> sevillaCategories = userManager.getMenuCategories(sevillaId);
        
        Assert.assertEquals(5, madridCategories.size());
        Assert.assertEquals(5, sevillaCategories.size());
        
        MenuCategory madridCategory = madridCategories.get(4);
        MenuCategory sevillaCategory = sevillaCategories.get(4);
        
        Assert.assertEquals(madridCategory.getName(), sevillaCategory.getName());
        Assert.assertNotSame(madridCategory.getId(), sevillaCategory.getId());
        Assert.assertEquals(madridRestaurant.getKeyString(), madridCategory.getRestaurant().getString());
        Assert.assertEquals(sevillaRestaurant.getKeyString(), sevillaCategory.getRestaurant().getString());
        
        List<MenuItem> madridMenuItems = userManager.getMenuItems(madridId);
        List<MenuItem> sevillaMenuItems = userManager.getMenuItems(sevillaId);
        
        Assert.assertEquals(1, madridMenuItems.size());
        Assert.assertEquals(1, sevillaMenuItems.size());
        
        MenuItem madridMenuItem = madridMenuItems.get(0);
        MenuItem sevillaMenuItem = sevillaMenuItems.get(0);
        
        Assert.assertEquals(madridMenuItem.getName(), sevillaMenuItem.getName());
        Assert.assertNotSame(madridMenuItem.getId(), sevillaMenuItem.getId());
        Assert.assertEquals(madridRestaurant.getKeyString(), madridMenuItem.getParent().getString());
        Assert.assertEquals(sevillaRestaurant.getKeyString(), sevillaMenuItem.getParent().getString());
        
        
    }
}
