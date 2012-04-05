package com.company.comanda.peter.server.admin;

import java.util.List;

import com.company.comanda.peter.server.model.Restaurant;

public interface ComandaAdmin {

    long createRestaurant(String name, String login,
            String password,
            String address,
            String description,
            String imageBlob);
    
    List<Restaurant> getRestaurants();
}
