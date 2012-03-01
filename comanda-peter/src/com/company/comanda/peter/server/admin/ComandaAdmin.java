package com.company.comanda.peter.server.admin;

public interface ComandaAdmin {

    long createRestaurant(String name, String password,
            String address);
    
    long createRestaurant(String name, String password, 
            double latitude, double longitude);
}
