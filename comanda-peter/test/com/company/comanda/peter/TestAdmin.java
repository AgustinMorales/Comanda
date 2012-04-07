package com.company.comanda.peter;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.SessionAttributesFactory;
import com.company.comanda.peter.server.UserManager;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.stubs.SessionAttributesHashMap;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class TestAdmin {

    private ComandaAdmin admin;
    
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

                }

            });


    @Before
    public void setUp() throws Exception {
        helper.setUp();
        admin = injector.getInstance(ComandaAdmin.class);
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
                "description", null, "678");
        final long madrid1Id = admin.createOrModifyRestaurant(null, "En Madrid",
                "madrid", "madrid", "Lola Membrives, 13, Madrid", 
                "Description - Madrid", null, "678");
        final long madrid2Id = admin.createOrModifyRestaurant(null, "Otro de madrid",
                "madrid2", "madrid", "Lola Membrives, 12, Madrid",
                "description", null, "678");
        
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
                "Description - Madrid", null, "678");
        try{
            final long madrid2Id = admin.createOrModifyRestaurant(null, "Otro de madrid",
                    "madrid", "madrid", "Lola Membrives, 12, Madrid",
                    "description", null, "678");
            Assert.fail();
        }
        catch(IllegalArgumentException e){
        }
    }
    @Test
    public void testModify(){
        final long madrid1Id = admin.createOrModifyRestaurant(null,"En Madrid",
                "madrid", "madrid", "Lola Membrives, 13, Madrid", 
                "Description - Madrid", null, "678");
        List<Restaurant> restaurants = admin.getRestaurants();
        Assert.assertEquals(restaurants.size(), 1);
        Restaurant restaurant = restaurants.get(0);
        Assert.assertEquals(restaurant.getPhone(), "678");
        final long madridId2 = admin.createOrModifyRestaurant(
                restaurant.getKeyString(), "Otro", "loginOtro", null, "yoquese"
                , "descriptiooooon", null, "456");
        Assert.assertEquals(madrid1Id, madridId2);
        
        restaurants = admin.getRestaurants();
        Assert.assertEquals(restaurants.size(), 1);
        Assert.assertEquals(restaurants.get(0).getPhone(), "456");
    }
}
