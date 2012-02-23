package com.company.comanda.peter.server;

import java.util.List;

import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;

public interface UserManager {

    public static class CodifiedData{
        public Restaurant restaurant;
        public Table table;
        
    }
    
    long registerUser(String phoneNumber, 
            String password,
            String validationCode);
    
    void placeOrder(long userId, String password, 
            long restaurantId,
            long menuItemId, Long tableId);
    
    CodifiedData getData(String code);
    
    List<MenuItem> getMenuItems(long restaurantId);
    
    List<Restaurant> searchRestaurant(double latitude,
            double longitude, int maxResults, double maxDistance);
    
    List<MenuCategory> getMenuCategories(long restaurantId);
}
