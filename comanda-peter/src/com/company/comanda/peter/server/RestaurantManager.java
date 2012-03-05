package com.company.comanda.peter.server;

public interface RestaurantManager {

    boolean login(String login, String password);
    
    RestaurantAgent getAgent();
}
