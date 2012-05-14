package com.company.comanda.peter.server;

public interface RestaurantManager {

    String login(String login, String password);
    
    String login(String token);
    
    RestaurantAgent getAgent();
    
    RestaurantAgent getAgent(String login, String password);
    
    RestaurantAgent getAgent(long restaurantId);
}
