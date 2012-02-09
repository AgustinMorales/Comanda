package com.company.comanda.peter.server;

public interface RestaurantManager {

    boolean login(String username, String password);
    
    RestaurantAgent getAgent();
}
