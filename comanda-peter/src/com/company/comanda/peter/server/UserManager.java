package com.company.comanda.peter.server;

import java.util.List;

import com.company.comanda.peter.server.model.Bill;
import com.company.comanda.peter.server.model.MenuCategory;
import com.company.comanda.peter.server.model.MenuItem;
import com.company.comanda.peter.server.model.Order;
import com.company.comanda.peter.server.model.Restaurant;
import com.company.comanda.peter.server.model.Table;
import com.company.comanda.peter.shared.BillType;

public interface UserManager {

    public static class CodifiedData{
        public Restaurant restaurant;
        public Table table;
        
    }
    
    long registerUser(String phoneNumber, 
            String password,
            String validationCode);
    
    String placeOrder(long userId, String password, 
            long restaurantId,
            List<Long> menuItemIds, 
            List<Integer> menuItemQualifierIndexes,
            List<String> menuItemComments, 
            String address, Long tableId,
            String comments,
            BillType type,
            String billKeyString);
    
    CodifiedData getData(String code);
    
    List<MenuItem> getMenuItems(long restaurantId);
    
    List<Restaurant> searchRestaurant(double latitude,
            double longitude, int maxResults, double maxDistance);
    
    List<MenuCategory> getMenuCategories(long restaurantId);
    
    List<Bill> getBills(long userId, String password);
    
    List<Order> getOrders(long userId, String password, String billKeyString);
}
