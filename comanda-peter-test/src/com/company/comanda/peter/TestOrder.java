package com.company.comanda.peter;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.SessionAttributes;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.guice.BusinessModule;
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
    
    private RestaurantManager manager;
    
    private long restaurantId;
    
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
    public void test() {
        fail("Not yet implemented");
    }

}
