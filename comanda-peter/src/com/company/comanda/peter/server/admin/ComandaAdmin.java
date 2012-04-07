package com.company.comanda.peter.server.admin;

import java.util.List;

import com.company.comanda.peter.server.model.Restaurant;

public interface ComandaAdmin {

    long createOrModifyRestaurant(
            String restaurantKeyString,
            String name, String login,
            String password,
            String address,
            String description,
            String imageBlob,
            String phone);
    
    List<Restaurant> getRestaurants();
}
