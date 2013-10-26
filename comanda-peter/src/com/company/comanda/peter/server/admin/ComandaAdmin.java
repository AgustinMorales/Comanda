package com.company.comanda.peter.server.admin;

import java.util.List;

import com.company.comanda.peter.server.model.Restaurant;

public interface ComandaAdmin {

    long createOrModifyRestaurant(
            String restaurantKeyString,
            String name, String login,
            String password,
            String description,
            String imageBlob,
            String phone,
            String address,
            double latitude, double longitude,
            float deliveryCost,
            float minimumForDelivery,
            double maxDeliveryDistance,
            String copyMenuItemsFromRestKeyString);
    
    long createOrModifyRestaurant(
            String restaurantKeyString, String name, String login,
            String password, String address,
            String description, String imageBlob,
            String phone,
            float deliveryCost,
            float minimumForDelivery,
            double maxDeliveryDistance,
            String copyMenuItemsFromRestKeyString);
    
    List<Restaurant> getRestaurants();
}
