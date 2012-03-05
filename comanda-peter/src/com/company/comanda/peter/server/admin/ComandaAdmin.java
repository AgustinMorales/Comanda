package com.company.comanda.peter.server.admin;

public interface ComandaAdmin {

    long createRestaurant(String name, String login,
            String password,
            String address,
            String description,
            String imageBlob);
    
}
