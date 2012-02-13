package com.company.comanda.peter.server.guice;

import javax.inject.Singleton;

import com.company.comanda.peter.server.RestaurantAgent;
import com.company.comanda.peter.server.RestaurantAgentFactory;
import com.company.comanda.peter.server.RestaurantAgentImpl;
import com.company.comanda.peter.server.RestaurantManagementImpl;
import com.company.comanda.peter.server.RestaurantManager;
import com.company.comanda.peter.server.UserManager;
import com.company.comanda.peter.server.UserManagerImpl;
import com.company.comanda.peter.server.admin.ComandaAdmin;
import com.company.comanda.peter.server.admin.ComandaAdminImpl;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.server.model.User;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class BusinessModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
        .implement(RestaurantAgent.class, RestaurantAgentImpl.class)
        .build(RestaurantAgentFactory.class));
        bind(ComandaAdmin.class).to(ComandaAdminImpl.class);
        bind(RestaurantManager.class).to(
                RestaurantManagementImpl.class);
        bind(UserManager.class).to(UserManagerImpl.class);
        
    }
    
    @Provides @Singleton
    Objectify provideObjectify(){
        Objectify ofy = ObjectifyService.begin();
        ObjectifyService.register(Restaurant.class);
        ObjectifyService.register(MenuItem.class);
        ObjectifyService.register(Order.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Table.class);
        return ofy;
    }

}
